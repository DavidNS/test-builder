package com.dns.resttestbuilder.gui.workspace.leftTab;

import com.dns.resttestbuilder.gui.endpoint.EndpointController;
import com.dns.resttestbuilder.gui.jsonmodel.JSONModelController;
import com.dns.resttestbuilder.gui.step.StepController;
import com.dns.resttestbuilder.gui.test.TestController;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LeftTabKind {
	
	MODEL("Models","fxml/JSONModel.fxml", JSONModelController.class),
	ENDPOINT("Enpoints","fxml/Enpoint.fxml",EndpointController.class),
	STEP("Steps","fxml/Step.fxml",StepController.class),
	TEST("Tests","fxml/Test.fxml",TestController.class);
	
	String text;
	
	String fxmlView;
	
	Class<?> classController;
}
