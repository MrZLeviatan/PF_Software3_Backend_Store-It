package co.edu.uniquindio.controller.objects.compra;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.service.utils.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/compra")
@RequiredArgsConstructor
public class CompraController {


    private final StripeService stripeService;


    @PostMapping("/crear-sesion-pago")
    public ResponseEntity<?> crearSesion(@RequestBody CarritoCompraDto carrito) throws StripeException {
            // URLs de éxito y cancelación (ajusta a tus rutas en frontend)
        String successUrl = "http://localhost:4200/cliente/productos";
        String cancelUrl = "http://localhost:4200/cliente/carrito-compra";


        String checkoutUrl = stripeService.crearSesionCheckout(carrito, successUrl, cancelUrl);
            return ResponseEntity.ok().body(Map.of("url", checkoutUrl));
    }

}
