package co.edu.uniquindio.controller.objects.compra;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.service.objects.compra.CompraService;
import co.edu.uniquindio.service.utils.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RestController
@RequestMapping("/api/compra")
@RequiredArgsConstructor
public class CompraController {


    private final StripeService stripeService;
    private final CompraService compraService;


    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/crear-sesion-pago")
    public ResponseEntity<?> crearSesion(@RequestBody CarritoCompraDto carrito) throws StripeException {
            // URLs de éxito y cancelación (ajusta a tus rutas en frontend)
        String successUrl = "https://storeit2-77c20.web.app/cliente/productos";
        String cancelUrl = "https://storeit2-77c20.web.app/cliente/carrito-compra";


        String checkoutUrl = stripeService.crearSesionCheckout(carrito, successUrl, cancelUrl);
            return ResponseEntity.ok().body(Map.of("url", checkoutUrl));
    }


    
    @PostMapping("/webhook")
    public ResponseEntity<String> manejarEventoStripe(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            // Verifica el evento con la clave secreta del webhook
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );
        } catch (SignatureVerificationException e) {
            // Si la firma no es válida, responde con error
            return ResponseEntity.badRequest().body("Firma inválida");
        }

        // Verifica si el evento corresponde a un pago exitoso
        if ("checkout.session.completed".equals(event.getType())) {
            // Llama al servicio para registrar factura y movimiento
            compraService.registrarCompra(event);
        }

        return ResponseEntity.ok("Evento recibido correctamente");
    }
}
