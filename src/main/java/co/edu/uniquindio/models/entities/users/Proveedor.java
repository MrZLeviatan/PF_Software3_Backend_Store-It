package co.edu.uniquindio.models.entities.users;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
public class Proveedor extends Persona {


    // Bodegas que se encuentran en la Sede.
    @OneToMany(mappedBy = "proveedor")
    @Comment("Productos que provee el Proveedor.")
    private List<Producto> productos;

    @OneToMany(mappedBy = "proveedor")
    @Comment("Lista de documentos asociados al Proveedor")
    private List<DocumentoIngreso> documentoIngresos;

}