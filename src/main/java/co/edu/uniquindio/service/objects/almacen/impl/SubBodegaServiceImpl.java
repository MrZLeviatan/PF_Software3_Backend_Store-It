package co.edu.uniquindio.service.objects.almacen.impl;


import co.edu.uniquindio.dto.objects.almacen.subBodega.SubBodegaDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.almacen.SubBodegaMapper;
import co.edu.uniquindio.models.entities.objects.almacen.SubBodega;
import co.edu.uniquindio.repository.objects.almacen.SubBodegaRepo;
import co.edu.uniquindio.service.objects.almacen.SubBodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubBodegaServiceImpl implements SubBodegaService {


    private final SubBodegaMapper subBodegaMapper;
    private final SubBodegaRepo subBodegaRepo;



    @Override
    public SubBodega obtenerSubBodega(Long id)
            throws ElementoNoEncontradoException {
        return subBodegaRepo.findById(id)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("SubBodega no encontrada"));
    }


    @Override
    public SubBodegaDto obtenerSubBodegaDto(Long idSubBodega)
            throws ElementoNoEncontradoException {
        return subBodegaMapper.toDto(obtenerSubBodega(idSubBodega));
    }


    @Override
    public List<SubBodegaDto> listarSubBodegas() {
        return subBodegaRepo.findAll()
                .stream()
                .map(subBodegaMapper::toDto)
                .toList();
    }


}
