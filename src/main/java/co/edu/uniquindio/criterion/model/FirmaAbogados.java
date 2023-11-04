package co.edu.uniquindio.criterion.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.*;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FirmaAbogados implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "La empresa debe tener un nombre")
    private String nombre;

    @OneToMany(mappedBy = "firmaAbogados", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Caso> casos = new ArrayList<>();

    @OneToMany(mappedBy = "firmaAbogados", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Persona> personas = new ArrayList<>();

    public FirmaAbogados(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
