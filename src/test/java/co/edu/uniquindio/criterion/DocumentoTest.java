package co.edu.uniquindio.criterion;

import java.time.LocalDateTime;
import java.util.*;


import co.edu.uniquindio.criterion.repositories.CasoRepo;

import co.edu.uniquindio.criterion.repositories.DocumentoRepo;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import co.edu.uniquindio.criterion.model.Documento;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes=App.class)
class DocumentoTest {

    @Autowired
    private CasoRepo casoRepo;
    @Autowired
    private DocumentoRepo documentoRepo;

    @Test
    @Sql("classpath:test.sql")
    void crearDocumentoTest() {

        Documento documento = new Documento("titulo1", LocalDateTime.now(), "Contenido", casoRepo.findById(1).orElse(null));

        Documento documentoGuardado = documentoRepo.save(documento);
        Assertions.assertNotNull(documentoGuardado);
        System.out.println(documentoGuardado);
    }



    @Test
    @Sql("classpath:test.sql")
    void eliminarDocumentoTest() {

        Documento documentoGuardado = documentoRepo.findById(1).orElse(null);
        if (documentoGuardado == null) {
            Assertions.fail("No existe el documento con la id dada");
        }
        documentoRepo.delete(documentoGuardado);
        Documento documentoRecuperado = documentoRepo.findById(1).orElse(null);
        Assertions.assertNull(documentoRecuperado, "No se ha eliminado el documento");
        System.out.println("El documento ha sido eliminado correctamente");
    }

    @Test
    @Sql("classpath:test.sql")
    void actualizarDocumentoTest() {

        Documento documentoGuardado = documentoRepo.findById(1).orElse(null);
        System.out.println("Titulo actual: " + documentoGuardado.getTitulo());
        documentoGuardado.setTitulo("Titulo nuevo");
        documentoRepo.save(documentoGuardado);
        Documento buscado = documentoRepo.findById(1).orElse(null);
        Assertions.assertEquals("Titulo nuevo", buscado.getTitulo());
        System.out.println("Descripcion nueva: " + buscado.getTitulo());

    }

    @Test
    @Sql("classpath:test.sql")
    void listarDocumentosTest() {

        List<Documento> lista = documentoRepo.findAll();
        Assertions.assertNotNull(lista);
        lista.forEach(System.out::println);
    }

 }