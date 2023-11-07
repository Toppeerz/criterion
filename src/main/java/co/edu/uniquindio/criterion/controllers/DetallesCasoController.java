package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import co.edu.uniquindio.criterion.model.Abogado;
import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.Cliente;
import co.edu.uniquindio.criterion.model.Documento;
import co.edu.uniquindio.criterion.model.EstadoCaso;
import co.edu.uniquindio.criterion.model.Persona;
import co.edu.uniquindio.criterion.model.Reunion;
import co.edu.uniquindio.criterion.repositories.DocumentoRepo;
import co.edu.uniquindio.criterion.repositories.ReunionRepo;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

@Component
public class DetallesCasoController implements Initializable {

    private static final String VALIDACION_DATOS = "Validacion de datos";
    private static final String INFORMACION = "Informacion";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Persona personaLogueada;
    private Caso casoSeleccionado;
    @Autowired
    private SceneController sceneController;
    private ObservableList<Documento> listaDocumentosObservable = FXCollections.observableArrayList();
    private ObservableList<Reunion> listaReunionesObservable = FXCollections.observableArrayList();
    @Autowired
    private DocumentoRepo documentoRepo;
    @Autowired
    private ReunionRepo reunionRepo;
    private Documento documentoSeleccionado;
    private Reunion reunionSeleccionada;
    @FXML
    private Button btnActualizarDocumento;

    @FXML
    private Button btnActualizarReunion;

    @FXML
    private Button btnCrearDocumento;

    @FXML
    private Button btnCrearReunion;

    @FXML
    private Button btnEliminarDocumento;

    @FXML
    private Button btnEliminarReunion;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField buscadorDocumentos;

    @FXML
    private TextField buscadorReuniones;

    @FXML
    private TableColumn<Documento, String> documentoColumnaContenido;

    @FXML
    private TableColumn<Documento, String> documentoColumnaFecha;

    @FXML
    private TableColumn<Documento, Integer> documentoColumnaId;

    @FXML
    private TableColumn<Documento, String> documentoColumnaTitulo;

    @FXML
    private DatePicker fechaReunion;

    @FXML
    private Label labelAsuntoReunion;

    @FXML
    private Label labelContenidoDocumento;

    @FXML
    private Label labelDescripcion;

    @FXML
    private Label labelEspecializacion;

    @FXML
    private Label labelEstado;

    @FXML
    private Label labelFechaCierre;

    @FXML
    private Label labelFechaFacturacion;

    @FXML
    private Label labelFechaInicio;

    @FXML
    private Label labelFechaReunion;

    @FXML
    private Label labelId;

    @FXML
    private Label labelNombre1;

    @FXML
    private Label labelNombre2;

    @FXML
    private Label labelTituloDocumento;

    @FXML
    private Label labelTotal;

    @FXML
    private Label labelUbicacionReunion;

    @FXML
    private TableColumn<Reunion, String> reunionColumnaAsunto;

    @FXML
    private TableColumn<Reunion, String> reunionColumnaFecha;

    @FXML
    private TableColumn<Reunion, Integer> reunionColumnaId;

    @FXML
    private TableColumn<Reunion, String> reunionColumnaUbicacion;

    @FXML
    private TableView<Documento> tablaDocumentos;

    @FXML
    private TableView<Reunion> tablaReuniones;

    @FXML
    private TextField txtAsuntoReunion;

    @FXML
    private Label labelEstadoFactura;

    @FXML
    private TextField txtContenidoDocumento;

    @FXML
    private TextField txtTituloDocumento;

    @FXML
    private TextField txtUbicacionReunion;

    @FXML
    void actualizarDocumento(ActionEvent event) {
       actualizarDocumento();
    }

    private void actualizarDocumento() {
        String titulo = txtTituloDocumento.getText();
        String contenido = txtContenidoDocumento.getText();

        String informacionVerificada = verificarInformacionDocumentoActualizar(titulo, contenido);

        if (documentoSeleccionado == null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un documento para actualizar",
                    AlertType.WARNING);
            return;
        }

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }


        try {
            Documento documento = documentoSeleccionado;
            if(!titulo.equalsIgnoreCase("")){
                documento.setTitulo(titulo);
            }
            if(!contenido.equalsIgnoreCase("")){
                documento.setContenido(contenido);
            }
            documentoRepo.save(documento);
            limpiarCamposDocumento();
            refrescarTablaDocumentos();
            mostrarMensaje("Gestion de documentos", INFORMACION, "Documento actualizado con exito",
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

    private String verificarInformacionDocumentoActualizar(String titulo, String contenido) {
        String mensaje = "";

        if (titulo.equalsIgnoreCase("") && contenido.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese informacion en los campos que desea actualizar \n";
        }
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }

        return mensaje;
    }

    @FXML
    void actualizarReunion(ActionEvent event) {
        actualizarReunion();
    }

    private void actualizarReunion() {
        String ubicacion = txtUbicacionReunion.getText();
        String asunto = txtAsuntoReunion.getText();
        String fechaReunionString = fechaReunion.getValue() != null
                ? this.fechaReunion.getValue().format(dateTimeFormatter)
                : "";
        String informacionVerificada = verificarInformacionReunionActualizar(ubicacion, asunto, fechaReunionString);

        if (reunionSeleccionada == null) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado una reunion para actualizar",
                    AlertType.WARNING);
            return;
        }

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }


        try {
            Reunion reunion = reunionSeleccionada;
            if(!ubicacion.equalsIgnoreCase("")){
                reunion.setUbicacion(ubicacion);
            }
            if(!asunto.equalsIgnoreCase("")){
                reunion.setAsunto(asunto);
            }
            if(!fechaReunionString.equalsIgnoreCase("")){
                reunion.setFecha(LocalDateTime.of(fechaReunion.getValue(), LocalTime.now()));
            }
            reunionRepo.save(reunion);
            limpiarCamposReunion();
                refrescarTablaReuniones();
            mostrarMensaje("Gestion de reuniones", INFORMACION, "Reunion actualizada con exito",
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

    private String verificarInformacionReunionActualizar(String ubicacion, String asunto, String fechaReunionString) {
        String mensaje = "";

        if (ubicacion.equalsIgnoreCase("") && asunto.equalsIgnoreCase("") && fechaReunionString.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese informacion en los campos que desea actualizar \n";
        }
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }

        return mensaje;
    }
    


    @FXML
    void crearDocumento(ActionEvent event) {
        crearDocumento();
    }

    private void crearDocumento() {
        String titulo = txtTituloDocumento.getText();
        String contenido = txtContenidoDocumento.getText();

        String informacionVerificada = verificarInformacionDocumento(titulo, contenido);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }
        try {
            Documento documento = new Documento(titulo, LocalDateTime.now(), contenido, casoSeleccionado);
            documentoRepo.save(documento);
            limpiarCamposDocumento();
                refrescarTablaDocumentos();
            mostrarMensaje("Gestion de documentos", INFORMACION, "Documento creado con exito",
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

    private String verificarInformacionDocumento(String titulo, String contenido) {
        String mensaje = "";

        if (titulo.equalsIgnoreCase("")){
            mensaje += "Por favor ingrese el titulo del documento \n";
        }
        if (contenido.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el contenido del documento \n";
        }
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }
        return mensaje;
    }

    @FXML
    void crearReunion(ActionEvent event) {
        crearReunion();
    }

    private void crearReunion() {
        String ubicacion = txtUbicacionReunion.getText();
        String asunto = txtAsuntoReunion.getText();
        String fechaReunionString = fechaReunion.getValue() != null ? this.fechaReunion.getValue().toString() : "";

        String informacionVerificada = verificarInformacionReunion(ubicacion, asunto, fechaReunionString);

        if (!informacionVerificada.equalsIgnoreCase("Ok")) {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, informacionVerificada, AlertType.WARNING);
            return;
        }
        try {
            Reunion reunion = new Reunion(LocalDateTime.of(fechaReunion.getValue(), LocalTime.now()), ubicacion, asunto,
                    casoSeleccionado);
            reunionRepo.save(reunion);
            limpiarCamposReunion();
                refrescarTablaReuniones();
            mostrarMensaje("Gestion de reuniones", INFORMACION, "Reunion creada con exito",
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

    private String verificarInformacionReunion(String ubicacion, String asunto, String fechaReunionString) {
        String mensaje = "";

        if (ubicacion.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la ubicacion de la reunion \n";
        }
        if (asunto.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese el asunto de la reunion \n";
        }
        if (fechaReunionString.equalsIgnoreCase("")) {
            mensaje += "Por favor ingrese la fecha de la reunion \n";
        }
        if (mensaje.equalsIgnoreCase("")) {
            mensaje = "OK";
        }

        return mensaje;
    }

    @FXML
    void eliminarDocumento(ActionEvent event) {
        eliminarDocumento();
    }

    private void eliminarDocumento() {

        boolean confirmacion;

        if (documentoSeleccionado != null) {
            confirmacion = mostrarMensajeConfirmacion("Confirme que desea eliminar el documento seleccionado");
            if (confirmacion) {
                documentoRepo.delete(documentoSeleccionado);
                limpiarCamposDocumento();
                refrescarTablaDocumentos();
                documentoSeleccionado = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Documento eliminado con exito",
                        AlertType.INFORMATION);
            }

        }

        else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado un documento para eliminar",
                    AlertType.WARNING);
        }

    }

    private void limpiarCamposDocumento() {
        txtTituloDocumento.setText("");
        txtContenidoDocumento.setText("");
    }

    @FXML
    void eliminarReunion(ActionEvent event) {
        eliminarReunion();
    }

    private void eliminarReunion() {

        boolean confirmacion;

        if (reunionSeleccionada != null) {
            if (LocalDateTime.now().isAfter(reunionSeleccionada.getFecha())) {
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No puede eliminar una reunion que ya ha pasado",
                        AlertType.WARNING);
                return;
            }
            confirmacion = mostrarMensajeConfirmacion("Confirme que desea eliminar la reunion seleccionada");
            if (confirmacion) {
                reunionRepo.delete(reunionSeleccionada);
                limpiarCamposReunion();
                refrescarTablaReuniones();
                reunionSeleccionada = null;
                mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "Reunion eliminada con exito",
                        AlertType.INFORMATION);
            }

        }

        else {
            mostrarMensaje(VALIDACION_DATOS, VALIDACION_DATOS, "No ha seleccionado una reunion para eliminar",
                    AlertType.WARNING);
        }

    }

    private void limpiarCamposReunion() {
        txtAsuntoReunion.setText("");
        txtUbicacionReunion.setText("");
        fechaReunion.setValue(null);
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
    void abrirDetallesDocumento(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && (event.getClickCount() == 2)) {
            documentoSeleccionado = tablaDocumentos.getSelectionModel().getSelectedItem();
            if (documentoSeleccionado != null) {
                sceneController.cambiarADetallesDocumento(event, documentoSeleccionado);
            }

        }
    }

    @FXML
    void abrirDetallesReunion(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && (event.getClickCount() == 2)) {
            reunionSeleccionada = tablaReuniones.getSelectionModel().getSelectedItem();
            if (reunionSeleccionada != null) {
                sceneController.cambiarADetallesReunion(event, reunionSeleccionada);
            }

        }
    }

    @FXML
    void volver(ActionEvent event) {

        if (personaLogueada instanceof Abogado) {
            sceneController.cambiarAVistaAbogado(event, personaLogueada);
        } else if (personaLogueada instanceof Asesor) {
            sceneController.cambiarAVistaAsesor(event, personaLogueada);
        } else if (personaLogueada instanceof Cliente) {
            sceneController.cambiarAVistaCliente(event, personaLogueada);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personaLogueada = sceneController.getPersonaLogueada();
        casoSeleccionado = sceneController.getCasoSeleccionado();
        inicializarDetalles();
        inicializarTablaDocumentos();
        inicializarTablaReuniones();
    }

    private void inicializarTablaReuniones() {
        this.reunionColumnaAsunto.setCellValueFactory(new PropertyValueFactory<>("asunto"));
        this.reunionColumnaFecha.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getFecha().format(dateTimeFormatter)));
        this.reunionColumnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.reunionColumnaUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));

        refrescarTablaReuniones();

        FilteredList<Reunion> listaReunionesFiltradas = new FilteredList<>(listaReunionesObservable, b -> true);
        buscadorReuniones.textProperty()
                .addListener((obs, oldValue, newValue) -> listaReunionesFiltradas.setPredicate(reunion -> {

                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (reunion.getAsunto().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (reunion.getFecha().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(reunion.getId()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        return (reunion.getUbicacion().toLowerCase().contains(lowerCaseFilter));
                    }
                }));

        SortedList<Reunion> listaReunionesSorteadas = new SortedList<>(listaReunionesFiltradas);

        listaReunionesSorteadas.comparatorProperty().bind(tablaReuniones.comparatorProperty());

        tablaReuniones.setItems(listaReunionesSorteadas);

        tablaReuniones.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> reunionSeleccionada = newSelection);
    }

    private void refrescarTablaReuniones() {
        listaReunionesObservable.clear();
        listaReunionesObservable.addAll(reunionRepo.findAllByCaso_Id(casoSeleccionado.getId()));
    }

    private void inicializarTablaDocumentos() {
        this.documentoColumnaContenido.setCellValueFactory(new PropertyValueFactory<>("contenido"));
        this.documentoColumnaFecha.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getFecha().format(dateTimeFormatter)));
        this.documentoColumnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.documentoColumnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        refrescarTablaDocumentos();

        FilteredList<Documento> listaDocumentosFiltrados = new FilteredList<>(listaDocumentosObservable, b -> true);
        buscadorDocumentos.textProperty()
                .addListener((obs, oldValue, newValue) -> listaDocumentosFiltrados.setPredicate(documento -> {

                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (documento.getContenido().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (documento.getFecha().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(documento.getId()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        return (documento.getTitulo().toLowerCase().contains(lowerCaseFilter));
                    }
                }));

        SortedList<Documento> listaDocumentosSorteados = new SortedList<>(listaDocumentosFiltrados);

        listaDocumentosSorteados.comparatorProperty().bind(tablaDocumentos.comparatorProperty());

        tablaDocumentos.setItems(listaDocumentosSorteados);

        tablaDocumentos.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> documentoSeleccionado = newSelection);
    }

    private void refrescarTablaDocumentos() {
        listaDocumentosObservable.clear();
        listaDocumentosObservable.addAll(documentoRepo.findAllByCaso_Id(casoSeleccionado.getId()));
    }

    private void inicializarDetalles() {
        if (casoSeleccionado.getEstadoCaso() == EstadoCaso.CERRADO) {
            ocultarFunciones();
        } else {
            if (personaLogueada instanceof Abogado) {
                labelNombre1.setText("Cliente: " + casoSeleccionado.getCliente().getNombre());
                labelNombre2.setText("Asesor: " + casoSeleccionado.getAsesorCaso().getNombre());
                labelTituloDocumento.setVisible(false);
                txtTituloDocumento.setVisible(false);
                btnCrearDocumento.setVisible(false);
                btnActualizarDocumento.setVisible(false);
                btnEliminarDocumento.setVisible(false);
                txtContenidoDocumento.setVisible(false);
                labelContenidoDocumento.setVisible(false);
            } else if (personaLogueada instanceof Asesor) {
                labelNombre1.setText("Cliente: " + casoSeleccionado.getCliente().getNombre());
                labelNombre2.setText("Abogado: " + casoSeleccionado.getAbogado().getNombre());
                labelFechaReunion.setVisible(false);
                fechaReunion.setVisible(false);
                labelAsuntoReunion.setVisible(false);
                txtAsuntoReunion.setVisible(false);
                labelUbicacionReunion.setVisible(false);
                txtUbicacionReunion.setVisible(false);
                btnCrearReunion.setVisible(false);
                btnActualizarReunion.setVisible(false);
                btnEliminarReunion.setVisible(false);
            } else if (personaLogueada instanceof Cliente) {
                labelNombre1.setText("Asesor: " + casoSeleccionado.getAsesorCaso().getNombre());
                labelNombre2.setText("Abogado: " + casoSeleccionado.getAbogado().getNombre());
                ocultarFunciones();
            }
        }
        labelDescripcion.setText("Descripcion: " + casoSeleccionado.getDescripcion());
        labelEspecializacion.setText("Especializacion: " + casoSeleccionado.getEspecializacion());
        labelEstado.setText("Estado: " + casoSeleccionado.getEstadoCaso().toString());
        labelFechaInicio.setText("Fecha de inicio: " + casoSeleccionado.getFechaApertura().toString());
        labelFechaCierre.setText("Fecha de cierre: " + casoSeleccionado.getFechaLimite().toString());
        labelId.setText("Id: " + casoSeleccionado.getId());
        if (casoSeleccionado.getFactura() != null) {
            labelTotal.setText("Total: $" + casoSeleccionado.getFactura().getTotal());
            labelFechaFacturacion.setText("Fecha de facturacion: "
                    + casoSeleccionado.getFactura().getFechaFacturacion().format(dateTimeFormatter));
            if (Boolean.TRUE.equals(casoSeleccionado.getFactura().getEstadoDePago())) {
                labelEstadoFactura.setText("Estado de la factura: Pagado");
            } else {
                labelEstadoFactura.setText("Estado de la factura: No pagado");
            }
        } else {
            labelTotal.setText("Total: En curso");
            labelFechaFacturacion.setText("Fecha de facturacion: En curso");
            labelEstadoFactura.setText("Estado de la factura: En curso");
        }

    }

    private void ocultarFunciones() {
        labelTituloDocumento.setVisible(false);
            txtTituloDocumento.setVisible(false);
            btnCrearDocumento.setVisible(false);
            btnActualizarDocumento.setVisible(false);
            btnEliminarDocumento.setVisible(false);
            txtContenidoDocumento.setVisible(false);
            labelContenidoDocumento.setVisible(false);
            labelFechaReunion.setVisible(false);
            fechaReunion.setVisible(false);
            labelAsuntoReunion.setVisible(false);
            txtAsuntoReunion.setVisible(false);
            labelUbicacionReunion.setVisible(false);
            txtUbicacionReunion.setVisible(false);
            btnCrearReunion.setVisible(false);
            btnActualizarReunion.setVisible(false);
            btnEliminarReunion.setVisible(false);
    }

}
