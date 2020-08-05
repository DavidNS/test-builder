package com.dns.resttestbuilder.gui.step;

import com.dns.resttestbuilder.gui.endpoint.EndpointController;
import com.dns.resttestbuilder.gui.jsonmodel.JSONModelController;
import com.dns.resttestbuilder.gui.test.TestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StepKind {

	
	MODIFY_FIELD("Modify Field","fxml/ModifyFieldStep.fxml", JSONModelController.class),
	MERGE_FIELD("Merge Field","fxml/MergeFieldController.fxml",EndpointController.class),
	SEND_REQUEST("Test","fxml/TestController.fxml",TestController.class);
	
	String text;
	
	String fxmlView;
	
	Class<?> classController;
}
