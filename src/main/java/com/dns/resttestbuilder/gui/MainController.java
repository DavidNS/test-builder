package com.dns.resttestbuilder.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.gui.user.UserController;
import com.dns.resttestbuilder.gui.workspace.WorkspaceController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;

@Component
@Getter
@Scope("singleton")
public class MainController {

	Stage primaryStage;

	@Autowired
	WindowBuilder wb;

	@Autowired
	UserController userController;

	@Autowired
	WorkspaceController workspaceController;

	@FXML
	AnchorPane content;

	Node userWindow;

	AnchorPane worspaceWindow;

	@FXML
	public void initialize() {
		userWindow = wb.buildWindow(userController, "fxml/User.fxml");
		worspaceWindow = wb.buildWindow(workspaceController, "fxml/Workspace.fxml");
		showUserWindow();
	}

	public void showUserWindow() {
		content.getChildren().setAll(userWindow);
		resize();
	}

	private void resize() {
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
	}

	public void showWorkspaceWindow(User user) {
		workspaceController.updateUser(user);
		content.getChildren().setAll(worspaceWindow);
		resize();

	}

	public void setMainStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
