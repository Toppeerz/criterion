package co.edu.uniquindio.criterion.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.CriterionApplication;
import co.edu.uniquindio.criterion.model.Admin;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.Documento;
import co.edu.uniquindio.criterion.model.FirmaAbogados;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.model.Reunion;
import co.edu.uniquindio.criterion.repositories.AdminRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Component
public class SceneController {

    private final FirmaAbogadosRepo firmaAbogadosRepo;
    private final AdminRepo adminRepo;

    private FirmaAbogados firmaAbogados;
    private Persona personaLogueada;
    private Caso casoSeleccionado;
    private Documento documentoSeleccionado;
    private Reunion reunionSeleccionada;

    @Autowired
    public SceneController(FirmaAbogadosRepo firmaAbogadosRepo, AdminRepo adminRepo) {
        this.firmaAbogadosRepo = firmaAbogadosRepo;
        this.adminRepo = adminRepo;
        firmaAbogados = firmaAbogadosRepo.findById(1).orElse(null);
        if (firmaAbogados == null) {
            firmaAbogados = new FirmaAbogados(1, "Criterion");
            Admin admin = new Admin("Admin", "1", "1", "admin@gmail.com", "1", "1", "1", firmaAbogados);
            firmaAbogadosRepo.save(firmaAbogados);
            adminRepo.save(admin);
        }
    }


    public void cambiarAAdminCrud(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/AdminCrud.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void cambiarALogin(ActionEvent event){
        try {
            personaLogueada=null;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/Login.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void cambiarAVistaAbogado(ActionEvent event, Persona persona) {
        try {
            personaLogueada = persona;
            casoSeleccionado=null;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/Abogado.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void cambiarAActualizarInfo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/ActualizarPersona.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void cambiarADetallesCaso(Event event, Caso casoSeleccionado) {
        try {
            this.casoSeleccionado=casoSeleccionado;
            this.documentoSeleccionado=null;
            this.reunionSeleccionada=null;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/DetallesCaso.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void cambiarAVistaAsesor(ActionEvent event, Persona persona) {
        try {
            personaLogueada = persona;
            casoSeleccionado=null;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/Asesor.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
        
    }


    public void cambiarAClientesCrud(ActionEvent event, Persona personaLogueada2) {
        try {
            personaLogueada = personaLogueada2;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/ClienteCrud.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void cambiarAVistaCliente(ActionEvent event, Persona persona) {
        try {
            personaLogueada = persona;
            casoSeleccionado=null;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/Cliente.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void cambiarADetallesReunion(MouseEvent event, Reunion reunionSeleccionada) {
        try {
            this.reunionSeleccionada=reunionSeleccionada;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/DetallesReunion.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void cambiarADetallesDocumento(MouseEvent event, Documento documentoSeleccionado) {
        try {
            this.documentoSeleccionado=documentoSeleccionado;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/DetallesDocumento.fxml"));
            loader.setControllerFactory(CriterionApplication.context::getBean);
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    
    
}
