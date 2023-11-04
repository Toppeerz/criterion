package co.edu.uniquindio.criterion.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Asesor extends Persona {

    @OneToMany(mappedBy = "asesorCreador", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Cliente> clientes = new ArrayList<>();

    @OneToMany(mappedBy = "asesorCaso", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Caso> casos = new ArrayList<>();

    public Asesor(String nombre, String cedula, String telefono, String email, String direccion, String nombreDeUsuario,
            String contraseña, FirmaAbogados firmaAbogados) {
        super(nombre, cedula, telefono, email, direccion, nombreDeUsuario, contraseña, firmaAbogados);
    }
}
