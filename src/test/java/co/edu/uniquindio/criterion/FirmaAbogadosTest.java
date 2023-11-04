package co.edu.uniquindio.criterion;

import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;


import co.edu.uniquindio.criterion.model.FirmaAbogados;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class FirmaAbogadosTest {


    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;



    @Test
    @Sql("classpath:test.sql")
    void actualizarFirmaDeAbogadosTest() {

        FirmaAbogados firmaAbogadosGuardada = firmaAbogadosRepo.findById(1).orElse(null);
        System.out.println("Nombre actual: " + firmaAbogadosGuardada.getNombre());
        firmaAbogadosGuardada.setNombre("Criticas criterion");
        firmaAbogadosRepo.save(firmaAbogadosGuardada);
        FirmaAbogados buscado = firmaAbogadosRepo.findById(1).orElse(null);
        Assertions.assertEquals("Criticas criterion", buscado.getNombre());
        System.out.println("Nombre nuevo: " + buscado.getNombre());

    }

    @Test
    @Sql("classpath:test.sql")
    void ObtenerFirmaDeAbogados() {

        FirmaAbogados firmaAbogados = firmaAbogadosRepo.findById(1).orElse(null);
        Assertions.assertNotNull(firmaAbogados);
        System.out.println(firmaAbogados);
    }

}