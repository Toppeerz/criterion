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
import co.edu.uniquindio.criterion.repositories.FacturaRepo;
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
public class ClienteController implements Initializable{

    @Autowired
    private SceneController sceneController;
    private ObservableList<Caso> listaCasosObservable = FXCollections.observableArrayList();
    private Caso casoSeleccionado;
    private static final String VALIDACION_DATOS = "Validacion de datos";
    @Autowired
    private CasoRepo casoRepo;
    private Persona personaLogueada;
    @Autowired
    private FacturaRepo facturaRepo;

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
    private TableColumn<Caso, String> columnaAbogado;

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

    @FXML
    private Button btnPagar;

    @FXML
    void pagar(ActionEvent event) {
        if(casoSeleccionado == null){
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Debe seleccionar un caso para poder pagarlo",
                            AlertType.WARNING);
            return;
        }
        if(casoSeleccionado.getEstadoCaso() !=  EstadoCaso.CERRADO){
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "El caso debe estar cerrado para poder ser pagado",
                            AlertType.WARNING);
            return;
        }
        if(Boolean.TRUE.equals(casoSeleccionado.getFactura().getEstadoDePago())){
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "El caso ya ha sido pagado",
                            AlertType.WARNING);
            return;
        }
        casoSeleccionado.getFactura().setEstadoDePago(true);
        facturaRepo.save(casoSeleccionado.getFactura());
        refrescarTablaCasos();
        mostrarMensaje("Gestion de pagos", "Informacion", "Pago realizado con exito",
                    AlertType.INFORMATION);
    }

    private void mostrarMensaje(String titulo, String header, String contenido, AlertType information) {
        Alert alert = new Alert(information);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

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
    void cerrarSesion(ActionEvent event) {
        sceneController.cambiarALogin(event);
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
            } else if (caso.getAbogado().getNombre().toLowerCase().contains(lowerCaseFilter)) {
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
        this.columnaAbogado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAbogado().getNombre()));
        this.columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.columnaEspecializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        this.columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estadoCaso"));
        this.columnaFechaCierre.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        this.columnaFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaApertura"));
        this.columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void refrescarTablaCasos() {
        listaCasosObservable.clear();
        listaCasosObservable.addAll(casoRepo.findAllByCliente_Cedula(personaLogueada.getCedula()));

        
    }


}
