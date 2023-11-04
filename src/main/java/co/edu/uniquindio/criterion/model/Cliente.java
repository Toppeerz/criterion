package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Cliente extends Persona {

    @ManyToMany(mappedBy = "clientes")
    @ToString.Exclude
    private List<Abogado> abogados = new ArrayList<>();

    @ManyToOne
    @NotNull
    private Asesor asesorCreador;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Caso> casos = new ArrayList<>();

    public Cliente(String nombre, String cedula, String telefono, String email, String direccion,
            String nombreDeUsuario,
            String contraseña, FirmaAbogados firmaAbogados, Asesor asesorCreador) {
        super(nombre, cedula, telefono, email, direccion, nombreDeUsuario, contraseña, firmaAbogados);
        this.asesorCreador = asesorCreador;
    }

}