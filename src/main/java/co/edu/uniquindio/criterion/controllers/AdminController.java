package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Admin;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.repositories.PersonaRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class AdminController implements Initializable {

    SceneController sceneController = new SceneController();
    ObservableList<Persona> listaPersonasObservable = FXCollections.observableArrayList();
    @Autowired
    PersonaRepo personaRepo;

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
    void actualizar(ActionEvent event) {

    }

    @FXML
    void cerrarSesion(ActionEvent event) {

        sceneController.cambiarALogin(event);
        
    }

    @FXML
    void crear(ActionEvent event) {

    }

    @FXML
    void eliminar(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
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
            return new SimpleStringProperty("Abogado");
        } else if (persona instanceof Asesor) {
            return new SimpleStringProperty("Asesor");
        } else if (persona instanceof Admin) {
            return new SimpleStringProperty("Admin");
        } else {
            return new SimpleStringProperty("Desconocido");
        }
        });


        tablaPersonas.getItems().clear();

        listaPersonasObservable.addAll(personaRepo.findAll());
		tablaPersonas.setItems(listaPersonasObservable);


        FilteredList<Persona> listaPersonasFiltrada = new FilteredList<>(listaPersonasObservable, b -> true);
        buscador.textProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null || newValue.isEmpty()) {
				tablaPersonas.setItems(listaPersonasObservable);
			} else {

				listaPersonasFiltrada.setPredicate(persona -> {

					String lowerCaseFilter = newValue.toLowerCase();

					return persona.getNombre().toLowerCase().indexOf(lowerCaseFilter) > -1;

				});
				SortedList<Persona> listaProductosSorteados = new SortedList<>(listaPersonasFiltrada);

				tablaPersonas.setItems(listaProductosSorteados);
            }
        });
    }

}
