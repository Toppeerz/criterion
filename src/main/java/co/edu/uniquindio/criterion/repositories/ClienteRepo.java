package co.edu.uniquindio.criterion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.criterion.model.Cliente;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, String> {


}