package co.edu.uniquindio.service.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;

import java.util.List;

public interface LoteService {


    void registroLote(RegistroLoteDto registroLoteDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException;

    Lote obtenerLote(Long idLote) throws ElementoNoEncontradoException;

    LoteDto obtenerLoteDtoId(Long idLote)
            throws ElementoNoEncontradoException;

    List<LoteDto> obtenerLotesEspacio(Long idEspacio)
            throws ElementoNoEncontradoException;
}
