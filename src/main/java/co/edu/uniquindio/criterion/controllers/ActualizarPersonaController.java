package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.PersonaRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Component
public class ActualizarPersonaController implements Initializable {

    @Autowired
    private SceneController sceneController;
    private Persona personaLogueada;
    private static final String VALIDACION_DATOS = "Validacion de datos";

    @Autowired
    private PersonaRepo personaRepo;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVolver;

    @FXML
    private Label labelCedula;

    @FXML
    private Label labelContraseña;

    @FXML
    private Label labelCorreo;

    @FXML
    private Label labelDireccion;

    @FXML
    private Label labelEspecializacion;

    @FXML
    private Label labelFechaInicioFirma;

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelNumeroLicencia;

    @FXML
    private Label labelTelefono;

    @FXML
    private Label labelUsuario;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasenia;

    @FXML
    void actualizar(ActionEvent event) {
        actualizarInformacion();
    }

    private void actualizarInformacion() {
        
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String direccion = txtDireccion.getText();
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();

        String informacionVerificada = verificarInformacionPersona(nombre, telefono, correo, direccion, usuario,
                contrasenia);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }
        if (personaRepo.findByNombreDeUsuario(usuario) != null && !personaRepo.findByNombreDeUsuario(usuario)
                .getCedula().equalsIgnoreCase(personaLogueada.getCedula())) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese nombre de usuario",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByEmail(correo) != null && !personaRepo.findByEmail(correo).getCedula()
                .equalsIgnoreCase(personaLogueada.getCedula())) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese correo",
                    AlertType.WARNING);
            return;
        }

        try {
            crearPersonaPorTipo(nombre, telefono, correo, direccion, usuario,
                contrasenia);
            mostrarMensaje("Gestion de personas", "Informacion", "Su informacion se ha actualizado con exito",
                    AlertType.INFORMATION);
                refrescarInfoPersona();
                limpiarCampos();
        } catch (TransactionSystemException e) {
            personaLogueada=personaRepo.findById(personaLogueada.getCedula()).orElse(null);
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) t;
                if (!cve.getConstraintViolations().isEmpty()) {
                    String errorMessage = cve.getConstraintViolations().iterator().next().getMessage();
                    mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, errorMessage,
                            AlertType.WARNING);
                }
            }
        }
    
    }

    private void refrescarInfoPersona() {
        labelNombre.setText("Nombre: "+personaLogueada.getNombre());
        labelCedula.setText("Cedula: "+personaLogueada.getCedula());
        labelTelefono.setText("Telefono: "+personaLogueada.getTelefono());
        labelCorreo.setText("Correo: "+personaLogueada.getEmail());
        labelDireccion.setText("Direccion: "+personaLogueada.getDireccion());
        labelUsuario.setText("Usuario: "+personaLogueada.getNombreDeUsuario());
        labelContraseña.setText("Contraseña: "+personaLogueada.getContraseña());
    }

    private void crearPersonaPorTipo(String nombre, String telefono, String correo, String direccion, String usuario,
            String contrasenia) {
            Persona persona = personaLogueada;
            if(!nombre.equalsIgnoreCase("")){
                persona.setNombre(nombre);
            }
            if(!telefono.equalsIgnoreCase("")){
                persona.setTelefono(telefono);
            }
            if(!correo.equalsIgnoreCase("")){
                persona.setEmail(correo);
            }
            if(!direccion.equalsIgnoreCase("")){
                persona.setDireccion(direccion);
            }
            if(!usuario.equalsIgnoreCase("")){
                persona.setNombreDeUsuario(usuario);
            }
            if(!contrasenia.equalsIgnoreCase("")){
                persona.setContraseña(contrasenia);
            }
            personaLogueada= personaRepo.save(persona);
    }

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private String verificarInformacionPersona(String nombre, String telefono, String correo, String direccion,
            String usuario, String contrasenia) {
        String mensaje = "";

        if (nombre.equalsIgnoreCase("") && telefono.equalsIgnoreCase("") && correo.equalsIgnoreCase("")
                && direccion.equalsIgnoreCase("") && usuario.equalsIgnoreCase("") && contrasenia.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese informacion en los campos que desea actualizar \n";
        }
        if(!isNumeric(telefono) && !telefono.equalsIgnoreCase("")){
            mensaje += "El telefono debe ser numerico \n";
        }
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }

        return mensaje;
    }

    private boolean isNumeric(String string) {
        
        return string.matches("\\d+");
    }

    @FXML
    void limpiar(ActionEvent event) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
    }

    @FXML
    void volver(ActionEvent event) {
        if(personaLogueada instanceof Abogado){
        sceneController.cambiarAVistaAbogado(event, personaLogueada);}
        else{
            sceneController.cambiarAVistaAsesor(event, personaLogueada);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personaLogueada = sceneController.getPersonaLogueada();

        refrescarInfoPersona();

        if(personaLogueada instanceof Abogado){
            labelEspecializacion.setVisible(true);
            labelFechaInicioFirma.setVisible(true);
            labelNumeroLicencia.setVisible(true);
            Abogado abogado = (Abogado) personaLogueada;
            labelEspecializacion.setText("Especializacion: "+abogado.getEspecializacion().toString());
            labelFechaInicioFirma.setText("Fecha de inicio de firma: "+abogado.getFechaDeInicioDeFirma().toString());
            labelNumeroLicencia.setText("Numero de licencia: "+abogado.getNumeroDeLicencia());
        }else {
            labelEspecializacion.setVisible(false);
            labelFechaInicioFirma.setVisible(false);
            labelNumeroLicencia.setVisible(false);
        }
    }

}
