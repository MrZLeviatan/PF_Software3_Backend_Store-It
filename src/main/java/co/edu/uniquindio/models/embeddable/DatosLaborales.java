package co.edu.uniquindio.models.embeddable;

import co.edu.uniquindio.models.entities.objects.almacen.Bodega;
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
@Table(name = "datos_laborales")
@Comment("Entidad que representa los datos laborales de cualquier empleado de la empresa.")
public class DatosLaborales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio_contrato", nullable = false)   // Fecha de registro de la cuenta ( Funciones futuras )
    @Comment("Fecha de inicio de contratación.")
    private LocalDate fechaInicioContrato;

    @Column(name = "sueldo", nullable = false)  // Sueldo -> Se descuenta por incidentes o algo parecido ( por el momento nop )
    private double sueldo;

    @ManyToOne
    @JoinColumn(name = "bodega_id") //  Bodega en la que trabaja el PersonalBodega
    @Comment("Bodega en la que trabaja el Personal Bodega.")
    private Bodega bodega;

}
