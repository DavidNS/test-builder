package com.dns.resttestbuilder.gui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

@Component
public class WindowBuilder {

	@Autowired
	private ConfigurableApplicationContext context;

	public  void buildMainWindow(Stage primaryStage, String fxmlWiew) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(context.getResource(fxmlWiew).getURL());
			fxmlLoader.setControllerFactory(context::getBean);
			fxmlLoader.setController(fxmlLoader.getController());
			fxmlLoader.setLocation(context.getResource(fxmlWiew).getURL());
			primaryStage.setTitle("Test Builder");
			primaryStage.setScene(new Scene(fxmlLoader.load()));
			primaryStage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public <C,T extends Node> T buildWindow(C controller, String fxmlView) {
		T rootNode = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(context.getResource(fxmlView).getURL());
			fxmlLoader.setController(controller);
			rootNode = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootNode;
	}
	
	
	public <C> Node buildWindow(Class<C> controllerClass, String fxmlView) {
		Node node = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(context.getResource(fxmlView).getURL());
			C controller=context.getBean(controllerClass);
			fxmlLoader.setController(controller);
//			fxmlLoader.setRoot(controller);	
			node = fxmlLoader.load();
//			AnchorPane.setTopAnchor(node, 0.0);
//			AnchorPane.setRightAnchor(node, 0.0);
//			AnchorPane.setLeftAnchor(node, 0.0);
//			AnchorPane.setBottomAnchor(node, 0.0);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}

	public <T, C extends ListCell<T>> C buildListCellElement(Class<C> cellClass, String fxmlWiew) {
		C controller = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(context.getResource(fxmlWiew).getURL());
			controller = context.getBean(cellClass);
			fxmlLoader.setController(controller);
			fxmlLoader.setRoot(controller);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return controller;

	}
	
	
	public void createAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEIIIIIIIIIIIII");
		alert.setHeaderText("STOP PLEASE");
		alert.setContentText("You are doing shomething forbiden!");
		alert.showAndWait();
	}

}
