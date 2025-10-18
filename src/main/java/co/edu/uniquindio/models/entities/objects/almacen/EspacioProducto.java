package co.edu.uniquindio.models.entities.objects.almacen;


import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.embeddable.UnidadAlmacenamiento;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
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
@Table(name = "espacio_producto")
@Comment("Entidad que representan los Espacios de un único Producto contenidas en las SubBodegas.")
public class EspacioProducto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único del EspacioProducto")
    private Long id;

    @Embedded   // Clase embeddable que representa el espacio de la subBodega
    private UnidadAlmacenamiento unidadAlmacenamiento;

    @ManyToOne  // SubBodega a la que pertenece el espacio producto
    @JoinColumn(name = "sub_bodega_id")
    private SubBodega subBodega;

    @OneToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Comment("Producto asociado a este espacio en la subbodega")
    private Producto producto;


    @Column(name = "cantidad_total")
    @Comment("Cantidad total de productos en base a los lotes")
    private int cantidadTotal;

    // Solicitudes asociadas al espacio producto
    @OneToMany(mappedBy = "espacioProducto")
    private List<Solicitud> solicitudes;

    // Lotes asociados al espacio
    @OneToMany(mappedBy = "espacioProducto")
    private List<Lote> lotes;


    // Método para agregar un nuevo lote al espacio
    public void agregarLote(Lote lote) throws ElementoNoValidoException {
        if (lote == null) return;

        // Calcula el volumen que ocupará el lote (m³)
        Double volumenLote = lote.getAreaTotal();
        if (volumenLote == null || volumenLote <= 0) {
            throw new ElementoNoValidoException("El lote debe tener un área o volumen válido.");
        }

        // Verifica si hay suficiente espacio disponible
        if (volumenLote > unidadAlmacenamiento.getVolumenDisponible()) {
            throw new ElementoNoValidoException("No hay suficiente espacio disponible en el EspacioProducto para agregar este lote.");
        }

        // Agrega el volumen ocupado
        unidadAlmacenamiento.agregarVolumen(volumenLote);

        // Suma la cantidad de productos del lote a la cantidad total
        cantidadTotal += lote.getCantidadDisponible();

        // Asocia el lote a este espacio
        lote.setEspacioProducto(this);
        lotes.add(lote);
    }


    // Método para liberar espacio ( Lotes en estado AGOTADO, CADUCADO )
    public void liberarEspacio(Lote lote) throws ElementoNoValidoException {
        if (lote == null || !lotes.contains(lote)) return;

        // Si el lote NO está caducado NI rechazado, se lanza excepción
        if (lote.getEstadoLote() != EstadoLote.CADUCADO &&
                lote.getEstadoLote() != EstadoLote.RECHAZADO &&
                lote.getEstadoLote() != EstadoLote.AGOTADO) {
            throw new ElementoNoValidoException("No se puede liberar espacio de un lote activo");
        }

        // Resta el volumen ocupado
        Double volumenLote = lote.getAreaTotal();
        unidadAlmacenamiento.liberarVolumen(volumenLote);

        // Actualiza la cantidad total
        cantidadTotal = Math.max(0, cantidadTotal - lote.getCantidadDisponible());
    }

}
