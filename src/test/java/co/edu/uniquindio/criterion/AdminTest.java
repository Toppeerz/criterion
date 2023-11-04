package co.edu.uniquindio.criterion;
import java.util.*;

import co.edu.uniquindio.criterion.repositories.AdminRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import co.edu.uniquindio.criterion.model.Admin;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class AdminTest {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private FirmaAbogadosRepo firmaAbogadosRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearAdminTest() {        

        Admin admin = new Admin("Pedro", "1004870909", "3136100183", "pedro@gmail.com", "Calle 6 Barrio Granada", "PedroAbogado715", "ContraseñaDePedro", firmaAbogadosRepo.findById(1).orElse(null));

        Admin adminGuardado = adminRepo.save(admin);
        System.out.println(adminGuardado);
        Assertions.assertNotNull(adminGuardado);
    }


    @Test
    @Sql("classpath:test.sql")
    void eliminarAdminTest() {

        Admin adminGuardado = adminRepo.findById("1004870908").orElse(null);
        if (adminGuardado == null) {
            Assertions.fail("No existe el admin con la cedula dada");
        }
        adminRepo.delete(adminGuardado);
        Admin adminRecuperado = adminRepo.findById("1004870908").orElse(null);
        Assertions.assertNull(adminRecuperado, "No se ha eliminado el admin");
        System.out.println("El admin ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarAdminTest() {

        Admin adminGuardado = adminRepo.findById("1004870908").orElse(null);
        System.out.println("Nombre actual: " + adminGuardado.getNombre());
        adminGuardado.setNombre("Didier");
        adminRepo.save(adminGuardado);
        Admin buscado = adminRepo.findById("1004870908").orElse(null);
        Assertions.assertEquals("Didier", buscado.getNombre());
        System.out.println("Nombre nuevo: " + buscado.getNombre());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarAdminsTest() {

        List<Admin> lista = adminRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

}