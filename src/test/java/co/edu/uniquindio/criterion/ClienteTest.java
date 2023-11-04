package co.edu.uniquindio.criterion;
import java.util.*;

import co.edu.uniquindio.criterion.repositories.AsesorRepo;
import co.edu.uniquindio.criterion.repositories.ClienteRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import co.edu.uniquindio.criterion.model.Cliente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class ClienteTest {

    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private AsesorRepo asesorRepo;
    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearClienteTest() {

        Cliente cliente = new Cliente("Pedro", "1", "3136100183", "cliente1@gmail.com", "Calle 6 Barrio Granada",
                "PedroAbogado715", "ContraseñaDePedro", firmaAbogadosRepo.findById(1).orElse(null),
                asesorRepo.findById("1004870907").orElse(null));

        Cliente clienteGuardado = clienteRepo.save(cliente);
        Assertions.assertNotNull(clienteGuardado);
        System.out.println(clienteGuardado);
    }

    @Test
    @Sql("classpath:test.sql")
    void eliminarClienteTest() {

        Cliente clienteGuardado = clienteRepo.findById("1004870906").orElse(null);
        if (clienteGuardado == null) {
            Assertions.fail("No existe el cliente con la id dada");
        }
        clienteRepo.delete(clienteGuardado);
        Cliente casoRecuperado = clienteRepo.findById("1004870906").orElse(null);
        Assertions.assertNull(casoRecuperado, "No se ha eliminado el cliente");
        System.out.println("El cliente ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarClienteTest() {

        Cliente clienteGuardado = clienteRepo.findById("1004870906").orElse(null);
        System.out.println("Nombre actual: " + clienteGuardado.getNombre());
        clienteGuardado.setNombre("Didier");
        clienteRepo.save(clienteGuardado);
        Cliente buscado = clienteRepo.findById("1004870906").orElse(null);
        Assertions.assertEquals("Didier", buscado.getNombre());
        System.out.println("Descripcion nueva: " + buscado.getNombre());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarClientesTest() {

        List<Cliente> lista = clienteRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

}