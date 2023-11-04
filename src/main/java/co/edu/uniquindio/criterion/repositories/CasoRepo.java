package co.edu.uniquindio.criterion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.criterion.model.Caso;

@Repository
public interface CasoRepo extends JpaRepository<Caso, Integer> {


}