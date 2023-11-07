package co.edu.uniquindio.criterion.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.criterion.model.Asesor;
import co.edu.uniquindio.criterion.model.Caso;
import co.edu.uniquindio.criterion.model.EstadoCaso;
import co.edu.uniquindio.criterion.model.Reunion;
import co.edu.uniquindio.criterion.repositories.ReunionRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

@Component
public class DetallesReunionController implements Initializable {

    @Autowired
    private SceneController sceneController;
    private Caso casoSeleccionado;
    private Reunion reunionSeleccionada;
    @Autowired
    private ReunionRepo reunionRepo;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @FXML
    private Button btnVolver;

    @FXML
    private Label labelAsuntoReunionCSS;

    @FXML
    private Label labelFechaReunionCSS;

    @FXML
    private Label labelNotasReunionCSS;

    @FXML
    private Label labelUbicacionReunionCSS;

    @FXML
    private TextArea txtNotasReunion;

    @FXML
    private Button btnEditar;

    @FXML
    void editar(ActionEvent event) {
        if(!txtNotasReunion.isEditable()){
            txtNotasReunion.setEditable(true);
        }else{
            reunionSeleccionada.setNotas(txtNotasReunion.getText());
            reunionRepo.save(reunionSeleccionada);
            txtNotasReunion.setEditable(false);
        }
    }

    @FXML
    void volver(ActionEvent event) {
        sceneController.cambiarADetallesCaso(event, casoSeleccionado);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        casoSeleccionado=sceneController.getCasoSeleccionado();
        reunionSeleccionada=sceneController.getReunionSeleccionada();
        labelAsuntoReunionCSS.setText("ASUNTO: "+reunionSeleccionada.getAsunto());
        labelFechaReunionCSS.setText("FECHA: "+reunionSeleccionada.getFecha().format(dateTimeFormatter));
        labelNotasReunionCSS.setText("NOTAS: ");
        labelUbicacionReunionCSS.setText("UBICACION: "+reunionSeleccionada.getUbicacion());
        txtNotasReunion.setEditable(false);
        txtNotasReunion.setText(reunionSeleccionada.getNotas());
        if(sceneController.getPersonaLogueada() instanceof Asesor){
        if(casoSeleccionado.getEstadoCaso() == EstadoCaso.CERRADO){
            btnEditar.setDisable(true);
            btnEditar.setVisible(false);
        }else{
            btnEditar.setDisable(false);
            btnEditar.setVisible(true);
        }}
        else{
            btnEditar.setDisable(true);
            btnEditar.setVisible(false);
        }
    }

}
