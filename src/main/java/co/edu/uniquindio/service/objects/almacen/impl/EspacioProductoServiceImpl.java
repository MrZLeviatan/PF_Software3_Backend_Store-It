package co.edu.uniquindio.service.objects.almacen.impl;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.mapper.objects.almacen.EspacioProductoMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.almacen.SubBodega;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.repository.objects.almacen.EspacioProductoRepo;
import co.edu.uniquindio.service.objects.almacen.EspacioProductoService;
import co.edu.uniquindio.service.objects.almacen.SubBodegaService;
import co.edu.uniquindio.service.objects.inventario.ProductoService;
import co.edu.uniquindio.service.objects.solicitudes.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EspacioProductoServiceImpl implements EspacioProductoService {


    // Servicios Propios
    private final SubBodegaService subBodegaService;
    private final ProductoService productoService;
    private final SolicitudService solicitudService;

    private final EspacioProductoMapper espacioProductoMapper;
    private final EspacioProductoRepo espacioProductoRepo;


    @Override
    public void registroEspacioProducto(RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        // Mapear el espacio
        EspacioProducto espacioProducto = espacioProductoMapper.toEntity(registroEspacioProductoDto);

        // Asignamos la subBodega y el producto
        SubBodega subBodega = subBodegaService.obtenerSubBodega(registroEspacioProductoDto.idSubBodega());
        Producto producto = productoService.obtenerProducto(registroEspacioProductoDto.idProducto());

        // Asignar relaciones
        espacioProducto.setSubBodega(subBodega);
        espacioProducto.setProducto(producto);
        producto.setEspacioProducto(espacioProducto); // sincroniza la relación inversa

        // Válida y agrega el espacio a la subBodega
        subBodega.agregarEspacio(espacioProducto);

        // Se guarda el espacioProducto
        espacioProductoRepo.save(espacioProducto);

        // Se genera automáticamente una solicitud para abrir el Espacio
        solicitudService.registrarSolicitudEspacio(espacioProducto,registroEspacioProductoDto);

    }


    @Override
    public EspacioProducto obtenerEspacioProductoId(Long idEspacioProducto) throws ElementoNoEncontradoException {
        return espacioProductoRepo.findById(idEspacioProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("EspacioProducto no encontrado"));
    }

    @Override
    public EspacioProductoDto obtenerEspacioProductoDto(Long idEspacioProducto) throws ElementoNoEncontradoException {
        return espacioProductoMapper.toDto(obtenerEspacioProductoId(idEspacioProducto));
    }


    public EspacioProducto obtenerEspacioDelProductoId(Long idProducto) throws ElementoNoEncontradoException {
        return espacioProductoRepo.findByProducto_Id(idProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("EspacioProducto no encontrado"));
    }


    @Override
    public EspacioProductoDto obtenerEspacioDelProducto(Long idProducto)
            throws ElementoNoEncontradoException {
        return espacioProductoMapper.toDto(obtenerEspacioProductoId(idProducto));
    }





}
