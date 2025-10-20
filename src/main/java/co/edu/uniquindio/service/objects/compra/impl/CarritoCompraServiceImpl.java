package co.edu.uniquindio.service.objects.compra.impl;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
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
    @Transactional
    public void agregarProductoCarritoCompra(RegistroItemCompraDto registroItemCompraDto) throws ElementoNoEncontradoException {

        // 1️⃣ Obtener cliente
        Cliente cliente = clienteRepo.findById(registroItemCompraDto.
                idCliente()).
                orElseThrow(() -> new ElementoNoEncontradoException("Cliente no encontrado"));

        // 2️⃣ Verificar si el cliente ya tiene un carrito, sino crear uno
        CarritoCompra carritoCompra = cliente.getCarritoCompra();
        if (carritoCompra == null) {
            carritoCompra = generarCarritoCliente(cliente);
            cliente.setCarritoCompra(carritoCompra);
        }

        // 3️⃣ Buscar producto
        Producto producto = productoRepo.findById(registroItemCompraDto.idProducto())
                .orElseThrow(() -> new ElementoNoEncontradoException("El producto no existe"));

        // 4️⃣ Verificar si el producto ya está en el carrito
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
        carritoCompraRepo.save(carritoCompra);
    }


}
