package co.edu.uniquindio.service.objects.compra.impl;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.EliminarItemsCarritoDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.ModificarCantidadItemsDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.mapper.objects.compra.CarritoCompraMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.repository.objects.inventario.LoteRepo;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import co.edu.uniquindio.service.users.ClienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraServiceImpl implements CarritoCompraService {

    private final CarritoCompraRepo carritoCompraRepo;
    private final CarritoCompraMapper carritoCompraMapper;
    private final ProductoRepo productoRepo;
    private final ClienteRepo clienteRepo;
    private final LoteRepo loteRepo;


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
    public void agregarProductoCarritoCompra(RegistroItemCompraDto registroItemCompraDto)
            throws ElementoNoEncontradoException, ElementoRepetidoException {

        // Obtener cliente
        Cliente cliente = clienteRepo.findById(registroItemCompraDto.
                idCliente()).
                orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito, si no crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }

        // Buscar producto
        Producto producto = productoRepo.findById(registroItemCompraDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // Ver si existen Lotes del Producto
        EspacioProducto espacioProducto = producto.getEspacioProducto();
        List<Lote> lotes = loteRepo.findByEspacioProductoAndEstadoLote(espacioProducto, EstadoLote.ACTIVO);

        if (lotes.isEmpty()) {
            throw new ElementoNoEncontradoException("No se puede agregar el producto, no hay lotes disponibles");
        }

        // Verificar si el producto ya está en el carrito
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        ItemsCarrito itemCarrito;
        if (itemExistenteOpt.isPresent()) {
            // Si ya existe el item, se envía una excepción de aviso
            throw new ElementoRepetidoException("El producto ya se encuentra en tu carrito de compras");
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
    public void modificarCantidadProductoCarrito(ModificarCantidadItemsDto modificarCantidadItemsDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        // Obtener cliente
        Cliente cliente = clienteRepo.findById(modificarCantidadItemsDto.idCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Verificar si el cliente ya tiene un carrito
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            throw new ElementoNoValidoException("Carrito no existente del Cliente");
        }

        // Buscar producto
        Producto producto = productoRepo.findById(modificarCantidadItemsDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // Verificar si el producto tiene un espacio asignado
        EspacioProducto espacio = producto.getEspacioProducto();
        if (espacio == null) {
            throw new ElementoNoValidoException("El producto no tiene un espacio asignado");
        }

        // Se obtienen los lotes del espacio
        List<Lote> lotes = espacio.getLotes();
        if (lotes == null || lotes.isEmpty()) {
            throw new ElementoNoValidoException("No existen lotes asociados al espacio del producto");
        }

        // Ordenas los lotes dependiendo del tipo de producto
        if (producto.getTipoProducto() == TipoProducto.PERECEDEROS) {
            lotes.sort(Comparator.comparing(Lote::getFechaVencimiento));
        } else {
            lotes.sort(Comparator.comparing(Lote::getFechaIngreso));
        }

        // Calcular cantidad total disponible en los lotes
        int cantidadDisponible = lotes.stream()
                .mapToInt(Lote::getCantidadDisponible)
                .sum();

        // Verificar si hay suficiente cantidad en stock / Verify if enough stock exists
        int cantidadSolicitada = modificarCantidadItemsDto.cantidad();
        if (cantidadSolicitada > cantidadDisponible) {
            throw new ElementoNoValidoException("No hay suficiente cantidad disponible en inventario.");
        }

        //  Si hay stock suficiente, actualizamos la cantidad en el carrito
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            ItemsCarrito itemCarrito = itemExistenteOpt.get();

            // Actualizar cantidad del carrito
            itemCarrito.setCantidad(cantidadSolicitada);

            // Recalcular total del carrito
            carritoCompra.calcularTotal();
            carritoCompraRepo.save(carritoCompra);
        }

    }



    @Override
    @Transactional
    public void eliminarProductoCarritoCompra(EliminarItemsCarritoDto eliminarProductoCarritoDto) throws ElementoNoEncontradoException {
        // Obtener cliente
        Cliente cliente = clienteRepo.findById(eliminarProductoCarritoDto.idCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // Obtener carrito del cliente
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null || carritoCompra.getItemsCarrito() == null || carritoCompra.getItemsCarrito().isEmpty()) {
            throw new ElementoNoEncontradoException("El carrito está vacío o no existe");
        }

        // Buscar el item en el carrito por ID del producto
        Optional<ItemsCarrito> itemExistenteOpt = carritoCompra.getItemsCarrito()
                .stream()
                .filter(item -> item.getProducto().getId().equals(eliminarProductoCarritoDto.idProducto()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            // Si existe, se elimina del carrito
            ItemsCarrito itemCarrito = itemExistenteOpt.get();
            carritoCompra.getItemsCarrito().remove(itemCarrito);
            itemCarrito.setCarritoCompra(null); // Desvincular la relación

            //  Recalcular el total del carrito
            carritoCompra.calcularTotal();

            // Guardar cambios
            carritoCompraRepo.save(carritoCompra);
        } else {
            throw new ElementoNoEncontradoException("El producto no existe en el carrito");
        }
    }


}
