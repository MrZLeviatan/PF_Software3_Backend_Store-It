package co.edu.uniquindio.models.entities.objects.solicitudes.incidentes;

import co.edu.uniquindio.models.entities.objects.solicitudes.Resolucion;
import co.edu.uniquindio.models.entities.users.PersonalBodega;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.models.enums.entities.TipoIncidente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Se crean tablas hijas relacionadas a la tabla madre (esta)
@Comment("Clase base que representa un incidente genérico de un loté o movimientos")
public abstract class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único del Incidente ")
    private Long id;

    @Column(name = "descripcion",nullable = false)
    @Comment("Descripción del incidente")
    private String descripcion;

    @Column(name = "fecha_registro",nullable = false)
    @Comment("Fecha del registro del incidente")
    private LocalDate fechaRegistro;

    @Column(name = "is_perdidas?",nullable = false)
    @Comment("Se verifica si hubieron perdidas")
    private boolean isPerdidas;

    @Column(name = "cantidad_afectada")
    @Comment("Cantidad afectada por el incidente (Opcional,Variable)")
    private int cantidadAfectada;

    @Column(name = "valor_perdida")
    @Comment("Valor de la perdida si hubo")
    private Double valorPerdidaCantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_proceso")
    @Comment("Estado del proceso del incidente")
    private EstadoProceso estadoProceso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidente")
    @Comment("Tipo de Incidente")
    private TipoIncidente tipoIncidente;


    // ----- RELACIONES ----------

    @ManyToOne
    @JoinColumn(name = "personal_bodega_id")
    @Comment("Personal Bodega que registra el reporte")
    private PersonalBodega reportadoPor;


    @OneToOne (mappedBy = "incidente")
    @Comment("Resolución del Incidente")
    private Resolucion resolucion;

}
