package com.dns.resttestbuilder.gui.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.data.Profile;
import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;
import com.dns.resttestbuilder.gui.MainController;
import com.dns.resttestbuilder.repository.ProfileRepository;
import com.dns.resttestbuilder.repository.ProjectRepository;
import com.dns.resttestbuilder.repository.UserRepository;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

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
	ProjectRepository projectRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	WorkspaceRepository workspaceRepository;
	
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
		Profile profile=defaultData.getProfile();
		
		ArrayList<Project> projects=defaultData.getProjects(project);
		ArrayList<Workspace> workspaces=defaultData.getWorkspaces(workspace);
		ArrayList<Profile> profiles=defaultData.getProfiles(profile);
		
		User user =defaultData.getUser(name);
		
		defaultData.save(project, projects, workspace);
		defaultData.save(workspace, workspaces, profile);
		defaultData.save(profile, profiles, user);

		return userRepository.save(user);
	}


	

}
