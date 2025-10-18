package co.edu.uniquindio.models.entities.objects.inventario.movimiento;


import co.edu.uniquindio.models.entities.users.PersonalBodega;
import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;
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
@Table(name = "participacion_movimiento")
@Comment("Entidad intermedia que representa la participación de un PersonalBodega en un Movimiento")

public class ParticipacionMovimiento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único de la participación.")
    private Long id;

    // Relación con Movimiento (muchos pueden apuntar al mismo movimiento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimiento_id", nullable = false)
    @Comment("Movimiento en el que participa el PersonalBodega")
    private Movimiento movimiento;

    // Relación con PersonalBodega (muchas participaciones pueden pertenecer a un mismo empleado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_bodega_id", nullable = false)
    @Comment("Personal de Bodega que participa en el Movimiento")
    private PersonalBodega personalBodega;

    // Rol específico dentro del movimiento (Gestor, Auxiliar, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_participacion", nullable = false)
    @Comment("Rol que desempeñó el PersonalBodega en el movimiento")
    private TipoPersonalBodega rolParticipacion;



}
