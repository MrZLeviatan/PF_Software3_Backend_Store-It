package co.edu.uniquindio.service.objects.compra.impl;

import co.edu.uniquindio.models.entities.objects.compra.*;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.EstadoCompra;
import co.edu.uniquindio.repository.objects.compra.CompraRepo;
import co.edu.uniquindio.repository.objects.compra.FacturaRepo;
import co.edu.uniquindio.repository.objects.inventario.LoteRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.CompraService;
import co.edu.uniquindio.service.utils.EmailService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {


    private final CompraRepo compraRepo;
    private final FacturaRepo facturaRepo;
    private final ClienteRepo clienteRepo;
    private final EmailService emailService;
    private final LoteRepo loteRepo;


    @Override
    @Transactional
    public void registrarCompra(Event event) {

        // Convierte el evento Stripe a una sesión
        Session session = (Session) ApiResource.GSON.fromJson(
                event.getDataObjectDeserializer().getRawJson(), Session.class
        );

        // Extrae los datos relevantes de la sesión
        String clienteId = session.getClientReferenceId();
        String paymentStatus = session.getPaymentStatus();


        // Verifica que el pago haya sido exitoso
        if ("paid".equalsIgnoreCase(paymentStatus)) {
            try {
                // Convierte el clientReferenceId (que viene como String) a Long
                Long idCliente = Long.parseLong(clienteId);

                // Llama al método para generar la compra, la factura y enviar el correo
                generarCompraCliente(idCliente);

            } catch (NumberFormatException e) {
                System.err.println("❌ clientReferenceId no es un número válido: " + clienteId);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("⚠️ Error al registrar la compra del cliente " + clienteId + ": " + e.getMessage());
            }
        } else {
            System.out.println("💤 Pago no completado, no se genera la compra.");
        }
    }


    private void generarCompraCliente(Long idCliente) {

        // Se busca el cliente mediante su Id
        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Se obtiene el carritoCompra del cliente.
        CarritoCompra carritoCompra = cliente.getCarritoCompra();


        if (carritoCompra == null || carritoCompra.getItemsCarrito().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar la compra.");
        }

        // Procesar cada item del lote
        for (ItemsCarrito item : carritoCompra.getItemsCarrito()) {

            Producto producto = item.getProducto();
            int cantidadSolicitada = item.getCantidad();

            // Seleccionamos los lotes del producto
            List<Lote> lotesProducto;

            if (producto.getTipoProducto().name().equalsIgnoreCase("PERECEDERO")) {
                // Si el producto es perecedero, se ordena por fecha de vencimiento
                lotesProducto = loteRepo.findByEspacioProducto_Producto_IdOrderByFechaVencimientoAsc(producto.getId());
            } else {
                // Si no es perecedero, se ordena por fecha de ingreso
                lotesProducto = loteRepo.findByEspacioProducto_Producto_IdOrderByFechaIngresoAsc(producto.getId());
            }

            if (lotesProducto.isEmpty()) {
                throw new RuntimeException("No existen lotes disponibles para el producto: " + producto.getNombre());
            }

            int cantidadRestante = cantidadSolicitada;

            // Recorremos los lotes disponibles hasta cumplir la cantidad requerida
            for (Lote lote : lotesProducto) {
                if (cantidadRestante == 0) break;
                if (lote.getEstadoLote().name().equalsIgnoreCase("AGOTADO") ||
                        lote.getEstadoLote().name().equalsIgnoreCase("CADUCADO") ||
                        lote.getCantidadDisponible() == 0) {
                    continue; // Saltar lotes sin stock o vencidos
                }

                int cantidadDisponible = lote.getCantidadDisponible();
                int cantidadADescontar = Math.min(cantidadDisponible, cantidadRestante);

                // Descontar del lote y actualizar cantidades
                try {
                    lote.retirarCantidad(cantidadADescontar);
                    loteRepo.save(lote);
                } catch (Exception e) {
                    throw new RuntimeException("Error al descontar del lote " + lote.getCodigoLote() + ": " + e.getMessage());
                }

                cantidadRestante -= cantidadADescontar;
            }

            // Si aún quedan unidades sin asignar, significa que no hay stock suficiente
            if (cantidadRestante > 0) {
                throw new RuntimeException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
        }

        // Si se procesan todos los items correctamente, se genera la factura
        generarFactura(cliente, carritoCompra);

        // Limpiar el carrito luego de generar la factura
        carritoCompra.getItemsCarrito().clear();
        carritoCompra.setTotalValor(0.0);
    }

    private void generarFactura(Cliente cliente, CarritoCompra carritoCompra) {

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setEstadoCompra(EstadoCompra.PAGADA);
        compra.setFechaCompra(LocalDate.now());
        compra.setFechaEntrega(LocalDate.now().plusDays(3));

        List<DetalleCompra> detalles = carritoCompra.getItemsCarrito().stream()
                .map(item -> {
                    DetalleCompra detalle = new DetalleCompra();
                    detalle.setProducto(item.getProducto());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setCompra(compra);

                    // Calcula manualmente el valor del detalle antes de persistir
                    detalle.setValorDetalle(item.getProducto().getValorVenta() * item.getCantidad());
                    return detalle;
                })
                .toList();

        compra.setDetalleCompra(detalles);

        // Calcula totales con los valores ya definidos
        compra.calcularTotales();

        // Guarda la compra y sus detalles en cascada
        compraRepo.save(compra);

        // Genera la factura asociada
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setCompra(compra);
        factura.setFechaCompra(LocalDate.now());
        facturaRepo.save(factura);

        // Envía la factura por correo
        emailService.enviarFacturaEmail(cliente, factura);
    }

}

