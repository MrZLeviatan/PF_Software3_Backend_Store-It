package co.edu.uniquindio.service.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;

public interface MovimientoService {

    void registroMovimientoIngreso(Lote lote, RegistroLoteDto registroLoteDto)
            throws ElementoNoEncontradoException;

}
