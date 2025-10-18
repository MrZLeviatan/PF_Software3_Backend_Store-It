package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.adminBodega.AdminBodegaDto;
import co.edu.uniquindio.mapper.objects.solicitudes.ResolucionMapper;
import co.edu.uniquindio.models.entities.users.AdminBodega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {
        PersonaMapper.class,
        ResolucionMapper.class
})
public interface AdminBodegaMapper {


    @Mapping(source = ".", target = "persona")
    AdminBodegaDto toDto(AdminBodega adminBodega);

}


