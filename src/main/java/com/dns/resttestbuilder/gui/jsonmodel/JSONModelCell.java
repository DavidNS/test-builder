package com.dns.resttestbuilder.gui.jsonmodel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.JSONModel;

import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

@Component
@Scope("prototype")
public class JSONModelCell  extends ListCell<JSONModel>{

	@FXML
	TextField jsonModelNameField;
	
	
	
	@FXML
	public void initialize() {

	}

	@Override
	protected void updateItem(JSONModel item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {
			jsonModelNameField.setText(item.getName());
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}
	
}
