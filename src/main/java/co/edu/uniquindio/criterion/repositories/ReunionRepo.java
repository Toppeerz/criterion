package co.edu.uniquindio.criterion.repositories;

import co.edu.uniquindio.criterion.model.Reunion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReunionRepo extends JpaRepository<Reunion, Integer> {

    List<Reunion> findAllByCaso_Id(Integer id);
}
