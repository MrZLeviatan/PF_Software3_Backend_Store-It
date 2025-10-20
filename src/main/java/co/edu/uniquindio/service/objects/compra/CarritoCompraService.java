package co.edu.uniquindio.service.objects.compra;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;

public interface CarritoCompraService {


    CarritoCompra generarCarritoCliente(Cliente cliente);

    void agregarProductoCarritoCompra(RegistroItemCompraDto registroItemCompraDto)
            throws ElementoNoEncontradoException;



}
