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
@ToString
@NoArgsConstructor
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Debe ingresar el titulo del documento")
    private String titulo;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(nullable = false, length = 1000)
    @NotEmpty(message = "Debe ingresar el contenido del documento")
    private String contenido;

    @ManyToOne
    @NotNull
    private Caso caso;

    public Documento(String titulo, LocalDateTime fecha, String contenido, Caso caso) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.contenido = contenido;
        this.caso = caso;
    }
}
