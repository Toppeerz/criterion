package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.ClienteRepo;
import co.edu.uniquindio.criterion.repositories.PersonaRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class ClienteCrudController implements Initializable {

    @Autowired
    private SceneController sceneController;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private PersonaRepo personaRepo;
    private Persona personaLogueada;
    ObservableList<Persona> listaPersonasObservable = FXCollections.observableArrayList();
    private Persona clienteSeleccionado;
    private static final String VALIDACION_DATOS = "Validacion de datos";

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField buscador;

    @FXML
    private TableColumn<Persona, String> columnaCedula;

    @FXML
    private TableColumn<Persona, String> columnaContrasenia;

    @FXML
    private TableColumn<Persona, String> columnaCorreo;

    @FXML
    private TableColumn<Persona, String> columnaDireccion;

    @FXML
    private TableColumn<Persona, String> columnaNombre;

    @FXML
    private TableColumn<Persona, String> columnaTelefono;

    @FXML
    private TableColumn<Persona, String> columnaUsuario;

    @FXML
    private TableView<Persona> tablaClientes;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtContrasenia;

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
    void actualizar(ActionEvent event) {
        actualizarPersona();
    }

    private void actualizarPersona() {
        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String direccion = txtDireccion.getText();
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();


        String informacionVerificada = verificarInformacionPersona(nombre, cedula, telefono, correo, direccion, usuario,
                contrasenia);

        if (clienteSeleccionado == null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un cliente para actualizar",
                    AlertType.WARNING);
            return;
        }

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }

        if (!validaciones(cedula, correo, usuario)) {
            return;
        }

        try {
            actualizarPersonaPorTipo(nombre, cedula, telefono, correo, direccion, usuario, contrasenia);
            mostrarMensaje("Gestion de personas", "Informacion", "Cliente actualizado con exito",
                    AlertType.INFORMATION);
        } catch (TransactionSystemException e) {
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

    private boolean validaciones(String cedula, String correo, String usuario) {
        if (personaRepo.findById(cedula).orElse(null) == null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No existe un cliente con esa cedula",
                    AlertType.WARNING);
            return false;
        }

        if (personaRepo.findByNombreDeUsuario(usuario) != null && !personaRepo.findByNombreDeUsuario(usuario)
                .getCedula().equalsIgnoreCase(cedula)) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe un cliente con ese nombre de usuario",
                    AlertType.WARNING);
            return false;
        }

        if (personaRepo.findByEmail(correo) != null && !personaRepo.findByEmail(correo).getCedula()
                .equalsIgnoreCase(cedula)) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe un cliente con ese correo",
                    AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void actualizarPersonaPorTipo(String nombre, String cedula, String telefono, String correo,
            String direccion, String usuario, String contrasenia) {
        clienteSeleccionado.setNombre(nombre);
        clienteSeleccionado.setCedula(cedula);
        clienteSeleccionado.setTelefono(telefono);
        clienteSeleccionado.setEmail(correo);
        clienteSeleccionado.setDireccion(direccion);
        clienteSeleccionado.setNombreDeUsuario(usuario);
        clienteSeleccionado.setContraseña(contrasenia);
        clienteRepo.save((Cliente) clienteSeleccionado);
        refrescarTablaPersonas();
        limpiarInterfazPersona();
    }

    @FXML
    void crear(ActionEvent event) {
        crearPersona();
    }

    private void crearPersona() {
        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String direccion = txtDireccion.getText();
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();

        String informacionVerificada = verificarInformacionPersona(nombre, cedula, telefono, correo, direccion, usuario,
                contrasenia);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }

        if (personaRepo.findById(cedula).orElse(null) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe un cliente con esa cedula",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByNombreDeUsuario(usuario) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe un cliente con ese nombre de usuario",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByEmail(correo) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe un cliente con ese correo",
                    AlertType.WARNING);
            return;
        }
        try {
            crearPersonaPorTipo(nombre, cedula, telefono, correo, direccion, usuario, contrasenia);
            mostrarMensaje("Gestion de clientes", "Informacion", "Cliente creado con exito",
                    AlertType.INFORMATION);
        } catch (TransactionSystemException e) {
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

    private void crearPersonaPorTipo(String nombre, String cedula, String telefono, String correo, String direccion,
            String usuario, String contrasenia) {

        Cliente cliente = new Cliente(nombre, cedula, telefono, correo, direccion, usuario, contrasenia, sceneController.getFirmaAbogados(), (Asesor) personaLogueada);
        clienteRepo.save(cliente);
        refrescarTablaPersonas();
        limpiarInterfazPersona();

    }

    private String verificarInformacionPersona(String nombre, String cedula, String telefono, String correo,
            String direccion, String usuario, String contrasenia) {
        String mensaje = "";

        if (nombre.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el nombre del cliente \n";
        }
        if (cedula.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la cedula del cliente \n";
        }
        if (cedula.length() > 10) {
            mensaje += "Por favor ingrese una cedula valida (10 digitos maximo) \n";
        }
        if (telefono.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el telefono del cliente \n";
        }
        if (correo.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el correo del cliente \n";
        }
        if (direccion.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la direccion del cliente \n";
        }
        if (usuario.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el usuario del cliente \n";
        }
        if (contrasenia.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la contrasenia del cliente \n";
        }
        if (!isNumeric(cedula)) {
            mensaje += "Por favor ingrese un numero de cedula valido \n";
        }
        if (!isNumeric(telefono)) {
            mensaje += "Por favor ingrese un numero de telefono valido \n";
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
    void eliminar(ActionEvent event) {
        eliminarPersona();
    }

    private void eliminarPersona() {

        boolean confirmacion;

        if (clienteSeleccionado != null) {
            confirmacion = mostrarMensajeConfirmacion("Confirme que desea eliminar el cliente seleccionado");
            if (confirmacion) {
                personaRepo.delete(clienteSeleccionado);
                limpiarInterfazPersona();
                refrescarTablaPersonas();
                clienteSeleccionado = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Cliente eliminado con exito",
                        AlertType.INFORMATION);
            }

        }

        else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un cliente para eliminar",
                    AlertType.WARNING);
        }

    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmacion");
        alert.setContentText(mensaje);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent()) {
            return (action.get() == ButtonType.OK);
        } else {
            return false;
        }
    }

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    void volver(ActionEvent event) {
        sceneController.cambiarAVistaAsesor(event, personaLogueada);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personaLogueada = sceneController.getPersonaLogueada();
        inicializarTablaPersonas();

        refrescarTablaPersonas();

        inicializarBuscador();

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            clienteSeleccionado = newSelection;

            mostrarInformacionPersona(clienteSeleccionado);

            if (clienteSeleccionado == null) {
                limpiarInterfazPersona();
            }

        });
    }

    private void inicializarBuscador() {
        FilteredList<Persona> listaPersonasFiltrada = new FilteredList<>(listaPersonasObservable, b -> true);
        buscador.textProperty().addListener((obs, oldValue, newValue) -> listaPersonasFiltrada.setPredicate(persona -> {

            if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (persona.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (persona.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (persona.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (persona.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (persona.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (persona.getNombreDeUsuario().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else {
                return (persona.getContraseña().toLowerCase().contains(lowerCaseFilter));
            }
        }));

        SortedList<Persona> listaProductosSorteados = new SortedList<>(listaPersonasFiltrada);

        listaProductosSorteados.comparatorProperty().bind(tablaClientes.comparatorProperty());

        tablaClientes.setItems(listaProductosSorteados);
    }

    private void inicializarTablaPersonas() {
        this.columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.columnaCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        this.columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        this.columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreDeUsuario"));
        this.columnaContrasenia.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
    }

    private void refrescarTablaPersonas() {
        listaPersonasObservable.clear();
        listaPersonasObservable.addAll(clienteRepo.findAll());

    }

    private void limpiarInterfazPersona() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
    }

    private void mostrarInformacionPersona(Persona personaSeleccionada) {
        if (personaSeleccionada != null) {
            txtNombre.setText(personaSeleccionada.getNombre());
            txtCedula.setText(personaSeleccionada.getCedula());
            txtTelefono.setText(personaSeleccionada.getTelefono());
            txtCorreo.setText(personaSeleccionada.getEmail());
            txtDireccion.setText(personaSeleccionada.getDireccion());
            txtUsuario.setText(personaSeleccionada.getNombreDeUsuario());
            txtContrasenia.setText(personaSeleccionada.getContraseña());
        }

    }
}
