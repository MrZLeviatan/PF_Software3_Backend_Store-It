package co.edu.uniquindio.service.objects.inventario.impl;

import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.inventario.documento.DocumentoIngresoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.repository.objects.inventario.documento.DocumentoIngresoRepo;
import co.edu.uniquindio.repository.objects.inventario.movimiento.MovimientoIngresoRepo;
import co.edu.uniquindio.service.objects.inventario.DocumentoService;
import co.edu.uniquindio.service.objects.notificacion.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoIngresoRepo documentoIngresoRepo;
    private final DocumentoIngresoMapper documentoIngresoMapper;

    private final MovimientoIngresoRepo movimientoIngresoRepo;
    private final NotificacionService notificacionService;


    @Override
    public void registrarDocumentoIngreso(Lote lote, RegistroLoteDto registroLoteDto, MovimientoIngreso movimientoIngreso)
            throws ElementoNoEncontradoException {

        DocumentoIngreso documentoIngreso = new DocumentoIngreso();
        documentoIngreso.setCodigoIngreso(generacionCodigo());
        documentoIngreso.setAreaTotal(lote.getAreaTotal());
        documentoIngreso.setFechaEntrega(registroLoteDto.fechaEntra());
        documentoIngreso.setCantidad(lote.getCantidadDisponible());
        documentoIngreso.setEstadoProceso(EstadoProceso.PENDIENTE);
        documentoIngreso.setGestorComercial(movimientoIngreso.getGestorComercial());

        documentoIngreso.setProveedor(lote.getEspacioProducto().getProducto().getProveedor());
        lote.getEspacioProducto().getProducto().getProveedor().agregarDocumentoIngreso(documentoIngreso);

        documentoIngreso.setLote(lote);

        documentoIngreso.setMovimientoIngreso(movimientoIngreso);
        movimientoIngreso.setDocumentoIngreso(documentoIngreso);

        // Se guarda en la base de datos
        documentoIngresoRepo.save(documentoIngreso);
        movimientoIngresoRepo.save(movimientoIngreso);

        // Se notifica sobre el primer paso del ingreso de lote
        notificacionService.crearNotificacionMovimientoIngreso(documentoIngreso,movimientoIngreso);

    }


    private String generacionCodigo() {
        return UUID.randomUUID().toString()
                .replace("-", "")  // Eliminamos guiones para mayor limpieza
                .substring(0, 5)   // Reducimos a 6 caracteres
                .toUpperCase();
    }
}
