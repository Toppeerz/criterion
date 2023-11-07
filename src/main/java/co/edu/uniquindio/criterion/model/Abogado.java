package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Abogado extends Persona {
    
    @Column(nullable = false, length = 100, unique = true)
    @NotEmpty(message = "Debe ingresar el numero de licencia del abogado")
    private String numeroDeLicencia;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate fechaDeInicioDeFirma;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Especializacion especializacion;

    @ManyToMany
    @ToString.Exclude
    private List<Cliente> clientes = new ArrayList<>();

    @OneToMany(mappedBy = "abogado", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Caso> casos = new ArrayList<>();

    public Abogado(String nombre, String cedula, String telefono, String email, String direccion,
            String nombreDeUsuario, String contraseña, FirmaAbogados firmaAbogados, String numeroDeLicencia,
            LocalDate fechaDeInicioDeFirma, Especializacion especializacion) {
        super(nombre, cedula, telefono, email, direccion, nombreDeUsuario, contraseña, firmaAbogados);
        this.fechaDeInicioDeFirma = fechaDeInicioDeFirma;
        this.numeroDeLicencia = numeroDeLicencia;
        this.especializacion = especializacion;
    }
}
