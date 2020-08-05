package com.dns.resttestbuilder.gui.workspace.leftTab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.gui.WindowBuilder;
import com.dns.resttestbuilder.gui.workspace.WorkspaceController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;

@Component
@Scope("prototype")
public class LeftTabCell extends ListCell<LeftTabKind> {

	@Autowired
	WorkspaceController mainWindow;

	@Autowired
	WindowBuilder wb;

	@FXML
	Button tabCellButton;

	@FXML
	public void initialize() {

	}

	@Override
	protected void updateItem(LeftTabKind item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {
			setContent(item);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}

	private void setContent(LeftTabKind item) {
		tabCellButton.setOnAction((e) -> {
			Node node=wb.buildWindow(item.getClassController(), item.getFxmlView());
			mainWindow.getLeftTabContent().getChildren()
					.setAll(node);

		});
		tabCellButton.setText(item.getText());
	}

}
