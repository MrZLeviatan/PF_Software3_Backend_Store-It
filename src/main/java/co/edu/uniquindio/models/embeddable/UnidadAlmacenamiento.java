package co.edu.uniquindio.models.embeddable;


import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.enums.embeddable.EstadoUnidad;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class UnidadAlmacenamiento {


    // Largo de la unidad en metros
    @Column(name = "largo")
    @Comment("Representación del largo del espacio")
    private Double largo;

    @Column(name = "ancho")
    @Comment("Representación del ancho del espacio")
    private Double ancho;

    @Column(name = "alto")
    @Comment("Representación del alto del espacio")
    private Double alto;

    @Column(name = "volumen_ocupado")
    @Comment("Representación del volumen ocupado del espacio total")
    private Double volumenOcupado = 0.0;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_unidad", nullable = false)
    @Comment("Estado actual de la unidad: DISPONIBLE, LLENO, EN_MANTENIMIENTO")
    private EstadoUnidad estadoUnidad;


    // Calcula el volumen total de la unidad (largo × ancho × alto).
    public Double getVolumenTotal() {
        if (largo == null || ancho == null || alto == null) {
            return 0.0;
        }
        return largo * ancho * alto;
    }


    // Calcula el volumen disponible de la unidad
    public Double getVolumenDisponible(){
        if (volumenOcupado == 0.0) {
            return getVolumenTotal();
        }
        return getVolumenTotal() -  volumenOcupado;
    }


    // Actualiza automáticamente el estado según el volumen ocupado
    public void actualizarEstado() {
        double total = getVolumenTotal();
        if (volumenOcupado >= total) {
            estadoUnidad = EstadoUnidad.LLENO;
        } else if (volumenOcupado > 0) {
            estadoUnidad = EstadoUnidad.DISPONIBLE;
        } else {
            estadoUnidad = EstadoUnidad.DISPONIBLE;
        }
    }


    // Devuelve el porcentaje de ocupación de la unidad
    public double getPorcentajeOcupacion() {
        double total = getVolumenTotal();
        return (total == 0) ? 0 : (volumenOcupado / total) * 100;
    }


    // Incrementa el volumen ocupado y actualiza estado
    public void agregarVolumen(Double volumen) throws ElementoNoValidoException {
        if (volumen == null || volumen <= 0) return;
        if (volumenOcupado + volumen > getVolumenTotal()) {
            throw new ElementoNoValidoException("El volumen excede la capacidad disponible de la unidad.");
        }
        volumenOcupado += volumen;
        actualizarEstado();
    }

    // Resta volumen ocupado y actualiza estado
    public void liberarVolumen(Double volumen) {
        if (volumen == null || volumen <= 0) return;
        volumenOcupado = Math.max(0, volumenOcupado - volumen);
        actualizarEstado();
    }
}
