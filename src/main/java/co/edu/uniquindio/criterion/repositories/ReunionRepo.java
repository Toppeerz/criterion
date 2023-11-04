package co.edu.uniquindio.criterion.repositories;

import co.edu.uniquindio.criterion.model.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReunionRepo extends JpaRepository<Reunion, Integer> {


}