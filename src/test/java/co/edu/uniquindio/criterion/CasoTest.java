package co.edu.uniquindio.criterion;
import java.time.LocalDate;
import java.util.*;

import co.edu.uniquindio.criterion.repositories.AsesorRepo;
import co.edu.uniquindio.criterion.repositories.CasoRepo;
import co.edu.uniquindio.criterion.repositories.ClienteRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Especializacion;
import co.edu.uniquindio.criterion.model.EstadoCaso;
import co.edu.uniquindio.criterion.model.FirmaAbogados;
import co.edu.uniquindio.criterion.repositories.AbogadoRepo;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class CasoTest {

    @Autowired
    private CasoRepo casoRepo;
    @Autowired
    private AbogadoRepo abogadoRepo;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private AsesorRepo asesorRepo;
    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;


    @Test
    @Sql("classpath:test.sql")
    void crearCasoTest() {

        Asesor asesor = asesorRepo.findById("1004870907").orElse(null);
        Abogado abogado = abogadoRepo.findById("1004870909").orElse(null);
        Cliente cliente = clienteRepo.findById("1004870906").orElse(null);
        FirmaAbogados firmaAbogados = firmaAbogadosRepo.findById(1).orElse(null);


        Caso caso = new Caso("Caso de prueba", LocalDate.now(), LocalDate.now().plusMonths(5), Especializacion.CIVIL, EstadoCaso.ABIERTO, abogado, asesor, cliente, firmaAbogados);

        Caso casoGuardado = casoRepo.save(caso);
        Assertions.assertNotNull(casoGuardado);
        System.out.println(casoGuardado);
    }



    @Test
    @Sql("classpath:test.sql")
    void eliminarCasoTest() {

        Caso casoGuardado = casoRepo.findById(1).orElse(null);
        if (casoGuardado == null) {
            Assertions.fail("No existe el caso con la id dada");
        }
        casoRepo.delete(casoGuardado);
        Caso casoRecuperado = casoRepo.findById(1).orElse(null);
        Assertions.assertNull(casoRecuperado, "No se ha eliminado el caso");
        System.out.println("El caso ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarCasoTest() {

        Caso casoGuardado = casoRepo.findById(1).orElse(null);
        System.out.println("Descripcion del caso actual: " + casoGuardado.getDescripcion());
        casoGuardado.setDescripcion("nueva descripcion");
        casoRepo.save(casoGuardado);
        Caso buscado = casoRepo.findById(1).orElse(null);
        Assertions.assertEquals("nueva descripcion", buscado.getDescripcion());
        System.out.println("Descripcion nueva: " + buscado.getDescripcion());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarCasosTest() {

        List<Caso> lista = casoRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

 }