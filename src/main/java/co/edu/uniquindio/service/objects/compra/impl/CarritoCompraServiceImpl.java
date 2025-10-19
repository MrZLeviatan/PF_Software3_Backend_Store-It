package co.edu.uniquindio.service.objects.compra.impl;

import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarritoCompraServiceImpl implements CarritoCompraService {

    private final CarritoCompraRepo carritoCompraRepo;


    @Override
    public CarritoCompra generarCarritoCliente(Cliente cliente) {
        CarritoCompra carritoCompra = new CarritoCompra();
        carritoCompra.setCliente(cliente);
        carritoCompra.setItemsCarrito(new ArrayList<>());
        carritoCompra.setTotalValor(0.0);
        return carritoCompra;
    }


}
