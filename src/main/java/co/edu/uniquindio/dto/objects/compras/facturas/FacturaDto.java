package co.edu.uniquindio.dto.objects.compras.facturas;

import java.time.LocalDate;

public record FacturaDto(

        Long id,
        Long idCompra,
        Long idCliente,
        Double subTotal,
        Double totalPaga,
        Double iva,
        LocalDate fechaCompra,
        Long idSede

) {
}
