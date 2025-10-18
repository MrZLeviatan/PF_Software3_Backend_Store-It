package co.edu.uniquindio.models.entities.objects.solicitudes.incidentes;

import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.enums.entities.TipoIncidente;
import co.edu.uniquindio.models.enums.entities.TipoIncidenteLote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("LOTE")
@Table(name = "incidente_lote")
@Comment("Incidente relacionado a los lotes")
public class IncidenteLote extends Incidente{


    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = false)
    @Comment("Lote asociado al incidente")
    private Lote lote;

    @Column(name = "valor_daños_propiedad")
    @Comment("Valor relacionado a posibles daños en la propiedad")
    private Double valorDaniosPropiedad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidente_lote", nullable = false)
    @Comment("Tipo de Incidente relacionado al Lote")
    private TipoIncidenteLote tipoIncidenteLote;


    public IncidenteLote() {
        super.setTipoIncidente(TipoIncidente.LOTE);
    }
}
