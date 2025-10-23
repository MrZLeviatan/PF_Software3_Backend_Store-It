package co.edu.uniquindio.service.objects.compra.impl;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.EliminarItemsCarritoDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.ModificarCantidadItemsDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.mapper.objects.compra.CarritoCompraMapper;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import co.edu.uniquindio.service.users.ClienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraServiceImpl implements CarritoCompraService {

    private final CarritoCompraRepo carritoCompraRepo;
    private final CarritoCompraMapper carritoCompraMapper;
    private final ProductoRepo productoRepo;
    private final ClienteRepo clienteRepo;


    @Override
    public CarritoCompra generarCarritoCliente(Cliente cliente) {
        CarritoCompra carritoCompra = new CarritoCompra();
        carritoCompra.setCliente(cliente);
        carritoCompra.setItemsCarrito(new ArrayList<>());
        carritoCompra.setTotalValor(0.0);
        return carritoCompra;
    }

    @Override
    public CarritoCompraDto obtenerCarritoCompraCliente(Long idCliente) throws ElementoNoEncontradoException {
        // Obtener cliente
        Cliente cliente = clienteRepo.findById(idCliente).
                orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito, sino crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }
        return carritoCompraMapper.toDto(cliente.getCarritoCompra());
    }


    @Override
    @Transactional
    public void agregarProductoCarritoCompra(RegistroItemCompraDto registroItemCompraDto) throws ElementoNoEncontradoException {

        // Obtener cliente
        Cliente cliente = clienteRepo.findById(registroItemCompraDto.
                idCliente()).
                orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito, sino crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }

        // Buscar producto
        Producto producto = productoRepo.findById(registroItemCompraDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // Verificar si el producto ya está en el carrito
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        ItemsCarrito itemCarrito;
        if (itemExistenteOpt.isPresent()) {
            // Si ya existe el item, se aumenta la cantidad
            itemCarrito = itemExistenteOpt.get();
            itemCarrito.setCantidad(itemCarrito.getCantidad() + registroItemCompraDto.cantidad());
        } else {
            // Si no existe, se crea un nuevo item
            itemCarrito = new ItemsCarrito();
            itemCarrito.setProducto(producto);
            itemCarrito.setCantidad(registroItemCompraDto.cantidad());
            itemCarrito.setCarritoCompra(carritoCompra);
            carritoCompra.getItemsCarrito().add(itemCarrito);
        }

        // Se recalcula el total del item y del carrito
        itemCarrito.setValorTotal(producto.getValorVenta() * itemCarrito.getCantidad());
        carritoCompra.calcularTotal();
        carritoCompraRepo.save(carritoCompra);
    }


    @Override
    public void agregarCantidadProductoCarrito(ModificarCantidadItemsDto agregarCantidadItemsDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        // Obtener cliente
        Cliente cliente = clienteRepo.findById(agregarCantidadItemsDto.idCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito, sino crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }

        // Buscar producto
        Producto producto = productoRepo.findById(agregarCantidadItemsDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // Verificar si el producto ya está en el carrito
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            ItemsCarrito itemCarrito = itemExistenteOpt.get();

            // Verificar que el EspacioProducto tenga suficiente cantidad
            int cantidadDisponible = producto.getEspacioProducto() != null ?
                    producto.getEspacioProducto().getCantidadTotal() : 0;

            if (cantidadDisponible < itemCarrito.getCantidad() + agregarCantidadItemsDto.cantidadAgregar()) {
                throw new ElementoNoValidoException(
                        "No hay suficiente cantidad disponible en el inventario para agregar este producto");
            }

            // Si hay suficiente, se aumenta la cantidad
            itemCarrito.setCantidad(itemCarrito.getCantidad() + agregarCantidadItemsDto.cantidadAgregar());
            itemCarrito.setValorTotal(producto.getValorVenta() * itemCarrito.getCantidad());

            // Recalcular total del carrito
            carritoCompra.calcularTotal();
            carritoCompraRepo.save(carritoCompra);

        } else {
            throw new ElementoNoEncontradoException("El producto no existe en el carrito");
        }
    }



    @Override
    @Transactional
    public void quitarCantidadProductoCarrito(ModificarCantidadItemsDto quitarCantidadItemsDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        // Obtener cliente
        Cliente cliente = clienteRepo.findById(quitarCantidadItemsDto.idCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito, sino crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }

        // Buscar producto
        Producto producto = productoRepo.findById(quitarCantidadItemsDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // Verificar si el producto ya está en el carrito
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            ItemsCarrito itemCarrito = itemExistenteOpt.get();

            // Verificar que no se intente quitar más de lo que hay
            if (itemCarrito.getCantidad() < quitarCantidadItemsDto.cantidadAgregar()) {
                throw new ElementoNoValidoException("No se puede quitar más de la cantidad total del producto");
            }

            // Disminuir la cantidad
            itemCarrito.setCantidad(itemCarrito.getCantidad() - quitarCantidadItemsDto.cantidadAgregar());

            // Recalcular el valor total del item
            itemCarrito.setValorTotal(producto.getValorVenta() * itemCarrito.getCantidad());

            // Recalcular el total del carrito
            carritoCompra.calcularTotal();

            // Guardar cambios en la base de datos
            carritoCompraRepo.save(carritoCompra);

        } else {
            throw new ElementoNoEncontradoException("El producto no existe en el carrito");
        }
    }





    @Override
    @Transactional
    public void eliminarProductoCarritoCompra(EliminarItemsCarritoDto eliminarProductoCarritoDto) throws ElementoNoEncontradoException {
        // 1️⃣ Obtener cliente
        Cliente cliente = clienteRepo.findById(eliminarProductoCarritoDto.idCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // 2️⃣ Obtener carrito del cliente
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null || carritoCompra.getItemsCarrito() == null || carritoCompra.getItemsCarrito().isEmpty()) {
            throw new ElementoNoEncontradoException("El carrito está vacío o no existe");
        }

        // 3️⃣ Buscar el item en el carrito por id del producto
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(eliminarProductoCarritoDto.idProducto()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            // 4️⃣ Si existe, se elimina del carrito
            ItemsCarrito itemCarrito = itemExistenteOpt.get();
            carritoCompra.getItemsCarrito().remove(itemCarrito);
            itemCarrito.setCarritoCompra(null); // Desvincular la relación

            // 5️⃣ Recalcular el total del carrito
            carritoCompra.calcularTotal();

            // 6️⃣ Guardar cambios
            carritoCompraRepo.save(carritoCompra);
        } else {
            throw new ElementoNoEncontradoException("El producto no existe en el carrito");
        }
    }


}
