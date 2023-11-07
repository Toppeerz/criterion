package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.Documento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

@Component
public class DetallesDocumentoController implements Initializable{

    @Autowired
    private SceneController sceneController;
    private Caso casoSeleccionado;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private Button btnVolver;

    @FXML
    private Label labelContenidoDocumentoCSS;

    @FXML
    private Label labelFechaDocumentoCSS;

    @FXML
    private Label labelTituloDocumentoCSS;

    @FXML
    private TextArea labelContenidoTextoDocumento;

    @FXML
    void volver(ActionEvent event) {
        sceneController.cambiarADetallesCaso(event, casoSeleccionado);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Documento documentoSeleccionado;
        casoSeleccionado=sceneController.getCasoSeleccionado();
        documentoSeleccionado=sceneController.getDocumentoSeleccionado();
        labelContenidoTextoDocumento.setText(documentoSeleccionado.getContenido());
        labelFechaDocumentoCSS.setText("FECHA: "+documentoSeleccionado.getFecha().format(dateTimeFormatter));
        labelTituloDocumentoCSS.setText("TITULO: "+documentoSeleccionado.getTitulo());
    }

}
