package co.edu.uniquindio.models.entities.objects.almacen;


import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.embeddable.UnidadAlmacenamiento;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
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
@Table(name = "sub_bodega")
@Comment("Entidad que representan las Sub_Bodegas contenidas en las Bodegas.")
public class SubBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la SubBodega")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bodega_id")   // Sede en la que se encuentra la bodega
    @Comment("Sede asociado a la bodega.")
    private Bodega bodega;

    @Embedded   // Clase embeddable que representa el espacio de la subBodega
    private UnidadAlmacenamiento unidadAlmacenamiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_subBodega", nullable = false)
    @Comment("El tipo de producto que se puede almacenar en la subBodega")
    private TipoProducto tipoProducto;

    // Lista de espacios asignados a productos dentro de la sub bodega
    @OneToMany(mappedBy = "subBodega")
    @Comment("Lista de espacios con productos almacenados en esta sub-bodega")
    private List<EspacioProducto> espaciosProductos;



    // Agrega un nuevo espacio y válida capacidad disponible
    public void agregarEspacio(EspacioProducto espacio)
            throws ElementoNoValidoException {

        Double volumenEspacio = espacio.getUnidadAlmacenamiento().getVolumenTotal();

        // Verifica que el volumen no supere la capacidad disponible
        if (volumenEspacio > unidadAlmacenamiento.getVolumenDisponible()) {
            throw new ElementoNoValidoException("No hay suficiente espacio disponible en la sub-bodega.");
        }

        // Suma el volumen ocupado y actualiza estado
        unidadAlmacenamiento.agregarVolumen(volumenEspacio);

        // Asocia el espacio a esta sub odega
        espacio.setSubBodega(this);
        espaciosProductos.add(espacio);
    }



    public void actualizarVolumen(Double deltaVolumen) throws ElementoNoValidoException {
        if (deltaVolumen == null) return;
        // Si deltaVolumen es positivo -> se está ocupando espacio
        if(deltaVolumen > 0){
            if (deltaVolumen > unidadAlmacenamiento.getVolumenDisponible()) {
                throw new ElementoNoValidoException("No hay suficiente espacio en la sub-bodega para ocupar este volumen adicional.");
            }
            unidadAlmacenamiento.agregarVolumen(deltaVolumen);
        }

        // Si deltaVolumen es negativo -> se está liberando espacio
        if (deltaVolumen < 0) {
            unidadAlmacenamiento.liberarVolumen(deltaVolumen);
        }
    }


    // Elimina un espacio y libera su volumen
    public void eliminarEspacio(EspacioProducto espacio) {
        if (espaciosProductos.remove(espacio)) {
            Double volumen = espacio.getUnidadAlmacenamiento().getVolumenTotal();
            unidadAlmacenamiento.liberarVolumen(volumen);
        }
    }
}
