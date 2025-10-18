package co.edu.uniquindio.models.entities.objects.inventario.documento;

import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documento_ingreso")
@Comment("Entidad que representa el documento físico o digital asociado a un movimiento de ingreso")
public class DocumentoIngreso {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único del documento de ingreso")
    private Long id;

    @Column(name = "codigo_ingreso", nullable = false, unique = true)
    @Comment("Código único del ingreso")
    private String codigoIngreso;

    @Column(name = "area_total", nullable = false)
    @Comment("Area total del Lote a ingresar")
    private Double areaTotal;

    @Column(name = "fecha_entrega",nullable = false)
    @Comment("Fecha de entrega del Lote")
    private LocalDate fechaEntrega;

    @Column(name = "cantidad",nullable = false)
    @Comment("La cantidad de productos en el Lote")
    private int cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_proceso",nullable = false)
    @Comment("Estado del proceso de ingreso del Documento")
    private EstadoProceso estadoProceso;


    // ------ Relaciones del Documento ----------


    // Relación con el Gestor Comercial (quien realiza la compra)
    @ManyToOne
    @JoinColumn(name = "gestor_comercial_id")
    @Comment("Gestor comercial responsable del ingreso del lote")
    private GestorComercial gestorComercial;

    // Relación con el Proveedor (quien entrega el producto)
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @Comment("Proveedor que entrega el lote")
    private Proveedor proveedor;

    // Relación con el Lote asociado al documento
    @OneToOne
    @JoinColumn(name = "lote_id", nullable = false)
    @Comment("Lote asociado al documento de ingreso")
    private Lote lote;

    @OneToOne(mappedBy = "documentoIngreso")
    @Comment("Movimiento al que le pertenece el Documento de ingreso")
    private MovimientoIngreso movimientoIngreso;



}
