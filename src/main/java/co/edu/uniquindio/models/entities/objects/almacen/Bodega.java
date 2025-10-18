package co.edu.uniquindio.models.entities.objects.almacen;

import co.edu.uniquindio.models.embeddable.DatosLaborales;
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
@Table(name = "bodegas")
@Comment("Entidad que representan las Bodegas de una Sede.")
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la Bodega")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sede_id")   // Sede en la que se encuentra la bodega
    @Comment("Sede asociado a la bodega.")
    private Sede sede;

    // Datos relacionados a los trabajadores de la bodega (no importa su rol)
    @OneToMany(mappedBy = "bodega")
    @Comment("Datos laborales del personal que trabaja en la bodega.")
    private List<DatosLaborales> datosLaborales;

    // Division de las subBodegas de la misma Bodega
    @OneToMany(mappedBy = "bodega")
    @Comment("SubBodegas relacionadas a la Bodega.")
    private List<SubBodega> subBodegas;

}
