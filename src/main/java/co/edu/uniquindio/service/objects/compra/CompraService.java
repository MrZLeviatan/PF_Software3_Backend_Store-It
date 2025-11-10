package co.edu.uniquindio.service.objects.compra;

import com.stripe.model.Event;

public interface CompraService {

    void registrarCompra(Event event);

}
