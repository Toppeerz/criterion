package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Admin;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Especializacion;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.AbogadoRepo;
import co.edu.uniquindio.criterion.repositories.AsesorRepo;
import co.edu.uniquindio.criterion.repositories.ClienteRepo;
import co.edu.uniquindio.criterion.repositories.FirmaAbogadosRepo;
import co.edu.uniquindio.criterion.repositories.PersonaRepo;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class AdminController implements Initializable {

    SceneController sceneController = new SceneController();
    ObservableList<Persona> listaPersonasObservable = FXCollections.observableArrayList();
    Persona personaSeleccionada;
    @Autowired
    PersonaRepo personaRepo;
    @Autowired
    AbogadoRepo abogadoRepo;
    @Autowired
    AsesorRepo asesorRepo;
    @Autowired
    ClienteRepo clienteRepo;
    @Autowired
    FirmaAbogadosRepo firmaAbogadosRepo;
    private static final String ABOGADO = "Abogado";
    private static final String CLIENTE = "Cliente";
    private static final String ASESOR = "Asesor";
    private static final String VALIDACION_DATOS = "Validacion de datos";

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnEliminar;

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
    private TableColumn<Persona, String> columnaTipo;

    @FXML
    private TableColumn<Persona, String> columnaUsuario;

    @FXML
    private ComboBox<String> especializacionAbogado;

    @FXML
    private TableView<Persona> tablaPersonas;

    @FXML
    private ComboBox<String> tipoUsuario;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtLicencia;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtUsuario;

    @FXML
    private Label labelEspecializacion;

    @FXML
    private Label labelNumeroLicencia;

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
        String txtTipoUsuario = String.valueOf(tipoUsuario.getSelectionModel().getSelectedItem());
        String txtEspecializacionAbogado = String.valueOf(especializacionAbogado.getSelectionModel().getSelectedItem());
        String numeroLicencia = txtLicencia.getText();

        String informacionVerificada = verificarInformacionPersona(nombre, cedula, telefono, correo, direccion, usuario,
                contrasenia, txtTipoUsuario, txtEspecializacionAbogado, numeroLicencia);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }

        if (personaRepo.findById(cedula).orElse(null) == null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No existe una persona con esa cedula",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByNombreDeUsuario(usuario) != null && !personaRepo.findByNombreDeUsuario(usuario)
                .getCedula().equalsIgnoreCase(cedula)) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese nombre de usuario",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByEmail(correo) != null && !personaRepo.findByEmail(correo).getCedula()
                .equalsIgnoreCase(cedula)) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese correo",
                    AlertType.WARNING);
            return;
        }

        try {
            crearPersonaPorTipo(nombre, cedula, telefono, correo, direccion, usuario, contrasenia, txtTipoUsuario,
                    txtEspecializacionAbogado, numeroLicencia);
            mostrarMensaje("Gestion de personas", "Informacion", "Persona actualizada con exito",
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

    @FXML
    void cerrarSesion(ActionEvent event) {

        sceneController.cambiarALogin(event);

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
        String txtTipoUsuario = String.valueOf(tipoUsuario.getSelectionModel().getSelectedItem());
        String txtEspecializacionAbogado = String.valueOf(especializacionAbogado.getSelectionModel().getSelectedItem());
        String numeroLicencia = txtLicencia.getText();

        String informacionVerificada = verificarInformacionPersona(nombre, cedula, telefono, correo, direccion, usuario,
                contrasenia, txtTipoUsuario, txtEspecializacionAbogado, numeroLicencia);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }

        if (personaRepo.findById(cedula).orElse(null) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con esa cedula",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByNombreDeUsuario(usuario) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese nombre de usuario",
                    AlertType.WARNING);
            return;
        }

        if (personaRepo.findByEmail(correo) != null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Ya existe una persona con ese correo",
                    AlertType.WARNING);
            return;
        }
        try {
            crearPersonaPorTipo(nombre, cedula, telefono, correo, direccion, usuario, contrasenia, txtTipoUsuario,
                    txtEspecializacionAbogado, numeroLicencia);
            mostrarMensaje("Gestion de personas", "Informacion", "Persona creada con exito",
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
            String usuario, String contrasenia, String txtTipoUsuario, String txtEspecializacionAbogado,
            String numeroLicencia) {
        if (txtTipoUsuario.equalsIgnoreCase(ABOGADO)) {
            Abogado abogado = new Abogado(nombre, cedula, telefono, correo, direccion, usuario,
                    contrasenia,
                    firmaAbogadosRepo.findById(1).orElse(null), numeroLicencia, LocalDate.now(),
                    Especializacion.valueOf(txtEspecializacionAbogado));
            abogadoRepo.save(abogado);
        } else if (txtTipoUsuario.equalsIgnoreCase(ASESOR)) {
            Asesor asesor = new Asesor(nombre, cedula, telefono, correo, direccion, usuario,
                    contrasenia,
                    firmaAbogadosRepo.findById(1).orElse(null));
            asesorRepo.save(asesor);
        } else if (txtTipoUsuario.equalsIgnoreCase(CLIENTE)) {
            Cliente cliente = new Cliente(nombre, cedula, telefono, correo, direccion, usuario,
                    contrasenia,
                    firmaAbogadosRepo.findById(1).orElse(null), null);
            clienteRepo.save(cliente);
        }
        refrescarTablaPersonas();
        limpiarInterfazPersona();

    }

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private String verificarInformacionPersona(String nombre, String cedula, String telefono, String correo,
            String direccion, String usuario, String contrasenia, String tipoUsuario2, String especializacionAbogado2,
            String numeroLicencia) {
        String mensaje = "";

        if (nombre.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el nombre de la persona \n";
        }
        if (cedula.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la cedula de la persona \n";
        }
        if (cedula.length() > 10) {
            mensaje += "Por favor ingrese una cedula valida (10 digitos maximo) \n";
        }
        if (telefono.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el telefono de la persona \n";
        }
        if (correo.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el correo de la persona \n";
        }
        if (direccion.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la direccion de la persona \n";
        }
        if (usuario.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el usuario de la persona \n";
        }
        if (contrasenia.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la contrasenia de la persona \n";
        }
        if (tipoUsuario2.equalsIgnoreCase("null")) {
            mensaje += "Por favor ingrese el tipo de usuario de la persona \n";
        }
        if (tipoUsuario2.equalsIgnoreCase(ABOGADO)) {
            mensaje += validarInformacionAbogado(especializacionAbogado2, numeroLicencia);
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

    private String validarInformacionAbogado(String especializacionAbogado2, String numeroLicencia) {
        String mensaje = "";
        if (especializacionAbogado2.equalsIgnoreCase("null")) {
            mensaje += "Por favor ingrese la especializacion del abogado \n";
        }
        if (numeroLicencia.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el numero de licencia del abogado \n";
        }
        if (!isNumeric(numeroLicencia)) {
            mensaje += "Por favor ingrese un numero de licencia valido \n";
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

        if (personaSeleccionada != null) {
            confirmacion = mostrarMensajeConfirmacion("Confirme que desea eliminar la persona seleccionada");
            if (confirmacion) {
                personaRepo.delete(personaSeleccionada);
                limpiarInterfazPersona();
                refrescarTablaPersonas();
                personaSeleccionada = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Persona eliminada con exito",
                        AlertType.INFORMATION);
            }

        }

        else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un vendedor para eliminar",
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inicializarTablaPersonas();

        refrescarTablaPersonas();

        inicializarBuscador();

        List<String> valoresTipoUsuario = new ArrayList<>();
        valoresTipoUsuario.add(ABOGADO);
        valoresTipoUsuario.add(ASESOR);
        valoresTipoUsuario.add(CLIENTE);
        ObservableList<String> listaTipoUsuario = FXCollections.observableArrayList(valoresTipoUsuario);
        tipoUsuario.getItems().clear();
        tipoUsuario.setItems(listaTipoUsuario);

        List<String> valoresEspecializacionAbogado = new ArrayList<>();
        valoresEspecializacionAbogado.add("CIVIL");
        valoresEspecializacionAbogado.add("PENAL");
        valoresEspecializacionAbogado.add("LABORAL");
        valoresEspecializacionAbogado.add("FAMILIAR");
        valoresEspecializacionAbogado.add("COMERCIAL");
        ObservableList<String> listaEspecializacionAbogado = FXCollections
                .observableArrayList(valoresEspecializacionAbogado);
        especializacionAbogado.getItems().clear();
        especializacionAbogado.setItems(listaEspecializacionAbogado);

        labelEspecializacion.setVisible(false);
        labelNumeroLicencia.setVisible(false);
        especializacionAbogado.setVisible(false);
        txtLicencia.setVisible(false);

        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            personaSeleccionada = newSelection;

            mostrarInformacionPersona(personaSeleccionada);

            if (personaSeleccionada == null) {
                limpiarInterfazPersona();
            }

        });

        tipoUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (tipoUsuario.getSelectionModel().getSelectedItem().equals(ABOGADO)) {
                    labelEspecializacion.setVisible(true);
                    labelNumeroLicencia.setVisible(true);
                    especializacionAbogado.setVisible(true);
                    txtLicencia.setVisible(true);
                } else {
                    labelEspecializacion.setVisible(false);
                    labelNumeroLicencia.setVisible(false);
                    especializacionAbogado.setVisible(false);
                    txtLicencia.setVisible(false);
                }
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

        listaProductosSorteados.comparatorProperty().bind(tablaPersonas.comparatorProperty());

        tablaPersonas.setItems(listaProductosSorteados);
    }

    private void inicializarTablaPersonas() {
        this.columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.columnaCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        this.columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        this.columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreDeUsuario"));
        this.columnaContrasenia.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        this.columnaTipo.setCellValueFactory(cellData -> {
            Persona persona = cellData.getValue();
            if (persona instanceof Abogado) {
                return new SimpleStringProperty(ABOGADO);
            } else if (persona instanceof Asesor) {
                return new SimpleStringProperty(ASESOR);
            } else if (persona instanceof Cliente) {
                return new SimpleStringProperty(CLIENTE);
            } else {
                return new SimpleStringProperty("Desconocido");
            }
        });
    }

    private void refrescarTablaPersonas() {
        listaPersonasObservable.clear();

        for (Persona persona : personaRepo.findAll()) {
            if (!(persona instanceof Admin)) {
                listaPersonasObservable.add(persona);
            }
        }
    }

    private void limpiarInterfazPersona() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
        txtLicencia.setText("");
        tipoUsuario.getSelectionModel().select(null);
        especializacionAbogado.getSelectionModel().select(null);
        labelEspecializacion.setVisible(false);
        labelNumeroLicencia.setVisible(false);
        especializacionAbogado.setVisible(false);
        txtLicencia.setVisible(false);

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
            if (personaSeleccionada instanceof Abogado) {
                tipoUsuario.getSelectionModel().select(ABOGADO);
                labelEspecializacion.setVisible(true);
                labelNumeroLicencia.setVisible(true);
                especializacionAbogado.setVisible(true);
                txtLicencia.setVisible(true);
                txtLicencia.setText(((Abogado) personaSeleccionada).getNumeroDeLicencia());
                especializacionAbogado.getSelectionModel()
                        .select(((Abogado) personaSeleccionada).getEspecializacion().toString());
            } else if (personaSeleccionada instanceof Asesor) {
                tipoUsuario.getSelectionModel().select(ASESOR);
                labelEspecializacion.setVisible(false);
                labelNumeroLicencia.setVisible(false);
                especializacionAbogado.setVisible(false);
                txtLicencia.setVisible(false);
            } else if (personaSeleccionada instanceof Cliente) {
                tipoUsuario.getSelectionModel().select(CLIENTE);
                labelEspecializacion.setVisible(false);
                labelNumeroLicencia.setVisible(false);
                especializacionAbogado.setVisible(false);
                txtLicencia.setVisible(false);
            }
        }
    }

}
