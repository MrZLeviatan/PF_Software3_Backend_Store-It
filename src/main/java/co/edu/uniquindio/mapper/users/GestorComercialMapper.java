package co.edu.uniquindio.mapper.users;


import co.edu.uniquindio.dto.users.gestroComercial.GestorComercialDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoIngresoMapper;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoRetiroMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.SolicitudMapper;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        PersonaMapper.class,
        SolicitudMapper.class,
        MovimientoRetiroMapper.class,
        MovimientoIngresoMapper.class
})
public interface GestorComercialMapper {

    @Mapping(source = ".", target = "persona")
    @Mapping(target = "datosLaborales.idBodega", source = "datosLaborales.bodega.id")
    GestorComercialDto toDto(GestorComercial gestorComercial);
}
