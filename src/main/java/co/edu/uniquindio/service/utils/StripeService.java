package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import com.stripe.exception.StripeException;

public interface StripeService {


    String crearSesionCheckout(CarritoCompraDto carritoCompraDto, String successUrl, String cancelUrl)
            throws StripeException;
}
