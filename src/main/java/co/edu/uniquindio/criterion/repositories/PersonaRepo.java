package co.edu.uniquindio.criterion.repositories;

import co.edu.uniquindio.criterion.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, String> {

    Persona findByNombreDeUsuario(String nombre);

    Persona findByEmail(String email);
}