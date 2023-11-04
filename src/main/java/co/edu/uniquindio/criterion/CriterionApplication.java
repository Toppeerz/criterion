package co.edu.uniquindio.criterion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SpringBootApplication
public class CriterionApplication extends Application {
	public static final ConfigurableApplicationContext context = SpringApplication.run(CriterionApplication.class);


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		primaryStage.getIcons().add(new Image(CriterionApplication.class.getResourceAsStream("views/images/logo.png")));
		primaryStage.setTitle("Criterion");
		FXMLLoader loader = new FXMLLoader(CriterionApplication.class.getResource("views/Login.fxml"));
		loader.setControllerFactory(context::getBean);
		BorderPane rootLayout = (BorderPane)loader.load();
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	@Override
	public void stop() {
    System.exit(0);
}

}
