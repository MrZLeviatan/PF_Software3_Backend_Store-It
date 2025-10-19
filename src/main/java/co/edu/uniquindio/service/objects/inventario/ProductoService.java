package co.edu.uniquindio.service.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;

import java.util.List;

public interface ProductoService {


    void registroProducto(RegistroProductoDto registroProducto)
            throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoNoValidoException, ElementoNulosException;

    Producto obtenerProducto(Long id) throws ElementoNoEncontradoException;

    ProductoDto obtenerProductoDto(Long idProducto) throws ElementoNoEncontradoException;

    List<ProductoDto> listarProductoDto();

}
