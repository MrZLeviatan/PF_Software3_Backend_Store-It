package co.edu.uniquindio.models.entities.objects.inventario.movimiento;


import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteMovimiento;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.models.enums.entities.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Se crean tablas hijas relacionadas a la tabla madre (esta)
@Comment("Clase base que representa un movimiento genérico de un lote")
public abstract class Movimiento {


    // Proceso general del Movimiento (las primeras características)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único del Movimiento Lote")
    private Long id;

    @Column(name = "fecha_solicitud", nullable = false)
    @Comment("Fecha en la que se realizó la solicitud del movimiento")
    private LocalDate fechaSolicitud;

    @Column(name = "cantidad", nullable = false)    // Se debe verificar antes si cumple nos las normas de negocio
    @Comment("Cantidad a manejar en el movimiento")
    private int cantidad;

    @Column(name = "observaciones")
    @Comment("Observaciones relacionadas al Movimiento")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    @Comment("Tipo de movimiento: INGRESO o RETIRO")
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    @Comment("Lote asociado al Movimiento")
    private Lote lote;

    // Lista de incidentes relacionadas al Movimiento
    @OneToMany(mappedBy = "movimiento")
    private List<IncidenteMovimiento> incidentesMovimiento;


    // ----- Proceso de verificación y autorización de la bodega -----


    @Column(name = "fecha_confirmación")
    @Comment("Fecha en la que se realizó la confirmación del movimiento")
    private LocalDate fechaConfirmacion;

    @ManyToOne
    @JoinColumn(name = "gestor_comercial_id")
    @Comment("Gestor Comercial encargado de la compra de nuevos productos o verificación de compra por parte del cliente")
    private GestorComercial gestorComercial;


    @OneToMany(mappedBy = "movimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Participaciones de PersonalBodega asociadas a este movimiento")
    private List<ParticipacionMovimiento> participaciones;


    public void agregarParciticacion(ParticipacionMovimiento participacionMovimiento) {
        participaciones.add(participacionMovimiento);
        participacionMovimiento.setMovimiento(this);
    }




}
