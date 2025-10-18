package co.edu.uniquindio.models.entities.objects.solicitudes;

import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.Incidente;
import co.edu.uniquindio.models.entities.users.AdminBodega;
import co.edu.uniquindio.models.enums.entities.TipoResolucion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resolucion")
@Comment("Entidad que representan las Resoluciones de los Incidentes y Solicitudes.")
public class Resolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la Resolución ")
    private Long id;

    @Column(name = "observaciones")
    @Comment("Observaciones relacionadas a la resolución")
    private String observaciones;

    @Column(name = "fecha_hora_registro")
    @Comment("Recha y Hora del registro de la resolución")
    private LocalDateTime fechayHoraRegistro;

    @ManyToOne
    @JoinColumn(name = "admin_bodega_id",nullable = false)
    @Comment("Resolutor del problema / solicitud")
    private AdminBodega resolutor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_resolucion",nullable = false)
    @Comment("Tipo de Resolución dada")
    private TipoResolucion tipoResolucion;


    // ------- Relaciones ---------------

    @OneToOne
    @JoinColumn(name = "incidente_id")
    @Comment("Incidente al que le da la resolución (Dependiendo)")
    private Incidente incidente;

    @OneToOne
    @JoinColumn(name = "solicitud_id")
    @Comment("Solicitud al que se le da la resolución (Dependiendo)")
    private Solicitud solicitud;

}
