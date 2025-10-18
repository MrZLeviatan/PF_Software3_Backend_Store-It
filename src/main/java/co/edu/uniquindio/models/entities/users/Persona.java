package co.edu.uniquindio.models.entities.users;

import co.edu.uniquindio.models.embeddable.User;
import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la persona creado automáticamente por el sistema.")
    private Long id;

    @Column(name = "nombre_completo", nullable = false) // El nombre no puede ser nulo.
    @Comment("Nombre completo del usuario (nombre y apellidos). Si es empresa, nombre de la empresa.")
    private String nombre;

    @Column(name = "teléfono", nullable = false)    // El teléfono principal no puede ser nulo.
    @Comment("Número telefónico principal de contacto.")
    private String telefono;

    @Column(name = "teléfono_secundario")   // El teléfono secundario puede ser nulo -> vacío.
    @Comment("Número telefónico principal de contacto (Opcional).")
    private String telefonoSecundario;

    @Embedded // Componente que se adhiere a la clase
    private User user;

    // Cada persona (cliente, gestor, etc.) tiene su lista de notificaciones
    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones;

}
