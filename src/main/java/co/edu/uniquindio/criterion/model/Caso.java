package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Caso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Debe ingresar la descripcion del caso")
    private String descripcion;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate fechaApertura;

    @Column(nullable = false)
    @Future
    private LocalDate fechaLimite;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Especializacion especializacion;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private EstadoCaso estadoCaso;

    @ManyToOne
    @NotNull
    private Abogado abogado;

    @ManyToOne
    @NotNull
    private Asesor asesorCaso;

    @OneToMany(mappedBy = "caso", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Documento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "caso", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Reunion> reuniones = new ArrayList<>();

    @OneToOne(mappedBy = "caso", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Factura factura;

    @ManyToOne
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @NotNull
    private FirmaAbogados firmaAbogados;

    public Caso(String descripcion, LocalDate fechaApertura, LocalDate fechaLimite, Especializacion especializacion,
            EstadoCaso estadoCaso, Abogado abogado, Asesor asesorCaso, Cliente cliente, FirmaAbogados firmaAbogados) {
        this.descripcion = descripcion;
        this.fechaApertura = fechaApertura;
        this.fechaLimite = fechaLimite;
        this.especializacion = especializacion;
        this.estadoCaso = estadoCaso;
        this.abogado = abogado;
        this.asesorCaso = asesorCaso;
        this.cliente = cliente;
        this.firmaAbogados = firmaAbogados;
    }
}
