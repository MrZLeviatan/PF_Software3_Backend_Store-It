package co.edu.uniquindio.dto.common.datosLaborales;

import java.time.LocalDate;

public record DatosLaboralesDto(

      Long id,
      LocalDate fechaInicioContrato,
      double sueldo,
      Long idBodega

) {
}
