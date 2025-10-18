package co.edu.uniquindio.models.entities.objects.inventario.movimiento;

import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.EstadoMovimientoIngreso;
import co.edu.uniquindio.models.enums.entities.TipoMovimiento;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("INGRESO")
@Table(name = "movimiento_ingreso")
@Comment("Movimiento que representa el ingreso de un lote a la bodega")
public class MovimientoIngreso extends Movimiento {


    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @Comment("Proveedor del Lote")
    private Proveedor proveedor;

    @OneToOne
    @JoinColumn(name = "movimiento_ingreso_id")
    @Comment("Documento asociado al movimiento ingreso del Lote.")
    private DocumentoIngreso documentoIngreso;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_movimiento_ingreso", nullable = false)
    @Comment("Estado en base a los procesos del ingreso del Lote")
    private EstadoMovimientoIngreso estadoMovimientoIngreso;


    public MovimientoIngreso(){
        super.setTipoMovimiento(TipoMovimiento.INGRESO);
    }


}
