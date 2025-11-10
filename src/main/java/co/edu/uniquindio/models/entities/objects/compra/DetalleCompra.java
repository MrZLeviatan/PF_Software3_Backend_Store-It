package co.edu.uniquindio.models.entities.objects.compra;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.Movimiento;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoRetiro;
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
@Table(name = "detalle_compra")
@Comment("Entidad que representa los detalles de una  compras por parte de los clientes")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único del detalle de la compra")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Comment("Producto asociado al detalle de la compra")
    private Producto producto;

    @Column(name = "cantidad",nullable = false)
    @Comment("Cantidad del producto")
    private int cantidad;

    @Column(name = "valor_detalle",nullable = false)
    @Comment("Precio Unitario total del detalle")
    private Double valorDetalle;

    @ManyToOne
    @JoinColumn(name = "compra_id",nullable = false)
    @Comment("Compra asociada al detalle")
    private Compra compra;


    @OneToMany(mappedBy = "detalleCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Lista de movimientos de retiro asociados a este detalle de producto")
    private List<MovimientoRetiro> movimientoRetiros;



    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    @PreUpdate  // Se ejecuta antes de actualizar entidades en la base de datos.
    private void calcularValorDetalle(){
        if (producto != null && cantidad > 0){
            // Se obtiene el precio del producto y se multiplica por la cantidad
            this.valorDetalle = producto.getValorVenta()*cantidad;
        }else{
            this.valorDetalle = 0.0;
        }
    }

}
