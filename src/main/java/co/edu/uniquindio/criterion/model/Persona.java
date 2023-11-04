package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;

import java.io.Serializable;

/**
 * Persona class.
 */
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "El usuario debe tener un nombre")
    private String nombre;

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 10)
    private String cedula;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "El usuario debe tener un numero de telefono")
    private String telefono;

    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "El usuario debe tener un correo valido")
    private String email;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "El usuario debe tener una dirección valida")
    private String direccion;

    @Column(nullable = false, length = 100, unique = true)
    @UniqueElements(message = "El usuario debe tener un nombre de usuario unico")
    @NotEmpty(message = "El usuario debe tener un nombre de usuario")
    private String nombreDeUsuario;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "El usuario debe tener una contraseña")
    private String contraseña;

    @ManyToOne
    @NotNull
    private FirmaAbogados firmaAbogados;
}