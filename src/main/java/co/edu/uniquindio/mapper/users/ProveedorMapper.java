package co.edu.uniquindio.mapper.users;


import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.mapper.objects.inventario.ProductoMapper;
import co.edu.uniquindio.mapper.objects.inventario.documento.DocumentoIngresoMapper;
import co.edu.uniquindio.models.entities.users.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ProductoMapper.class,
        DocumentoIngresoMapper.class,
})
public interface ProveedorMapper {

    ProveedorDto toDto(Proveedor proveedor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "documentoIngresos", expression = "java(new java.util.ArrayList<>())")
    Proveedor toEntity(RegistroProveedorDto registroProveedorDto);

}
