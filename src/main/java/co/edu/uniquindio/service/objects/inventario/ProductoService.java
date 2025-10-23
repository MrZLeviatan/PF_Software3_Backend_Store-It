package co.edu.uniquindio.service.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.enums.entities.TipoProducto;

import java.util.List;

public interface ProductoService {


    // Proceso del Gestor comercial para registrar un producto.
    ProductoDto registroProducto(RegistroProductoDto registroProducto)
            throws ElementoRepetidoException, ElementoNoEncontradoException,
            ElementoNoValidoException, ElementoNulosException;

    // Método para obtener productos mediante su Id
    Producto obtenerProducto(Long id)
            throws ElementoNoEncontradoException;

    // Método para obtener y mapear el producto a productoDto mediante su ID
    ProductoDto obtenerProductoDto(Long idProducto)
            throws ElementoNoEncontradoException;

    // Se lista todos los productos, se mapean a productoDto,
    List<ProductoDto> listarProductoDto();

    // Filtrar los productos mediante su TipoProducto o Id Proveedor
    List<ProductoDto> listarProductosFiltro(
            TipoProducto tipoProducto, String idProveedor,
            int pagina, int size);


}
