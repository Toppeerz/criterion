package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import co.edu.uniquindio.criterion.model.Caso;

import co.edu.uniquindio.criterion.model.Especializacion;
import co.edu.uniquindio.criterion.model.EstadoCaso;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.CasoRepo;


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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


@Component
public class AbogadoController implements Initializable {

    @Autowired
    private SceneController sceneController;
    private ObservableList<Caso> listaCasosObservable = FXCollections.observableArrayList();
    private Caso casoSeleccionado;
    @Autowired
    CasoRepo casoRepo;
    private static final String VALIDACION_DATOS = "Validacion de datos";

    @FXML
    private Button btnActualizarInfo;

    @FXML
    private Button btnCerrarCaso;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnResolverCaso;

    @FXML
    private TextField buscador;

    @FXML
    private TableColumn<Caso, String> columnaAsesor;

    @FXML
    private TableColumn<Caso, String> columnaCliente;

    @FXML
    private TableColumn<Caso, String> columnaDescripcion;

    @FXML
    private TableColumn<Caso, Especializacion> columnaEspecializacion;

    @FXML
    private TableColumn<Caso, EstadoCaso> columnaEstado;

    @FXML
    private TableColumn<Caso, LocalDate> columnaFechaCierre;

    @FXML
    private TableColumn<Caso, LocalDate> columnaFechaInicio;

    @FXML
    private TableColumn<Caso, Integer> columnaID;

    @FXML
    private TableView<Caso> tablaCasos;
    private Persona personaLogueada;

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
    void actualizarInfo(ActionEvent event) {
        sceneController.cambiarAActualizarInfo(event);
    }

    @FXML
    void cerrarCaso(ActionEvent event) {
        cerrarCaso();
    }

    private void cerrarCaso() {
        if (casoSeleccionado != null) {
            if (casoSeleccionado.getEstadoCaso() == EstadoCaso.EN_PROCESO) {
                casoSeleccionado.setEstadoCaso(EstadoCaso.CERRADO);
                casoRepo.save(casoSeleccionado);
                refrescarTablaCasos();
                casoSeleccionado = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Caso cerrado con exito",
                        AlertType.INFORMATION);
            } else {
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS,
                        "El caso no ha sido iniciado a resolver o ya fue cerrado/cancelado",
                        AlertType.WARNING);
            }
        } else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un caso para cerrar",
                    AlertType.WARNING);
        }
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        sceneController.cambiarALogin(event);
    }

    @FXML
    void resolverCaso(ActionEvent event) {
        resolverCaso();
    }

    private void resolverCaso() {
        if (casoSeleccionado != null) {
            if (casoSeleccionado.getEstadoCaso() == EstadoCaso.ABIERTO) {
                casoSeleccionado.setEstadoCaso(EstadoCaso.EN_PROCESO);
                casoRepo.save(casoSeleccionado);
                refrescarTablaCasos();
                casoSeleccionado = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Caso iniciado a resolver con exito",
                        AlertType.INFORMATION);
            } else {
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "El caso ya esta siendo resuelto o ya fue cerrado y/o cancelado",
                        AlertType.WARNING);
            }
        } else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un caso para iniciar a resolver",
                    AlertType.WARNING);
        }
    }

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personaLogueada = sceneController.getPersonaLogueada();
        inicializarTablaCasos();
        refrescarTablaCasos();
        inicializarBuscador();
        tablaCasos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->  casoSeleccionado = newSelection);

    }

    private void inicializarBuscador() {
        FilteredList<Caso> listaCasosFiltrados = new FilteredList<>(listaCasosObservable, b -> true);
        buscador.textProperty().addListener((obs, oldValue, newValue) -> listaCasosFiltrados.setPredicate(caso -> {

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

    private void inicializarTablaCasos() {
        this.columnaAsesor.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAsesorCaso().getNombre()));
        this.columnaCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        this.columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.columnaEspecializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        this.columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoCaso"));
        this.columnaFechaCierre.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        this.columnaFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaApertura"));
        this.columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void refrescarTablaCasos() {
        listaCasosObservable.clear();

        for (Caso caso : casoRepo.findAllByAbogado_Cedula(personaLogueada.getCedula())) {

            listaCasosObservable.add(caso);

        }
    }

}
