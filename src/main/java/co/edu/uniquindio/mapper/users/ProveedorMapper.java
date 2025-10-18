package co.edu.uniquindio.mapper.users;


import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.mapper.objects.inventario.ProductoMapper;
import co.edu.uniquindio.mapper.objects.inventario.documento.DocumentoIngresoMapper;
import co.edu.uniquindio.models.entities.users.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        PersonaMapper.class,
        ProductoMapper.class,
        DocumentoIngresoMapper.class,
})
public interface ProveedorMapper {


    @Mapping(source = ".", target = "persona")
    ProveedorDto toDto(Proveedor proveedor);
}
