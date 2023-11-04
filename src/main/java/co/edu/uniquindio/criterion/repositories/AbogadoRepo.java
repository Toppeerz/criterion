package co.edu.uniquindio.criterion.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.criterion.model.Abogado;


@Repository
public interface AbogadoRepo extends JpaRepository<Abogado, String> {

    List<Abogado> findAllByClientes_Cedula(String cedula);

}