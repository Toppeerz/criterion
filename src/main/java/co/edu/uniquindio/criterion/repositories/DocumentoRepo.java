package co.edu.uniquindio.criterion.repositories;


import co.edu.uniquindio.criterion.model.Documento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepo extends JpaRepository<Documento, Integer> {

    List<Documento> findAllByCaso_Id(Integer id);
}
