package co.edu.uniquindio.service.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.subBodega.SubBodegaDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.almacen.SubBodega;

import java.util.List;

public interface SubBodegaService {


    SubBodega obtenerSubBodega(Long id)
            throws ElementoNoEncontradoException;

    SubBodegaDto obtenerSubBodegaDto(Long idSubBodega)
            throws ElementoNoEncontradoException;

    List<SubBodegaDto> listarSubBodegas();

}
