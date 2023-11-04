package co.edu.uniquindio.criterion;
import java.time.LocalDateTime;
import java.util.*;


import co.edu.uniquindio.criterion.repositories.CasoRepo;

import co.edu.uniquindio.criterion.repositories.FacturaRepo;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import co.edu.uniquindio.criterion.model.Factura;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class FacturaTest {

    @Autowired
    private CasoRepo casoRepo;

    @Autowired
    private FacturaRepo facturaRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearFacturaTest() {

        Factura factura = new Factura(LocalDateTime.now(), 5000.0, true, casoRepo.findById(1).orElse(null));

        Factura facturaGuardada = facturaRepo.save(factura);
        Assertions.assertNotNull(facturaGuardada);
        System.out.println(facturaGuardada);
    }



    @Test
    @Sql("classpath:test.sql")
    void eliminarFacturaTest() {

        Factura facturaGuardada = facturaRepo.findById(1).orElse(null);
        if (facturaGuardada == null) {
            Assertions.fail("No existe la factura con la id dada");
        }
        facturaRepo.delete(facturaGuardada);
        Factura documentoRecuperado = facturaRepo.findById(1).orElse(null);
        Assertions.assertNull(documentoRecuperado, "No se ha eliminado la factura");
        System.out.println("La factura ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarFacturaTest() {

        Factura facturaGuardada = facturaRepo.findById(1).orElse(null);
        System.out.println("Total actual: " + facturaGuardada.getTotal());
        facturaGuardada.setTotal(66000.0);
        facturaRepo.save(facturaGuardada);
        Factura buscado = facturaRepo.findById(1).orElse(null);
        Assertions.assertEquals(66000.0, buscado.getTotal());
        System.out.println("Total nuevo: " + buscado.getTotal());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarFacturasTest() {

        List<Factura> lista = facturaRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

 }