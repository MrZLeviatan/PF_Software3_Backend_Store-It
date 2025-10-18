package co.edu.uniquindio.models.entities.objects.compra;

import co.edu.uniquindio.models.entities.users.Cliente;
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
@Table(name = "carrito_compra")
@Comment("Entidad que representa el Carrito Compra de un Cliente")
public class CarritoCompra {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único del carrito compra")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id",nullable = false)
    @Comment("Cliente dueño del carrito de compras")
    private Cliente cliente;

    @Column(name = "total_valor")
    @Comment("Total del Valor del Carrito Compra")
    private Double totalValor;

    @OneToMany(mappedBy = "carritoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemsCarrito> itemsCarrito;



    // Método que recalcula automáticamente el total antes de insertar o actualizar la entidad
    @PrePersist
    @PreUpdate
    private void calcularTotal() {
        if (itemsCarrito != null && !itemsCarrito.isEmpty()) {
            totalValor = itemsCarrito.stream()
                    .mapToDouble(ItemsCarrito::getValorTotal)
                    .sum();
        } else {
            totalValor = 0.0;
        }
    }


    // Método auxiliar para añadir un item y recalcular el total
    public void agregarItem(ItemsCarrito item) {
        itemsCarrito.add(item);
        item.setCarritoCompra(this);
        calcularTotal();
    }


    // Método auxiliar para eliminar un itenm y recalcular el total
    public void eliminarItem(ItemsCarrito item) {
        itemsCarrito.remove(item);
        item.setCarritoCompra(null);
        calcularTotal();
    }

}
