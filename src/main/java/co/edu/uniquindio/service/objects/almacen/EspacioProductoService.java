package co.edu.uniquindio.service.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;

public interface EspacioProductoService {


    void registroEspacioProducto(RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException;

    EspacioProducto obtenerEspacioProductoId(Long idEspacioProducto)
            throws ElementoNoEncontradoException;

    EspacioProductoDto obtenerEspacioProductoDto(Long idEspacioProducto)
            throws ElementoNoEncontradoException;

    // Se busca el espacio de un producto mediante Id dell producto, posteriormente se mapea el espacio
    EspacioProductoDto obtenerEspacioDelProducto(Long idProducto)
            throws ElementoNoEncontradoException;



}
