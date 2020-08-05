package com.dns.resttestbuilder.gui.jsonmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.JSONModel;
import com.dns.resttestbuilder.gui.WindowBuilder;
import com.dns.resttestbuilder.repository.JSONModelRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

@Component
@Scope("singleton")
public class JSONModelController {

	private static final String FX_BACKGROUND_COLOR_GREY = "-fx-background-color: grey;";

	private static final String FX_BACKGROUND_COLOR_GREEN = "-fx-background-color: green;";

	private static final String FX_BACKGROUND_COLOR_YELLOW = "-fx-background-color: yellow;";

	private static final String FX_BACKGROUND_COLOR_ORANGE = "-fx-background-color: orange;";

	private static final String FX_BACKGROUND_COLOR_RED = "-fx-background-color: red;";

	@Autowired
	JSONModelRepository jsonModelRepository;

	@FXML
	Button newButton;

	@FXML
	Button saveButton;

	@FXML
	Button saveAllButton;

	@FXML
	Button deleteButton;

	private ObservableList<JSONModel> leftTabObsJSONModel = FXCollections.observableArrayList();

	@FXML
	ListView<JSONModel> leftTabViewJSONModel;
	

	@Autowired
	private WindowBuilder wb;

	@FXML
	public void initialize() {
		newButton.setStyle(FX_BACKGROUND_COLOR_GREEN);
		newButton.setOnAction((e)->{
			JSONModel jsonModel=new JSONModel();
			leftTabObsJSONModel.add(jsonModel);
		});
		
		leftTabViewJSONModel.setCellFactory((d)->{
			 return wb.buildListCellElement(JSONModelCell.class, "fxml/JSONModelCell.fxml");
		});
		leftTabViewJSONModel.setItems(leftTabObsJSONModel);
		
		saveButton.setStyle(FX_BACKGROUND_COLOR_GREY);
		saveAllButton.setStyle(FX_BACKGROUND_COLOR_GREY);
		deleteButton.setStyle(FX_BACKGROUND_COLOR_GREY);
	}

}
