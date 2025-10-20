package co.edu.uniquindio.controller.objects.compra;


import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrito-compra")
@RequiredArgsConstructor
public class CarrioCompraController {


    private final CarritoCompraService carritoCompraService;

}
