package co.edu.uniquindio.service.objects.compra;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.EliminarItemsCarritoDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.ModificarCantidadItemsDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;

public interface CarritoCompraService {


    CarritoCompra generarCarritoCliente(Cliente cliente);


    CarritoCompraDto obtenerCarritoCompraCliente(Long idCliente) throws ElementoNoEncontradoException;


    void agregarProductoCarritoCompra(RegistroItemCompraDto registroItemCompraDto)
            throws ElementoNoEncontradoException, ElementoRepetidoException;


    void modificarCantidadProductoCarrito(ModificarCantidadItemsDto modificarCantidadItemsDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException;


    void eliminarProductoCarritoCompra(EliminarItemsCarritoDto eliminarProductoCarritoDto)
            throws ElementoNoEncontradoException;

}
