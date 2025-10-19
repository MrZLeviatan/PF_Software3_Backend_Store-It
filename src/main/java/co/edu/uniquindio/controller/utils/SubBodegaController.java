package co.edu.uniquindio.controller.utils;


import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.almacen.subBodega.SubBodegaDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.almacen.SubBodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sub-bodega")
@RequiredArgsConstructor
public class SubBodegaController {


    private final SubBodegaService subBodegaService;

    @GetMapping("/{idSubBodega}")
    public ResponseEntity<MensajeDto<SubBodegaDto>> obtenerSubBodegaPorId(@PathVariable Long idSubBodega)
            throws ElementoNoEncontradoException {
        SubBodegaDto subBodegaDto = subBodegaService.obtenerSubBodegaDto(idSubBodega);
        return ResponseEntity.ok().body(new MensajeDto<>(false, subBodegaDto));
    }

    @GetMapping("/listar")
    public ResponseEntity<MensajeDto<List<SubBodegaDto>>> obtenerSubBodegas() {
        List<SubBodegaDto> subBodegas = subBodegaService.listarSubBodegas();
        return ResponseEntity.ok().body(new MensajeDto<>(false,subBodegas));
    }


}
