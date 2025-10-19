package co.edu.uniquindio.service.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;

public interface DocumentoService {


    void registrarDocumentoIngreso(Lote lote, RegistroLoteDto registroLoteDto, MovimientoIngreso movimientoIngreso)
            throws ElementoNoEncontradoException;
}
