package co.edu.uniquindio.models.entities.objects.inventario.movimiento;

import co.edu.uniquindio.models.entities.objects.compra.DetalleCompra;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.TipoMovimiento;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoRetiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("RETIRO")
@Table(name = "movimiento_retiro")
@Comment("Movimiento que representa el retiro o salida de un lote de la bodega")
public class MovimientoRetiro extends Movimiento {


    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Comment("Cliente que solicita el retiro")
    private Cliente cliente;


    @ManyToOne
    @JoinColumn(name = "documento_retiro_id")
    @Comment("Documento asociado al movimiento retiro del Lote.")
    private DocumentoRetiro documentoRetiro;


    @ManyToOne
    @JoinColumn(name = "detalle_compra_id", nullable = false)
    @Comment("Detalle de producto al que pertenece este movimiento de retiro")
    private DetalleCompra detalleCompra;



    public MovimientoRetiro() {
        super.setTipoMovimiento(TipoMovimiento.RETIRO);
    }
}
