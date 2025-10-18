package co.edu.uniquindio.models.entities.objects.almacen;

import co.edu.uniquindio.models.embeddable.Ubicacion;
import jakarta.persistence.*;
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
@Table(name = "sedes")
@Comment("Entidad que representan las Sedes de la empresa")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la Sede")
    private Long id;

    @Column(name = "nombre_sede", nullable = false) // El nombre no puede ser nulo.
    @Comment("Nombre completo de la Sede.")
    private String nombre;

    @Column(name = "dirección", nullable = false) // La dirección de la Sede
    @Comment("Dirección de la Sede.")
    private String direccion;

    @Column(name = "teléfono", nullable = false)    // El teléfono principal no puede ser nulo.
    @Comment("Número telefónico principal de contacto.")
    private String telefono;

    @Column(name = "email_sede", nullable = false) // El email no puede ser nulo.
    @Comment("Email de la Sede.")
    private String email;

    @Embedded   // Ubicación de la Sede
    private Ubicacion ubicacion;

    // Bodegas que se encuentran en la Sede.
    @OneToMany(mappedBy = "sede")
    @Comment("Bodegas relacionadas a la Sede.")
    private List<Bodega> bodegas;


}
