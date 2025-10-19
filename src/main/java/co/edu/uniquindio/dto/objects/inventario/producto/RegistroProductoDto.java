package co.edu.uniquindio.dto.objects.inventario.producto;

import co.edu.uniquindio.models.enums.entities.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record RegistroProductoDto(


        @NotBlank @Length(max = 100) String codigoBarras,
        @NotBlank @Length(max = 100)String nombre,
        @NotNull Double valorCompra,
        @NotNull TipoProducto tipoProducto,
        @NotNull Long idProveedor,
        // Archivo de imagen del producto, opcional.
        MultipartFile imagenProducto


) {
}
