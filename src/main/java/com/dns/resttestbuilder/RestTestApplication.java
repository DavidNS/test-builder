package com.dns.resttestbuilder;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.gui.MainController;
import com.dns.resttestbuilder.gui.WindowBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Controlador de contenedores principales. Se encarga de iniciar
 * spring-boot+javafx y controlar que el final de la ejecución del programa sea
 * lo más limpio posible
 * 
 * @author David Nowakowski Stachyra
 *
 */
public class RestTestApplication extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {
		// Spring Boot Context
		this.context = new SpringApplicationBuilder(RestTestBuilderBoot.class).run();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Java FX view
		MainController mainController=context.getBean(MainController.class);
		mainController.setMainStage(primaryStage);
		WindowBuilder wb = context.getBean(WindowBuilder.class);
		wb.buildMainWindow(primaryStage, "fxml/Main.fxml");
	}

	@Override
	public void stop() throws Exception {
		// TODO also shall stop async-threads or wait until they finish ->
		// AsyncConfiguration.class -> beans
		this.context.close(); // Stop Spring context
		Platform.exit(); // Stop Java FX context
	}
}
