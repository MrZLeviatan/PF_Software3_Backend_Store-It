package co.edu.uniquindio.models.entities.objects.solicitudes.incidentes;

import co.edu.uniquindio.models.entities.objects.inventario.movimiento.Movimiento;
import co.edu.uniquindio.models.entities.users.PersonalBodega;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.TipoIncidente;
import co.edu.uniquindio.models.enums.entities.TipoIncidenteMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("MOVIMIENTO")
@Table(name = "incidente_movimiento")
@Comment("Incidente relacionado a los movimientos del Producto")
public class IncidenteMovimiento extends Incidente{


    @ManyToOne
    @JoinColumn(name = "movimiento_id",nullable = false)
    @Comment("Movimiento relacionado al Incidente")
    private Movimiento movimiento;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @Comment("Proveedor como posible culpable")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "personal_bodega_id")
    @Comment("Personal de Bodega responsable (SI APLICA)")
    private PersonalBodega responsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidente_movimiento",nullable = false)
    @Comment("Tipo de Incidente relacionado al Movimiento")
    private TipoIncidenteMovimiento tipoIncidenteMovimiento;


    public IncidenteMovimiento() {
        super.setTipoIncidente(TipoIncidente.MOVIMIENTO);
    }

}
