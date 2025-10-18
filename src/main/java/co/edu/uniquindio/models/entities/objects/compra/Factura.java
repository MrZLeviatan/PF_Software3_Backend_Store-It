package co.edu.uniquindio.models.entities.objects.compra;

import co.edu.uniquindio.models.entities.objects.almacen.Sede;
import co.edu.uniquindio.models.entities.users.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "factura")
@Comment("Entidad que representa la factura de una compra")
public class Factura {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único de la factura")
    private Long id;

    @OneToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @Comment("Compra asociada a la factura")
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "cliente_id",nullable = false)
    @Comment("Cliente quien realiza la compra")
    private Cliente cliente;

    @JoinColumn(name = "sub_total")
    @Comment("SubTotal de la compra")
    private Double subTotal;

    @JoinColumn(name = "total_paga")
    @Comment("Total a Pagar (Incluyendo IVA)")
    private Double totalPaga;

    @JoinColumn(name = "iva")
    @Comment("Iva aplicado a la compra (19%)")
    private Double iva;

    @JoinColumn(name = "fecha_compra")
    @Comment("Fecha en la que se realizo la compra")
    private LocalDate fechaCompra;

    @ManyToOne
    @JoinColumn(name = "sede_id")
    @Comment("Sede en la que se realizo la compra")
    private Sede sede;


    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    @PreUpdate  // Se ejecuta antes de actualizar entidades en la base de datos.
    private void calcularTotalPagar(){
        if (compra != null){
            // Se suma el iva y el subTotal
            this.totalPaga = compra.getSubTotal() + compra.getIva();
            this.iva = compra.getIva();
            this.subTotal = compra.getSubTotal();
        }else{
            this.totalPaga = 0.0;
            this.iva = 0.0;
            this.subTotal = 0.0;
        }
    }
}
