package co.edu.uniquindio.service.objects.compra;

import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;

public interface CarritoCompraService {


    CarritoCompra generarCarritoCliente(Cliente cliente);

}
