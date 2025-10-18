package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.PersonaDto;
import co.edu.uniquindio.models.entities.users.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    // Convierte la clase base en su DTO
    PersonaDto toDto(Persona persona);



}
