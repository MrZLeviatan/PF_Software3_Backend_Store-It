package co.edu.uniquindio.controller.objects.inventario;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.inventario.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lote")
@RequiredArgsConstructor
public class LoteController {


    private final LoteService loteService;

    @GetMapping("/{idLote}")
    public ResponseEntity<MensajeDto<LoteDto>> obtenerLoteId(@PathVariable Long idLote)
            throws ElementoNoEncontradoException {
        LoteDto loteDto = loteService.obtenerLoteDtoId(idLote);
        return ResponseEntity.ok().body(new MensajeDto<>(false, loteDto));
    }


    @GetMapping("/espacio-producto/{idEspacio}/lotes")
    public ResponseEntity<MensajeDto<List<LoteDto>>> obtenerLotesEspacio(@PathVariable Long idEspacio)
            throws ElementoNoEncontradoException {

        List<LoteDto> lotes = loteService.obtenerLotesEspacio(idEspacio);
        return ResponseEntity.ok().body(new MensajeDto<>(false,lotes));
    }

}
