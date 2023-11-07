package co.edu.uniquindio.criterion.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Admin;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.PersonaRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class LoginController {

    @Autowired
    private PersonaRepo personaRepo;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Label labelContrasenia;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private Label txtError;

    @FXML
    private TextField txtNombreDeUsuario;

    @Autowired
    private SceneController sceneController;

    @FXML
    void iniciarSesion(ActionEvent event) {
        String nombre = txtNombreDeUsuario.getText();
		String contrasenia = txtContraseña.getText();
       
            
		Persona persona = personaRepo.findByNombreDeUsuario(nombre);
        if(persona != null){
		if(contrasenia.equals(persona.getContraseña()) )
				{
                    if(persona instanceof Abogado){
                        abrirVentanaAbogado(event, persona);
                    }else if(persona instanceof Asesor){
                        abrirVentanaAsesor(event, persona);
		            }else if(persona instanceof Admin){
                        abrirVentanaAdmin(event);
                    }else if(persona instanceof Cliente){
                        abrirVentanaCliente(event, persona);
                    }
                }
            else{
        txtError.setText("Contraseña incorrecta");
        }
    }else{
        txtError.setText("No hay existe un usuario con ese nombre");
    }
    }

    private void abrirVentanaCliente(ActionEvent event, Persona persona) {
        sceneController.cambiarAVistaCliente(event, persona);
    }

    private void abrirVentanaAsesor(ActionEvent event, Persona persona) {
        sceneController.cambiarAVistaAsesor(event, persona);
    }

    private void abrirVentanaAbogado(ActionEvent event, Persona persona) {
        sceneController.cambiarAVistaAbogado(event, persona);
    }

    private void abrirVentanaAdmin(ActionEvent event) {
        sceneController.cambiarAAdminCrud(event);
        
    }


}
