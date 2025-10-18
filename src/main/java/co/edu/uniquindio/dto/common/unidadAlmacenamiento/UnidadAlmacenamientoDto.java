package co.edu.uniquindio.dto.common.unidadAlmacenamiento;

import co.edu.uniquindio.models.enums.embeddable.EstadoUnidad;

public record UnidadAlmacenamientoDto(

        Double largo,
        Double ancho,
        Double alto,
        Double volumenOcupado,
        EstadoUnidad estadoUnidad,
        Double volumenTotal
) {
}
