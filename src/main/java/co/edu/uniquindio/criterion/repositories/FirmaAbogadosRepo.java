package co.edu.uniquindio.criterion.repositories;

import co.edu.uniquindio.criterion.model.FirmaAbogados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmaAbogadosRepo extends JpaRepository<FirmaAbogados, Integer> {


}