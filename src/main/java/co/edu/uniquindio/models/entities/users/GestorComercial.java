package co.edu.uniquindio.models.entities.users;


import co.edu.uniquindio.models.embeddable.DatosLaborales;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.Movimiento;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.Incidente;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteMovimiento;
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
@Table(name = "gestor_comercial")   // Tabla del administrador de las bodegas
@DiscriminatorValue("GESTOR_COMERCIAL")
@Comment("Entidad que representa a los Gestores Comerciales")
public class GestorComercial extends Persona{


    // Relación uno a uno con los datos laborales

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_laborales_id",nullable = false)
    @Comment("Datos laborales del empleado")
    private DatosLaborales datosLaborales;


    // Relación uno a muchos con las solicitudes generadas por el Gestor
    @OneToMany(mappedBy = "gestorComercial")
    @Comment("Lista de Solicitudes por parte del Gestor Comercial")
    private List<Solicitud> solicitudes;


    @OneToMany(mappedBy = "gestorComercial")
    @Comment("Lista de Movimientos asociado con el Gestor Comercial")
    private List<Movimiento> movimientos;


    public void agregarSolicitud(Solicitud solicitud){
        solicitudes.add(solicitud);
        solicitud.setGestorComercial(this);
    }

    public void agregarMovimiento(Movimiento movimiento){
        movimientos.add(movimiento);
        movimiento.setGestorComercial(this);
    }


}
