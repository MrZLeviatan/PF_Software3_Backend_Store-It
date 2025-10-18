package co.edu.uniquindio.mapper.objects.inventario.documento;

import co.edu.uniquindio.dto.objects.inventario.documento.documentoIngreso.DocumentoIngresoDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoIngresoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MovimientoIngresoMapper.class,
})
public interface DocumentoIngresoMapper {


    @Mapping(target = "idGestorComercial", source = "gestorComercial.id")
    @Mapping(target = "idProveedor", source = "proveedor.id")
    @Mapping(target = "idLote", source = "lote.id")
    DocumentoIngresoDto toDto(DocumentoIngreso documentoIngreso);



}
