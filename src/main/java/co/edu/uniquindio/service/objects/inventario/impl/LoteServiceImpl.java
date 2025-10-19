package co.edu.uniquindio.service.objects.inventario.impl;

import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.mapper.objects.inventario.LoteMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.inventario.LoteRepo;
import co.edu.uniquindio.service.objects.almacen.EspacioProductoService;
import co.edu.uniquindio.service.objects.inventario.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LoteServiceImpl implements LoteService {

    private final LoteRepo loteRepo;
    private final LoteMapper loteMapper;
    private final EspacioProductoService espacioProductoService;


    @Override
    public void registroLote(RegistroLoteDto registroLoteDto) throws ElementoNoEncontradoException, ElementoNoValidoException {

        // 1. Se mapea el lote
        Lote lote = loteMapper.toEntity(registroLoteDto);

        // 2. Validamos el espacio para agregar el lote
        EspacioProducto espacioProducto = espacioProductoService.obtenerEspacioProductoId(registroLoteDto.idEspacioProducto());
        espacioProducto.agregarLote(lote);

        // 3. Completamos los campos del lote
        lote.setCodigoLote(asignarCodigoLote(lote));

        // Validamos si es perecedero y si tiene la fecha de vencimiento
        if (espacioProducto.getProducto().getTipoProducto().equals(TipoProducto.PERECEDEROS)
                && registroLoteDto.fechaVencimiento() == null){
            throw new ElementoNoValidoException("Los lotes perecederos deben tener fecha de vencimiento");
        }

        // 4. Guardamos en la BD
        loteRepo.save(lote);
    }



    private String asignarCodigoLote(Lote lote){
        if (lote == null || lote.getEspacioProducto() == null) return null;

        // Determinamos el código del producto asociado
        String codigoProducto = lote.getEspacioProducto().getProducto().getCodigoBarras();

        // Determinamos el número de lote
        int numeroLote = lote.getEspacioProducto().getLotes().size() +1;

        // Formatear el número de lote con 3 dígitos (001, 002, ...)
        String numeroFormateado = String.format("%03d", numeroLote);

        // Asignar el código final
        return codigoProducto + "-" + numeroFormateado;
    }


    @Override
    public Lote obtenerLote(Long idLote)
            throws ElementoNoEncontradoException {

        return loteRepo.findById(idLote)
                .orElseThrow(() -> new ElementoNoEncontradoException("Lote no encontrado"));
    }

    @Override
    public LoteDto obtenerLoteDtoId(Long idLote) throws ElementoNoEncontradoException {
        return loteMapper.toDto(obtenerLote(idLote));
    }


    @Override
    public List<LoteDto> obtenerLotesEspacio(Long idEspacio) throws ElementoNoEncontradoException {
        EspacioProducto espacioProducto = espacioProductoService.obtenerEspacioProductoId(idEspacio);

        return espacioProducto.getLotes().stream()
                .map(loteMapper::toDto)
                .toList();
    }


}
