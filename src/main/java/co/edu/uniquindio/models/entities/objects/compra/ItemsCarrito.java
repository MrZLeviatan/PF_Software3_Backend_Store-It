package co.edu.uniquindio.models.entities.objects.compra;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items_carrito")
@Comment("Entidad que representa los Items del Carrito")
public class ItemsCarrito {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único del item del carrito")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Comment("Producto asociado al item")
    private Producto producto;

    @Column(name = "cantidad")
    @Comment("Cantidad del producto")
    private int cantidad;

    @Column(name = "valor_total")
    @Comment("Valor total del Producto * Cantidad")
    private Double valorTotal;


    @ManyToOne
    @JoinColumn(name = "carrito_compra_id",nullable = false)
    @Comment("Carrito asociado al item")
    private CarritoCompra carritoCompra;




    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    @PreUpdate  // Se ejecuta antes de actualizar entidades en la base de datos.
    private void calcularValorItem(){
        if (producto != null && cantidad > 0){
            // Se obtiene el precio del producto y se multiplica por la cantidad
            this.valorTotal = producto.getValorVenta()*cantidad;
        }else{
            this.valorTotal = 0.0;
        }
    }
}
