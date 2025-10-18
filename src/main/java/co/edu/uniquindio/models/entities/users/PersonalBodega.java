package co.edu.uniquindio.models.entities.users;


import co.edu.uniquindio.models.embeddable.DatosLaborales;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.ParticipacionMovimiento;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.Incidente;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteMovimiento;
import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;
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
@Table(name = "personal_bodega")
@DiscriminatorValue("PERSONAL_BODEGA")
@Comment("Entidad que representa a los trabajadores de una Bodega")
public class PersonalBodega extends Persona{


    // Relación uno a uno con los datos laborales

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_laborales_id",nullable = false)
    @Comment("Datos laborales del empleado")
    private DatosLaborales datosLaborales;


    @Enumerated(EnumType.STRING)    // Tipo de Personal Bodega, para manejos de roles y autorizaciones.
    @Column(name = "tipo_personal_bodega", nullable = false)
    @Comment("Tipo de Personal de Bodega ( Administrador, Auxiliar, Pasante ).")
    private TipoPersonalBodega tipoPersonalBodega;


    @OneToMany(mappedBy = "personalBodega", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Participaciones en movimientos realizadas por este PersonalBodega")
    private List<ParticipacionMovimiento> participacionesMovimiento;


    @OneToMany(mappedBy = "reportadoPor")
    @Comment("Incidentes reportados por el Personal Bodega")
    private List<Incidente> incidentes;


    @OneToMany(mappedBy = "responsable")
    @Comment("Incidentes que fueron responsabilidad del Personal Bodega")
    private List<IncidenteMovimiento> listaResponsabilidad;



    // Método funcional para obtener el Rol del Personal de Bodega mediante tu Tipo.
    public String getRol() {
        return switch (this.tipoPersonalBodega) {
            case AUXILIAR_BODEGA -> "ROLE_AUXILIAR_BODEGA";
            case GESTOR_INVENTARIO -> "ROLE_GESTOR_INVENTARIO";
            default -> "ROLE_GESTOR_BODEGA";
        };
    }
}