package co.edu.uniquindio.criterion.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Factura class.
 */
@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaFacturacion;

    @Column(nullable = false)
    @PositiveOrZero
    private Double total;

    @Column(nullable = false)
    private Boolean estadoDePago;

    @ManyToOne
    @NotNull
    private Caso caso;

    public Factura(LocalDateTime fechaFacturacion, Double total, Boolean estadoDePago, Caso caso) {
        this.fechaFacturacion = fechaFacturacion;
        this.total = total;
        this.estadoDePago = estadoDePago;
        this.caso = caso;
    }

}
