package com.dns.resttestbuilder.gui.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.configuration.UserPreferencesNames;
import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;
import com.dns.resttestbuilder.gui.MainController;
import com.dns.resttestbuilder.repository.UserRepository;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Scope("singleton")
@Slf4j
public class UserController {
	
	@Autowired
	DefaultData defaultData;

	@Autowired
	MainController mainController;
	
	@Autowired
	UserRepository userRepository;

	@FXML
	TextField userTextField;

	@FXML
	Button userButton;


	@FXML
	public void initialize() {
		userButton.setOnAction((e) -> {
			User user=getOrCreateUser(userTextField.getText());
			mainController.showWorkspaceWindow(user);
		});
	}

	private User getOrCreateUser(String name) {
		List<User> users=userRepository.findByName(name);
		if(users!=null && users.size()>0) {
			log.info("Usuario encontrado: "+name);
			return users.get(0);
		}else{
			log.info("Usuario no encontrado: "+name+" creando nuevo usuario...");
			return createBaseUser(name);
		}
	}

	private User createBaseUser(String name) {
		Project project=defaultData.getProject();
		Workspace workspace=defaultData.getWorkspace();
		User user =defaultData.getUser(name,project,workspace);
		
		user= userRepository.save(user);
		
		defaultData.saveUserPreference(user, UserPreferencesNames.USER_PROJECT_ID, project.getId());
		defaultData.saveUserPreference(user, UserPreferencesNames.USER_WORKSPACE_ID, workspace.getId());
		
		return userRepository.save(user);
	}
	
}
