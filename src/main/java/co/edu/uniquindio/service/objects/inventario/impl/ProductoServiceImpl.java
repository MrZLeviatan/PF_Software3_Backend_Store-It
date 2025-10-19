package co.edu.uniquindio.service.objects.inventario.impl;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.mapper.objects.inventario.ProductoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.service.objects.inventario.ProductoService;
import co.edu.uniquindio.service.users.ProveedorService;
import co.edu.uniquindio.service.utils.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {


    // Servicios Propios
    private final ProveedorService proveedorService;
    private final CloudinaryService cloudinaryService;

    private final ProductoRepo productoRepo;
    private final ProductoMapper productoMapper;


    @Override
    public void registroProducto(RegistroProductoDto registroProducto)
            throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoNoValidoException, ElementoNulosException {

        // 1. Validamos si el código de barras no está repetido
        if (productoRepo.existsByCodigoBarras(registroProducto.codigoBarras())){
            throw new ElementoRepetidoException("Código de barras ya registrado en otro producto");
        }

        // 2. Se mapea el producto registrado
        Producto producto = productoMapper.toEntity(registroProducto);

        // 3. Subimos imagen de perfil o lanzamos excepción si no se envía
        String urlImagen;
        if (registroProducto.imagenProducto() != null && !registroProducto.imagenProducto().isEmpty()) {
            urlImagen = cloudinaryService.uploadImage(registroProducto.imagenProducto());
        } else {
            throw new ElementoNulosException("No se proporcionó ninguna imagen para el producto");
        }
        producto.setImagen(urlImagen);

        // 4. Se le asigna el Proveedor
        Proveedor proveedor = proveedorService.encontrarProveedor(registroProducto.idProveedor());
        producto.setProveedor(proveedor);
        proveedor.agregarProducto(producto);

        // Guardamos el producto
        productoRepo.save(producto);
    }


    @Override
    public Producto obtenerProducto(Long id)
            throws ElementoNoEncontradoException {
        return productoRepo.findById(id)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("Producto no encontrado"));
    }

    @Override
    public ProductoDto obtenerProductoDto(Long idProducto)
            throws ElementoNoEncontradoException {
        return productoMapper.toDto(obtenerProducto(idProducto));
    }


    @Override
    public List<ProductoDto> listarProductoDto() {
        return productoRepo.findAll()
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }


}
