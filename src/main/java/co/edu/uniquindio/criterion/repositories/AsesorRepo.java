package co.edu.uniquindio.criterion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.criterion.model.Asesor;

@Repository
public interface AsesorRepo extends JpaRepository<Asesor, String> {


}
