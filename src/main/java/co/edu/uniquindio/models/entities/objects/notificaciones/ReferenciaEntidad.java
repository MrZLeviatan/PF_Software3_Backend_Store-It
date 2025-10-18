package co.edu.uniquindio.models.entities.objects.notificaciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "referencias_entidades")
@Comment("Tabla que almacena las referencias genéricas a las entidades asociadas a una notificación.")
public class ReferenciaEntidad {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID único de la referencia")
    private Long id;

    @Column(name = "tipo_entidad", nullable = false)
    @Comment("Tipo o clase de la entidad referenciada (por ejemplo: Solicitud, Documento)")
    private String tipoEntidad;

    @Column(name = "entidad_id", nullable = false)
    @Comment("ID de la entidad referenciada en su tabla correspondiente")
    private Long entidadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacion_id", nullable = false)
    @Comment("Notificación a la que pertenece esta referencia")
    private Notificacion notificacion;

}

