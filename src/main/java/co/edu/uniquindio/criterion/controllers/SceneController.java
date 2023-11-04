package co.edu.uniquindio.criterion.controllers;

import java.io.IOException;

import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.CriterionApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

@Component
public class SceneController {

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
    
    
}
