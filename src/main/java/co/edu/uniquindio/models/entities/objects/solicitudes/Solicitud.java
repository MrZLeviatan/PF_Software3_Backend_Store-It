package co.edu.uniquindio.models.entities.objects.solicitudes;

import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.models.enums.entities.TipoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud")
@Comment("Entidad que representan las Solicitudes del espacio.")
public class Solicitud {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la Solicitud")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitud", nullable = false)
    @Comment("Tipo de solicitud")
    private TipoSolicitud tipoSolicitud;

    @Column(name = "descripcion", nullable = false)
    @Comment("Descripción de la solicitud")
    private String descripcion;

    @Column(name = "fecha_solicitud", nullable = false)
    @Comment("Fecha de registro de la Solicitud")
    private LocalDate fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_proceso", nullable = false)
    @Comment("Estado de la Solicitud")
    private EstadoProceso estadoSolicitud;

    @Column(name = "espacio_solicitado", nullable = false)
    @Comment("El espacio solicitado")
    private Double espacioSolicitado;

    @ManyToOne
    @JoinColumn(name = "gestor_comercial_id", nullable = false)
    @Comment("Gestor comercial que hace la solicitud")
    private GestorComercial gestorComercial;

    @ManyToOne
    @JoinColumn(name = "espacio_producto_id")
    @Comment("Espacio al que se le hace la solicitud")
    private EspacioProducto espacioProducto;

    @OneToOne(mappedBy = "solicitud")
    @Comment("Resolución de la Solicitud")
    private Resolucion resolucion;


}
