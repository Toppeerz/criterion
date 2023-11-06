package co.edu.uniquindio.criterion.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.CriterionApplication;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.FirmaAbogados;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;
import javafx.event.ActionEvent;
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

    private FirmaAbogados firmaAbogados;
    private Persona personaLogueada;
    private Caso casoSeleccionado;

    @Autowired
    public SceneController(FirmaAbogadosRepo firmaAbogadosRepo) {
        this.firmaAbogadosRepo = firmaAbogadosRepo;
        firmaAbogados = firmaAbogadosRepo.findById(1).orElse(null);
        if (firmaAbogados == null) {
            firmaAbogados = new FirmaAbogados(1, "Criterion");
            firmaAbogadosRepo.save(firmaAbogados);
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
    public void cambiarADetallesCaso(MouseEvent event, Caso casoSeleccionado) {
        try {
            this.casoSeleccionado=casoSeleccionado;
            FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/DetalleCaso.fxml"));
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
    
    
}
