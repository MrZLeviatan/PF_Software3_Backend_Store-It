package co.edu.uniquindio.models.entities.users;

import co.edu.uniquindio.models.embeddable.DatosLaborales;
import co.edu.uniquindio.models.entities.objects.solicitudes.Resolucion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_bodega")   // Tabla del administrador de las bodegas
@DiscriminatorValue("ADMIN_BODEGA")
@Comment("Entidad que representa al Administrador de la Bodega")
public class AdminBodega extends Persona{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_laborales_id",nullable = false)
    @Comment("Datos laborales del empleado")
    private DatosLaborales datosLaborales;

    @OneToMany(mappedBy = "resolutor")
    @Comment("Resoluciones hechas por el Admin")
    private List<Resolucion> resoluciones;


}
