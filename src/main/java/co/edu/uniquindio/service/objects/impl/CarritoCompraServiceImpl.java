package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.service.objects.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarritoCompraServiceImpl implements CarritoCompraService {


    @Override
    public CarritoCompra generarCarritoCliente(Cliente cliente) {
        CarritoCompra carritoCompra = new CarritoCompra();
        carritoCompra.setCliente(cliente);
        return carritoCompra;
    }


}
