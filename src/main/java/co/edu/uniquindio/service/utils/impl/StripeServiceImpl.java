package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.service.utils.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public String crearSesionCheckout(CarritoCompraDto carrito, String successUrl, String cancelUrl) throws StripeException {
        // Establece la clave de API (obligatorio antes de hacer llamadas a Stripe)
        com.stripe.Stripe.apiKey = stripeApiKey;

        // Construimos los line items según los items del carrito
        List<SessionCreateParams.LineItem> lineItems = carrito.itemsCarrito().stream().map(i ->
                SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("cop")
                                        .setUnitAmount((long) (i.valorTotal() / i.cantidad() * 100)) // convertir a centavos
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Producto ID: " + i.idProducto())
                                                        .build()
                                        )
                                        .build()
                        )
                        .setQuantity((long) i.cantidad())
                        .build()
        ).collect(Collectors.toList());

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .setClientReferenceId(String.valueOf(carrito.idCliente()))
                .build();

        // 🇺🇸 Create the checkout session
        // 🇪🇸 Crea la sesión de pago
        Session session = Session.create(params);

        return session.getUrl();
    }
}

