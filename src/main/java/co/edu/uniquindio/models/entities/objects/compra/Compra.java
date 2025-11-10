package co.edu.uniquindio.models.entities.objects.compra;

import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.EstadoCompra;
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
@Table(name = "compra")
@Comment("Entidad que representa las compras por parte de los clientes")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único de la compra")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Comment("Cliente propietario de la compra.")
    private Cliente cliente;

    @Column(name = "sub_total",nullable = false)
    @Comment("SubTotal del precio de la compra")
    private Double subTotal;

    @Column(name = "iva",nullable = false)
    @Comment("Iva aplicado a la compra (19%)")
    private Double iva;

    @Column(name = "fecha_compra",nullable = false)
    @Comment("Fecha en que se realizo la compra")
    private LocalDate fechaCompra;

    @Column(name = "fecha_entrega",nullable = false)
    @Comment("Fecha en la que se realizara la entrega")
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_compra",nullable = false)
    @Comment("Estado del proceso de la compra")
    private EstadoCompra estadoCompra;


    // -------- Relaciones de la Compra

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Detalles de productos comprados.")
    private List<DetalleCompra> detalleCompra;

    // Relación 1:1 con Factura
    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL)
    @Comment("Factura asociada a esta compra.")
    private Factura factura;


    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    @PreUpdate  // Se ejecuta antes de actualizar entidades en la base de datos.
    public void calcularTotales() {
        if (detalleCompra != null && !detalleCompra.isEmpty()) {
            // Suma todos los valores detalle
            subTotal = detalleCompra.stream()
                    .mapToDouble(DetalleCompra::getValorDetalle)
                    .sum();

            // Calcula el IVA como el 19% del subtotal
            iva = subTotal * 0.19;
        } else {
            subTotal = 0.0;
            iva = 0.0;
        }
    }


}
