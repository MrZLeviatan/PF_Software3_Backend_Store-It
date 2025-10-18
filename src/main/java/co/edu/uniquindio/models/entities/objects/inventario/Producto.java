package co.edu.uniquindio.models.entities.objects.inventario;


import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
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
@Table(name = "producto")
@Comment("Entidad que representa un Producto Almacenado o Vendido.")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único del EspacioProducto")
    private Long id;

    @Column(name = "codigo_barras", nullable = false, unique = true)    // Código de barras único por producto
    @Comment("Código de Barras relacionado a un único producto")
    private String codigoBarras;

    @Column(name = "nombre_producto", nullable = false)
    @Comment("Nombre del producto")
    private String nombre;

    @Column(name = "valor_compra", nullable = false)
    @Comment("Valor compra del producto")
    private Double valorCompra;

    @Column(name = "valor_venta", nullable = false) // Se establece mediante un cálculo del sistema.
    @Comment("Valor venta establecido por el sistema")
    private Double valorVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    @Comment("Clae o Tipo de producto")
    private TipoProducto tipoProducto;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL)
    @Comment("Espacio físico donde se encuentra almacenado el producto")
    private EspacioProducto espacioProducto;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false) // Foreign key en la tabla producto
    private Proveedor proveedor;



    @PrePersist // Se ejecuta antes de insertar entidades en la base de datos
    @PreUpdate  // Se ejecuta antes de actualizar entidades en la base de datos.
    private void calcularValorVenta() {
        if (valorCompra != null && valorCompra > 0) {
            this.valorVenta = valorCompra * 1.20;  // Aplica un 20% de ganancia
        } else {
            this.valorVenta = 0.0;  // Valor por defecto si no hay valorCompra válido
        }
    }

}
