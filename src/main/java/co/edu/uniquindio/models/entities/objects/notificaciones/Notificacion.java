package co.edu.uniquindio.models.entities.objects.notificaciones;

import co.edu.uniquindio.models.entities.users.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notificaciones")
@Comment("Entidad que representa las notificaciones enviadas a los usuarios del sistema.")
public class Notificacion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la Notificación")
    private Long id;

    @Column(nullable = false)
    @Comment("Título breve de la notificación")
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("Mensaje o cuerpo de la notificación")
    private String descripcion;

    @Column(name = "fecha_envio", nullable = false)
    @Comment("Fecha en la que se generó la notificación")
    private LocalDateTime fechaEnvio;

    @Column(name = "es_leida", nullable = false)
    @Comment("Indica si la notificación fue leída")
    private boolean esLeida = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id")
    private Persona receptor;

    // Lista de referencias a entidades asociadas
    @OneToMany(mappedBy = "notificacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Lista de entidades asociadas a esta notificación (solicitudes, documentos, etc.)")
    private List<ReferenciaEntidad> entidadesAsociadas;


    // Método para marcar la notificación como leída
    public void marcarComoLeida() {
        this.esLeida = true;
    }

    // Método para agregar una entidad relacionada a la notificación
    public <T> void agregarEntidadAsociada(T entidad, Long entidadId) {
        ReferenciaEntidad ref = new ReferenciaEntidad();
        ref.setTipoEntidad(entidad.getClass().getSimpleName());
        ref.setEntidadId(entidadId);
        ref.setNotificacion(this);
        entidadesAsociadas.add(ref);
    }

}
