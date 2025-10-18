package co.edu.uniquindio.dto.objects.inventario.documento.documentoRetiro;

import co.edu.uniquindio.models.enums.entities.EstadoProceso;

import java.time.LocalDate;

public record DocumentoRetiroDto(

   Long id,
   String codigoRetiro,
   LocalDate fechaEntrega,
   EstadoProceso estadoProceso,

   Long idCompra


) {
}
