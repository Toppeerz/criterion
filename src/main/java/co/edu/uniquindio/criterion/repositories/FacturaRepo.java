package co.edu.uniquindio.criterion.repositories;

import co.edu.uniquindio.criterion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepo extends JpaRepository<Factura, Integer> {


}