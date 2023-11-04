package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reunion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Debe ingresar la ubicacion de la reunion")
    private String ubicacion;

    @Column(nullable = false, length = 1000)
    @NotEmpty(message = "Debe ingresar las notas de la reunion")
    private String notas;

    @ManyToOne
    @NotNull
    private Caso caso;

    public Reunion(LocalDateTime fecha, String ubicacion, String notas, Caso caso) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.notas = notas;
        this.caso = caso;
    }
}
