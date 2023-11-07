package co.edu.uniquindio.criterion.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.criterion.model.Caso;

@Repository
public interface CasoRepo extends JpaRepository<Caso, Integer> {


    List<Caso> findAllByAbogado_Cedula(String cedula);
    List<Caso> findAllByAsesorCaso_Cedula(String cedula);
    List<Caso> findAllByCliente_Cedula(String cedula);
}
