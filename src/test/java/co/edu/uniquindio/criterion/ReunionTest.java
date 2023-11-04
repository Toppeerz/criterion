package co.edu.uniquindio.criterion;
import java.time.LocalDateTime;
import java.util.*;


import co.edu.uniquindio.criterion.repositories.CasoRepo;


import co.edu.uniquindio.criterion.repositories.ReunionRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;


import co.edu.uniquindio.criterion.model.Reunion;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class ReunionTest {

    @Autowired
    private CasoRepo casoRepo;

    @Autowired
    private ReunionRepo reunionRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearReunionTest() {

        Reunion reunion = new Reunion(LocalDateTime.now(), "Armenia", "La reunion fue exitosa", casoRepo.findById(1).orElse(null));

        Reunion reunionGuardada = reunionRepo.save(reunion);
        Assertions.assertNotNull(reunionGuardada);
        System.out.println(reunionGuardada);
    }



    @Test
    @Sql("classpath:test.sql")
    void eliminarReunionTest() {

        Reunion reunionGuardada = reunionRepo.findById(1).orElse(null);
        if (reunionGuardada == null) {
            Assertions.fail("No existe la reunion con la id dada");
        }
        reunionRepo.delete(reunionGuardada);
        Reunion documentoRecuperado = reunionRepo.findById(1).orElse(null);
        Assertions.assertNull(documentoRecuperado, "No se ha eliminado la reunion");
        System.out.println("La reunion ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarReunionTest() {

        Reunion reunionGuardada = reunionRepo.findById(1).orElse(null);
        System.out.println("Notas actual: " + reunionGuardada.getNotas());
        reunionGuardada.setNotas("Notas nuevas");
        reunionRepo.save(reunionGuardada);
        Reunion buscado = reunionRepo.findById(1).orElse(null);
        Assertions.assertEquals("Notas nuevas", buscado.getNotas());
        System.out.println("Notas nuevo: " + buscado.getNotas());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarReunionesTest() {

        List<Reunion> lista = reunionRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

 }