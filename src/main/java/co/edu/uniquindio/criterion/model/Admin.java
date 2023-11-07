package co.edu.uniquindio.criterion.model;

import lombok.*;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Admin extends Persona {

    public Admin(String nombre, String cedula, String telefono, String email, String direccion, String nombreDeUsuario,
            String contraseña, FirmaAbogados firmaAbogados) {
        super(nombre, cedula, telefono, email, direccion, nombreDeUsuario, contraseña, firmaAbogados);
    }
}
