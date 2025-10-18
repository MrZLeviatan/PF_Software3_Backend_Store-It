package co.edu.uniquindio.dto.objects.compras.compra;

import co.edu.uniquindio.dto.objects.compras.detalleCompra.DetalleCompraDto;
import co.edu.uniquindio.dto.objects.compras.facturas.FacturaDto;
import co.edu.uniquindio.models.enums.entities.EstadoCompra;

import java.time.LocalDate;
import java.util.List;

public record CompraDto(

       Long id,
       Long idCliente,
       Double subTotal,
       Double iva,
       LocalDate fechaCompra,
       LocalDate fechaEntrega,
       EstadoCompra estadoCompra,
       List<DetalleCompraDto> detalleCompra,
       FacturaDto factura

) {
}
