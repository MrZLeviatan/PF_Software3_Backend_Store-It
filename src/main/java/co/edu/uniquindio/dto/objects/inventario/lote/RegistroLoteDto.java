package co.edu.uniquindio.dto.objects.inventario.lote;

import java.time.LocalDate;

public record RegistroLoteDto(

        // (Opcional)
        LocalDate fechaVencimiento,
        int cantidadTotal,
        Double areaTotal,
        Long idEspacioProducto,


        Long idGestorComercial,
        LocalDate fechaEntra,
        String observaciones

) {
}
