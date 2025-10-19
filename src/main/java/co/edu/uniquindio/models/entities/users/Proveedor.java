package co.edu.uniquindio.models.entities.users;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "proveedor")
@Comment("Entidad que representa a los proveedores de los productos")
public class Proveedor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de los proveedores")
    private Long id;

    @Column(name = "nombre_completo", nullable = false) // El nombre no puede ser nulo.
    @Comment("Nombre completo del usuario (nombre y apellidos). Si es empresa, nombre de la empresa.")
    private String nombre;

    @Email
    @Column(name = "email", nullable = false) // El nombre no puede ser nulo.
    @Comment("Email asociado al proveedor.")
    private String email;

    @Column(name = "teléfono", nullable = false)    // El teléfono principal no puede ser nulo.
    @Comment("Número telefónico principal de contacto.")
    private String telefono;

    @Column(name = "marca", nullable = false) // El nombre no puede ser nulo.
    @Comment("Marca relacionada al proveedor.")
    private String marca;

    // Bodegas que se encuentran en la Sede.
    @OneToMany(mappedBy = "proveedor")
    @Comment("Productos que provee el Proveedor.")
    private List<Producto> productos;

    @OneToMany(mappedBy = "proveedor")
    @Comment("Lista de documentos asociados al Proveedor")
    private List<DocumentoIngreso> documentoIngresos;


    public void agregarProducto(Producto producto){
        productos.add(producto);
        producto.setProveedor(this);
    }


    public void agregarDocumentoIngreso(DocumentoIngreso documentoIngreso){
        documentoIngresos.add(documentoIngreso);
        documentoIngreso.setProveedor(this);
    }


}
