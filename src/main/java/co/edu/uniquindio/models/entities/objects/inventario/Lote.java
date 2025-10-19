package co.edu.uniquindio.models.entities.objects.inventario;


import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.Movimiento;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteLote;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lote_producto")
@Comment("Entidad que representan los Lotes de los Productos.")
public class Lote {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único del Lote")
    private Long id;

    @Column(name = "codigo_lote", nullable = false)   // Se genera mediante el código Producto y una inicial con un número (A1)
    @Comment("Código representativo del Lote")
    private String codigoLote;

    @Column(name = "fecha_ingreso", nullable = false)
    @Comment("Fecha de ingreso del Lote")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_vencimiento" )    // Es opcional porque depende del tipo de Producto
    @Comment("Fecha de Vencimiento (Aplica a ciertos Productos)")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_lote", nullable = false)
    @Comment("Estado del proceso del Lote")
    private EstadoLote estadoLote;

    @Column(name = "cantidad_disponible",nullable = false)
    @Comment("Cantidad total de los productos")
    private int cantidadDisponible;

    @Column(name = "valor_total")   // Solo se aplica el valor en el ingreso
    @Comment("Valor total del Lote en base al valor del Producto")
    private Double valorTotal;

    @Column(name = "area_total", nullable = false)
    @Comment("Area ocupada por el Lote")
    private Double areaTotal;


    // ----- RELACIONES ---------

    @ManyToOne
    @JoinColumn(name = "espacio_producto_id")
    @Comment("Espacio asociado al Lote")
    private EspacioProducto espacioProducto;


    @OneToMany(mappedBy = "lote")
    private List<Movimiento> movimientos;


    @OneToMany(mappedBy = "lote")
    private List<IncidenteLote> incidenteLotes;



    public void retirarCantidad(int cantidad) throws ElementoNoValidoException {
        if (cantidad <= 0) {
            throw new ElementoNoValidoException("La cantidad a retirar debe ser mayor a cero.");
        }

        if (cantidad > cantidadDisponible) {
            throw new ElementoNoValidoException("No hay suficiente cantidad disponible en el lote.");
        }

        // Actualiza cantidad del lote
        int cantidadAnterior = this.cantidadDisponible;
        this.cantidadDisponible -= cantidad;

        // Actualiza la cantidad total del espacio producto
        if (espacioProducto != null) {
            int diferencia = cantidadAnterior - this.cantidadDisponible;
            espacioProducto.setCantidadTotal(
                    Math.max(0, espacioProducto.getCantidadTotal() - diferencia)
            );
        }

        // Si el lote se agota, actualiza estado
        if (this.cantidadDisponible == 0) {
            this.estadoLote = EstadoLote.AGOTADO;
        }
    }

    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    private void calcularValorTotal() {
        if (cantidadDisponible > 0) {
            // Recalcular el valor total automáticamente antes de persistir
            valorTotal = espacioProducto.getProducto().getValorCompra() * cantidadDisponible;

        } else {
            valorTotal = 0.0;
        }
    }

    public void agregarMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
        movimiento.setLote(this);
    }

}
