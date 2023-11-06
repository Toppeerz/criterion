package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Especializacion;
import co.edu.uniquindio.criterion.model.EstadoCaso;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.AbogadoRepo;
import co.edu.uniquindio.criterion.repositories.CasoRepo;
import co.edu.uniquindio.criterion.repositories.ClienteRepo;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

@Component
public class AsesorController implements Initializable{

    @Autowired
    private SceneController sceneController;
    private ObservableList<Cliente> listaClienteObservable = FXCollections.observableArrayList();
    private ObservableList<Abogado> listaAbogadoObservable = FXCollections.observableArrayList();
    private ObservableList<Caso> listaCasosObservable = FXCollections.observableArrayList();
    private Cliente clienteSeleccionado;
    private Abogado abogadoSeleccionado;
    private Caso casoSeleccionado;
    private static final String VALIDACION_DATOS = "Validacion de datos";
    @Autowired
    private CasoRepo casoRepo;
    private Persona personaLogueada;
    @Autowired
    private AbogadoRepo abogadoRepo;
    @Autowired
    private ClienteRepo clienteRepo;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaCedula;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaCorreo;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaDireccion;

    @FXML
    private TableColumn<Abogado, Especializacion> abogadoColumnaEspecializacion;

    @FXML
    private TableColumn<Abogado, LocalDate> abogadoColumnaFechaInicio;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaLicencia;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaNombre;

    @FXML
    private TableColumn<Abogado, String> abogadoColumnaTelefono;

    @FXML
    private Button btnAbrirVistaClientes;

    @FXML
    private Button btnActualizarCaso;

    @FXML
    private Button btnActualizarInfo;

    @FXML
    private Button btnCancelarCaso;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnCrearCaso;

    @FXML
    private TextField buscadorAbogados;

    @FXML
    private TextField buscadorCasos;

    @FXML
    private TextField buscadorClientes;

    @FXML
    private TableColumn<Caso, String> casoColumnaDescripcion;

    @FXML
    private TableColumn<Caso, Especializacion> casoColumnaEspecializacion;

    @FXML
    private TableColumn<Caso, EstadoCaso> casoColumnaEstado;

    @FXML
    private TableColumn<Caso, LocalDate> casoColumnaFechaCierre;

    @FXML
    private TableColumn<Caso, LocalDate> casoColumnaFechaInicio;

    @FXML
    private TableColumn<Caso, Integer> casoColumnaId;

    @FXML
    private TableColumn<Caso, String> casoColumnaNombreAbogado;

    @FXML
    private TableColumn<Caso, String> casoColumnaNombreCliente;

    @FXML
    private TableColumn<Cliente, String> clienteColumnaCedula;

    @FXML
    private TableColumn<Cliente, String> clienteColumnaCorreo;

    @FXML
    private TableColumn<Cliente, String> clienteColumnaDireccion;

    @FXML
    private TableColumn<Cliente, String> clienteColumnaNombre;

    @FXML
    private TableColumn<Cliente, String> clienteColumnaTelefono;

    @FXML
    private DatePicker fechaCierre;

    @FXML
    private TableView<Abogado> tablaAbogados;

    @FXML
    private TableView<Caso> tablaCasos;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TextField txtDescripcion;

    @FXML
    void abrirDetallesCaso(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY &&  (event.getClickCount() == 2)) {
                casoSeleccionado = tablaCasos.getSelectionModel().getSelectedItem();
                if (casoSeleccionado != null) {
                    sceneController.cambiarADetallesCaso(event, casoSeleccionado);
                }
            
        }
    }

    @FXML
    void abrirVistaClientes(ActionEvent event) {
        sceneController.cambiarAClientesCrud(event, personaLogueada);
    }

    @FXML
    void actualizarCaso(ActionEvent event) {
        actualizarCaso();

    }

    private void actualizarCaso() {
        String descripcion = txtDescripcion.getText();
        String fechaCierreString = this.fechaCierre.getValue() != null ? this.fechaCierre.getValue().toString() : "";

        String informacionVerificada = verificarInformacionCaso(descripcion,fechaCierreString);

        if(casoSeleccionado == null){
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un caso para actualizar", AlertType.WARNING);
            return;
        }

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }
        if(!clienteSeleccionado.getCedula().equalsIgnoreCase(casoSeleccionado.getCliente().getCedula())){
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No se puede cambiar el cliente de un caso", AlertType.WARNING);
            return;
        }
        try {
            casoSeleccionado.setDescripcion(descripcion);
            casoSeleccionado.setFechaLimite(this.fechaCierre.getValue());
            casoSeleccionado.setAbogado(abogadoSeleccionado);
            casoSeleccionado.setEspecializacion(abogadoSeleccionado.getEspecializacion());
            casoRepo.save(casoSeleccionado);
            refrescarTablaAbogados();
            refrescarTablaCasos();
            refrescarTablaClientes();
            limpiarInterfazCaso();
            mostrarMensaje("Gestion de casos", "Informacion", "Caso actualizado con exito",
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
    void actualizarInfo(ActionEvent event) {
        sceneController.cambiarAActualizarInfo(event);
    }

    @FXML
    void cancelarCaso(ActionEvent event) {
        cancelarCaso();
    }

    private void cancelarCaso() {
        if (casoSeleccionado != null) {
            if (casoSeleccionado.getEstadoCaso() != EstadoCaso.CERRADO) {
                casoSeleccionado.setEstadoCaso(EstadoCaso.CANCELADO);
                casoRepo.save(casoSeleccionado);
                refrescarTablaCasos();
                casoSeleccionado = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Caso cancelado con exito",
                        AlertType.INFORMATION);
            } else {
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS,
                        "No se puede cancelar un caso que ya fue cerrado",
                        AlertType.WARNING);
            }
        } else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un caso para cancelar",
                    AlertType.WARNING);
        }
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        sceneController.cambiarALogin(event);
    }

    @FXML
    void crearCaso(ActionEvent event) {
        crearCaso();
    }

    private void crearCaso() {
        String descripcion = txtDescripcion.getText();
        String fechaCierreString = this.fechaCierre.getValue() != null ? this.fechaCierre.getValue().toString() : "";

        String informacionVerificada = verificarInformacionCaso(descripcion,fechaCierreString);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }
        try {
            Caso caso = new Caso(descripcion, LocalDate.now(), this.fechaCierre.getValue(), abogadoSeleccionado.getEspecializacion(), EstadoCaso.ABIERTO, abogadoSeleccionado, (Asesor) personaLogueada, clienteSeleccionado, sceneController.getFirmaAbogados());
            casoRepo.save(caso);
            refrescarTablaAbogados();
            refrescarTablaCasos();
            refrescarTablaClientes();
            limpiarInterfazCaso();
            mostrarMensaje("Gestion de casos", "Informacion", "Caso creado con exito",
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

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private String verificarInformacionCaso(String descripcion, String fechaCierre) {
        String mensaje = "";

        if (descripcion.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la descripcion del caso \n";
        }
        if (fechaCierre.equalsIgnoreCase("")) {
            mensaje += "Por favor seleccione la fecha de cierre maxima del caso \n";
        }
        if (clienteSeleccionado == null) {
            mensaje += "Por favor seleccione el cliente que solicita el caso \n";
        }
        if (abogadoSeleccionado == null) {
            mensaje += "Por favor ingrese abogado al que se le asignara el caso \n";
        }
        
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }
        return mensaje;
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personaLogueada = sceneController.getPersonaLogueada();
        inicializarTablaClientes();
        inicializarTablaAbogados();
        inicializarTablaCasos();
    }

    private void inicializarTablaAbogados() {
        this.abogadoColumnaCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        this.abogadoColumnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.abogadoColumnaCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.abogadoColumnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        this.abogadoColumnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.abogadoColumnaEspecializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        this.abogadoColumnaFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaDeInicioDeFirma"));
        this.abogadoColumnaLicencia.setCellValueFactory(new PropertyValueFactory<>("numeroDeLicencia"));
        
        refrescarTablaAbogados();

        FilteredList<Abogado> listaAbogadosFiltrados = new FilteredList<>(listaAbogadoObservable, b -> true);
        buscadorAbogados.textProperty().addListener((obs, oldValue, newValue) -> listaAbogadosFiltrados.setPredicate(abogado -> {

            if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (abogado.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getEspecializacion().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (abogado.getFechaDeInicioDeFirma().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else {
                return (abogado.getNumeroDeLicencia().toLowerCase().contains(lowerCaseFilter));
            }
        }));

        SortedList<Abogado> listaAbogadosSorteados = new SortedList<>(listaAbogadosFiltrados);

        listaAbogadosSorteados.comparatorProperty().bind(tablaAbogados.comparatorProperty());

        tablaAbogados.setItems(listaAbogadosSorteados);

        tablaAbogados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->  abogadoSeleccionado = newSelection);
    }

    private void refrescarTablaAbogados() {
        listaAbogadoObservable.clear();
        listaAbogadoObservable.addAll(abogadoRepo.findAll());
    }

    private void inicializarTablaCasos() {
        this.casoColumnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.casoColumnaEspecializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        this.casoColumnaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoCaso"));
        this.casoColumnaFechaCierre.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        this.casoColumnaFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaApertura"));
        this.casoColumnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.casoColumnaNombreAbogado.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAbogado().getNombre()));
        this.casoColumnaNombreCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        
        refrescarTablaCasos();

        inicializarBuscadorCasos();

        tablaCasos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->  {
        casoSeleccionado = newSelection;
        mostarInformacionCaso(casoSeleccionado);
        if (casoSeleccionado == null) {
            limpiarInterfazCaso();
        }
        });
    }

    private void inicializarBuscadorCasos() {
        FilteredList<Caso> listaCasosFiltrados = new FilteredList<>(listaCasosObservable, b -> true);
        buscadorCasos.textProperty().addListener((obs, oldValue, newValue) -> listaCasosFiltrados.setPredicate(caso -> {

            if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (String.valueOf(caso.getId()).toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getCliente().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getAsesorCaso().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getEstadoCaso().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getFechaApertura().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (caso.getFechaLimite().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else {
                return (caso.getEspecializacion().toString().toLowerCase().contains(lowerCaseFilter));
            }
        }));

        SortedList<Caso> listaCasosSorteados = new SortedList<>(listaCasosFiltrados);

        listaCasosSorteados.comparatorProperty().bind(tablaCasos.comparatorProperty());

        tablaCasos.setItems(listaCasosSorteados);
    }

    private void limpiarInterfazCaso() {
        txtDescripcion.setText("");
        fechaCierre.setValue(null);
        tablaAbogados.getSelectionModel().clearSelection();
        tablaClientes.getSelectionModel().clearSelection();
    }

    private void mostarInformacionCaso(Caso casoSeleccionado) {
        if (casoSeleccionado != null) {
            txtDescripcion.setText(casoSeleccionado.getDescripcion());
            fechaCierre.setValue(casoSeleccionado.getFechaLimite());
            tablaAbogados.getSelectionModel().select(casoSeleccionado.getAbogado());
            tablaClientes.getSelectionModel().select(casoSeleccionado.getCliente());
        }
    }

    private void refrescarTablaCasos() {
        listaCasosObservable.clear();
        listaCasosObservable.addAll(casoRepo.findAllByAsesorCaso_Cedula(personaLogueada.getCedula()));
    }

    private void inicializarTablaClientes() {
        this.clienteColumnaCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        this.clienteColumnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.clienteColumnaCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.clienteColumnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        this.clienteColumnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        
        refrescarTablaClientes();

        FilteredList<Cliente> listaClientesFiltrados = new FilteredList<>(listaClienteObservable, b -> true);
        buscadorClientes.textProperty().addListener((obs, oldValue, newValue) -> listaClientesFiltrados.setPredicate(cliente -> {

            if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (cliente.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (cliente.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (cliente.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else if (cliente.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } 
            else { return cliente.getTelefono().toLowerCase().contains(lowerCaseFilter);
            }
        }));

        SortedList<Cliente> listaClientesSorteados = new SortedList<>(listaClientesFiltrados);

        listaClientesSorteados.comparatorProperty().bind(tablaClientes.comparatorProperty());

        tablaClientes.setItems(listaClientesSorteados);

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->  clienteSeleccionado = newSelection);
    }

    private void refrescarTablaClientes() {
        listaClienteObservable.clear();
        listaClienteObservable.addAll(clienteRepo.findAll());
    }

}
