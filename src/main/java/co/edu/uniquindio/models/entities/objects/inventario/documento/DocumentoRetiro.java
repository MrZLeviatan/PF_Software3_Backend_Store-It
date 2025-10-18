package co.edu.uniquindio.models.entities.objects.inventario.documento;

import co.edu.uniquindio.models.entities.objects.compra.Compra;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documento_retiro")
@Comment("Entidad que representa el documento físico o digital asociado a un movimiento de retiro")
public class DocumentoRetiro {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID interno único del documento retiro")
    private Long id;

    @JoinColumn(name = "codigo_retiro", nullable = false, unique = true)
    @Comment("Código único del retiro")
    private String codigoRetiro;

    @JoinColumn(name = "fecha_entrega",nullable = false)
    @Comment("Fecha de entrega del Lote")
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "estado_proceso",nullable = false)
    @Comment("Estado del proceso de retiro del Documento")
    private EstadoProceso estadoProceso;


    // ------ Relaciones del Documento ----------


    @OneToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @Comment("Lote asociado al documento de ingreso")
    private Compra compra;



}
