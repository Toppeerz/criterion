package co.edu.uniquindio.criterion;

import java.time.LocalDate;
import java.util.*;

import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;


import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Especializacion;
import co.edu.uniquindio.criterion.repositories.AbogadoRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class AbogadoTest {

    @Autowired
    private AbogadoRepo abogadoRepo;
    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearAbogadoTest() {
        // Arregla el constructor de abajo
        Abogado abogado = new Abogado("Pedro", "22", "3136100183", "22@gmail", "Calle 6 Barrio Granada",
                "PedroAbogado715", "ContraseñaDePedro", firmaAbogadosRepo.findById(1).orElse(null), "1234567890",
                LocalDate.now(), Especializacion.CIVIL);
        Abogado abogadoGuardado = abogadoRepo.save(abogado);
        Assertions.assertNotNull(abogadoGuardado);
        System.out.println(abogadoGuardado);
    }

    @Test
    @Sql("classpath:test.sql")
    void eliminarAbogadoTest() {
        Abogado abogadoGuardado = abogadoRepo.findById("1004870909").orElse(null);
        if (abogadoGuardado == null) {
            Assertions.fail("No existe el abogado con la cedula dada");
        }
        abogadoRepo.delete(abogadoGuardado);
        Abogado abogadoRecuperado = abogadoRepo.findById("1004870909").orElse(null);
        Assertions.assertNull(abogadoRecuperado, "No se ha eliminado el abogado");
        System.out.println("El abogado ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarAbogadoTest() {

        Abogado abogadoGuardado = abogadoRepo.findById("1004870909").orElse(null);
        System.out.println("Nombre actual: " + abogadoGuardado.getNombre());
        abogadoGuardado.setNombre("Didier");
        abogadoRepo.save(abogadoGuardado);
        Abogado buscado = abogadoRepo.findById("1004870909").orElse(null);
        Assertions.assertEquals("Didier", buscado.getNombre());
        System.out.println("Nombre nuevo: " + buscado.getNombre());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarAbogadosTest() {

        List<Abogado> lista = abogadoRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

}