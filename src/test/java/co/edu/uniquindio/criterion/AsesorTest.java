package co.edu.uniquindio.criterion;
import java.util.*;

import co.edu.uniquindio.criterion.repositories.AsesorRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import co.edu.uniquindio.criterion.model.Asesor;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class AsesorTest {

    @Autowired
    private AsesorRepo asesorRepo;
    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearAsesorTest() {
        Asesor asesor = new Asesor("Pedro", "1004870909", "3136100183", "pedro@gmail.com", "Calle 6 Barrio Granada",
                "PedroAbogado715", "ContraseñaDePedro", firmaAbogadosRepo.findById(1).orElse(null));

        Asesor asesorGuardado = asesorRepo.save(asesor);
        System.out.println(asesorGuardado);
        Assertions.assertNotNull(asesorGuardado);
    }

    @Test
    @Sql("classpath:test.sql")
    void eliminarAsesorTest() {

        Asesor asesorGuardado = asesorRepo.findById("1004870907").orElse(null);
        if (asesorGuardado == null) {
            Assertions.fail("No existe el asesor con la cedula dada");
        }
        asesorRepo.delete(asesorGuardado);
        Asesor asesorRecuperado = asesorRepo.findById("1004870907").orElse(null);
        Assertions.assertNull(asesorRecuperado, "No se ha eliminado el asesor");
        System.out.println("El asesor ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarAsesorTest() {

        Asesor asesorGuardado = asesorRepo.findById("1004870907").orElse(null);
        System.out.println("Nombre actual: " + asesorGuardado.getNombre());
        asesorGuardado.setNombre("Didier");
        asesorRepo.save(asesorGuardado);
        Asesor buscado = asesorRepo.findById("1004870907").orElse(null);
        Assertions.assertEquals("Didier", buscado.getNombre());
        System.out.println("Nombre nuevo: " + buscado.getNombre());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarAsesoresTest() {

        List<Asesor> lista = asesorRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

}