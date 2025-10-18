package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.mapper.objects.compra.CarritoCompraMapper;
import co.edu.uniquindio.mapper.objects.compra.CompraMapper;
import co.edu.uniquindio.mapper.objects.compra.FacturaMapper;
import co.edu.uniquindio.models.entities.users.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        PersonaMapper.class,
                FacturaMapper.class,
                CompraMapper.class,
                CarritoCompraMapper.class
        })
public interface ClienteMapper {


    @Mapping(source = ".", target = "persona")
    ClienteDto toDto(Cliente cliente);


}
