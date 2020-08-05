package com.dns.resttestbuilder.gui.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.data.Profile;
import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;
import com.dns.resttestbuilder.gui.MainController;
import com.dns.resttestbuilder.gui.WindowBuilder;
import com.dns.resttestbuilder.gui.workspace.choiceBox.WorkspaceChoiceBox;
import com.dns.resttestbuilder.gui.workspace.leftTab.LeftTabCell;
import com.dns.resttestbuilder.gui.workspace.leftTab.LeftTabKind;
import com.dns.resttestbuilder.repository.ProfileRepository;
import com.dns.resttestbuilder.repository.ProjectRepository;
import com.dns.resttestbuilder.repository.UserRepository;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Scope("singleton")
@Slf4j
public class WorkspaceController {

	private static final String WORKSPACE_CHOICE_BOX_FXML = "fxml/WorkspaceChoiceBox.fxml";

	private static final String WORKSPACE_LEFT_TAB_ELEMENT_FXML = "fxml/WorkspaceLefTabElement.fxml";

	private ObservableList<LeftTabKind> leftTabObs = FXCollections.observableArrayList();

	@Autowired
	MainController mainController;

	@Autowired
	WindowBuilder wb;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	WorkspaceRepository workspaceRepository;

	@Autowired
	UserRepository userRepository;

	@FXML
	ListView<LeftTabKind> leftTabView;

	@FXML
	AnchorPane leftTabContent;

	@FXML
	Button changeUserButton;

	@FXML
	AnchorPane workspaceCBAnchor;

	@FXML
	AnchorPane projectCBAnchor;

	@FXML
	AnchorPane profileCBAnchor;

	@Autowired
	WorkspaceChoiceBox<Profile> profileChoice;

	@Autowired
	WorkspaceChoiceBox<Workspace> worskpaceChoice;

	@Autowired
	WorkspaceChoiceBox<Project> projectChoice;

	User user;

	@Autowired
	DefaultData defaultData;
	
	@FXML
	public void initialize() {

		initChangeUserButton();

		initLeftTabs();

		Node n = wb.buildWindow(profileChoice, WORKSPACE_CHOICE_BOX_FXML);
		profileCBAnchor.getChildren().setAll(n);
		profileChoice.initializeChoiceBox(Profile::new, Profile::getName, Profile::setName, this::equalsProfile);

		Node n2 = wb.buildWindow(worskpaceChoice, WORKSPACE_CHOICE_BOX_FXML);
		workspaceCBAnchor.getChildren().setAll(n2);
		worskpaceChoice.initializeChoiceBox(Workspace::new, Workspace::getName, Workspace::setName, this::equalsWorkspace);

		Node n3 = wb.buildWindow(projectChoice, WORKSPACE_CHOICE_BOX_FXML);
		projectCBAnchor.getChildren().setAll(n3);
		projectChoice.initializeChoiceBox(Project::new, Project::getName, Project::setName, this::equalsProject);
	}

	private void initChangeUserButton() {
		changeUserButton.setOnAction((e) -> {
			mainController.showUserWindow();
		});
	}

	private void initLeftTabs() {
		leftTabView.setCellFactory((d) -> {
			return wb.buildListCellElement(LeftTabCell.class, WORKSPACE_LEFT_TAB_ELEMENT_FXML);
		});
		for (LeftTabKind leftTabKind : LeftTabKind.values()) {
			leftTabObs.add(leftTabKind);
		}
		leftTabView.setItems(leftTabObs);
	}
	
	public void updateUser(User user) {
		this.user = user;
		List<Profile>  profiles = user.getProfiles();
		Profile selectedProfile = getSelected(profiles, user.getSelectProfileID(), this::equalsProfile);
		List<Workspace> workspaces = selectedProfile.getWorkspaces();
		Workspace  selectedWorkspace = getSelected(workspaces, selectedProfile.getSelectWorkspaceID(), this::equalsWorkspace);
		List<Project>  projects = selectedWorkspace.getProjects();
		Project selectedProject = getSelected(projects, selectedWorkspace.getSelectProjectID(), this::equalsProject);

		profileChoice.updateStatus(selectedProfile, profiles);
		worskpaceChoice.updateStatus(selectedWorkspace, workspaces);
		projectChoice.updateStatus(selectedProject, projects);
	}

	private <C> C getSelected(List<C> searchingList, Long itemID, BiFunction<C, Long, Boolean> selectionFunction) {
		C searchingItem = null;
		for (int i = 0; i < searchingList.size() && searchingItem == null; i++) {
			C listItem = searchingList.get(i);
			if (selectionFunction.apply(listItem, itemID)) {
				searchingItem = listItem;
			}
		}		
		return searchingItem;
	}

	public <T> void editFromChoiceBox(T item) {
		if (item instanceof Project) {
			editProject();
		} else if (item instanceof Workspace) {

		} else if (item instanceof Profile) {

		}
	}

	private void editProject() {
		Project editedItem =  projectChoice.getChoiceBox().getValue();
		editedItem = projectRepository.save(editedItem);
		projectChoice.restoreStatus(editedItem);
	}
	
	private void editWorkspace() {
		Workspace editedItem =  worskpaceChoice.getChoiceBox().getValue();
		editedItem = workspaceRepository.save(editedItem);
		worskpaceChoice.restoreStatus(editedItem);
	}
	
	private void editProfile() {
		Profile editedItem =  profileChoice.getChoiceBox().getValue();
		editedItem = profileRepository.save(editedItem);
		profileChoice.restoreStatus(editedItem);
	}
	

	public void itemSelectedFromChoiceBox() {
		
		
	}

	public <T> void saveFromChoiceBox(T newObjectItem) {
		if (newObjectItem instanceof Project) {
			
			newProject();

		} else if (newObjectItem instanceof Workspace) {
			
			newWorkspace();
			
		} else if (newObjectItem instanceof Profile) {
			newProfile();
			
		}
	}

	private void newProfile() {
		Project project=defaultData.getProject();
		Workspace workspace=defaultData.getWorkspace();
		
		ArrayList<Project> projects=defaultData.getProjects(project);
		ArrayList<Workspace> workspaces=defaultData.getWorkspaces(workspace);

		
		Profile newProfile=profileChoice.getChoiceBox().getValue();
		List<Profile> selectedProfiles=user.getProfiles();
		
		defaultData.save(project, projects, workspace);
		defaultData.save(workspace, workspaces, newProfile);
		defaultData.save(newProfile, selectedProfiles,user);
		
		worskpaceChoice.updateStatus(workspace,workspaces);
		projectChoice.updateStatus(project, projects);
		profileChoice.updateStatus(newProfile, selectedProfiles);
	}

	private void newWorkspace() {
		Project project=defaultData.getProject();
		List<Project> projects=defaultData.getProjects(project);
		
		Profile selectedProfile=profileChoice.getChoiceBox().getValue();
		List<Workspace> workspaces=worskpaceChoice.getChoiceBox().getItems();
		Workspace newWorkspace =  worskpaceChoice.getChoiceBox().getValue();
		
		defaultData.save(project, projects, newWorkspace);
		defaultData.save(newWorkspace, workspaces, selectedProfile);
		
		profileRepository.save(selectedProfile);
		
		worskpaceChoice.updateStatus(newWorkspace, workspaces);
		projectChoice.updateStatus(project, projects);
	}

	private void newProject() {
		Workspace selectedWorkspace=worskpaceChoice.getChoiceBox().getValue();
		List<Project> projects=projectChoice.getChoiceBox().getItems();
		
		Project newProject =  projectChoice.getChoiceBox().getValue(); //GetValue
		newProject = defaultData.save(newProject,projects,selectedWorkspace);
		
		workspaceRepository.save(selectedWorkspace);
		
		projectChoice.updateStatus(newProject, selectedWorkspace.getProjects());
	}

	public <T> void deleteFromChoiceBox(T deletedItem) {
		if (deletedItem instanceof Project) {
			Workspace workspace =  worskpaceChoice.getChoiceBox().getValue();
			Project deletedProject = projectChoice.getChoiceBox().getValue();
			List<Project> projects=projectChoice.getChoiceBox().getItems();
		
			
			projectChoice.deleteSelectedItem();
			
			Project newSelectedProject = projectChoice.getChoiceBox().getValue();
			workspace.setSelectProjectID(newSelectedProject.getId());
			workspace.setProjects(projects);
			
			workspaceRepository.save(workspace);
			projectRepository.delete(deletedProject);
			
			projectChoice.updateStatus(newSelectedProject, workspace.getProjects());
		} else if (deletedItem instanceof Workspace) {
			Profile profile=profileChoice.getChoiceBox().getValue();
			Workspace deletedWorkspace =  worskpaceChoice.getChoiceBox().getValue();
			List<Workspace> workspaces=worskpaceChoice.getChoiceBox().getItems();
			List<Project> projects=deletedWorkspace.getProjects();
			
			worskpaceChoice.deleteSelectedItem();
			
			Workspace newSelectedWorkspace =  worskpaceChoice.getChoiceBox().getValue();
			profile.setSelectWorkspaceID(newSelectedWorkspace.getId());
			profile.setWorkspaces(workspaces);
			List<Project> newProjects=newSelectedWorkspace.getProjects();
			
			Project selectedProject = getSelected(newProjects, newSelectedWorkspace.getSelectProjectID(), this::equalsProject);
			

			profileRepository.save(profile);
			workspaceRepository.delete(deletedWorkspace);
//			projectRepository.deleteAll(projects);
			

			worskpaceChoice.updateStatus(newSelectedWorkspace,workspaces);
			projectChoice.updateStatus(selectedProject, newProjects);
			
			
		} else if (deletedItem instanceof Profile) {
			
		}
	}
	
	private boolean equalsProject(Project p, Long l) {
		return p.getId().equals(l);
	}

	private boolean equalsWorkspace(Workspace p, Long l) {
		return p.getId().equals(l);
	}

	private Boolean equalsProfile(Profile p, Long l) {
		return p.getId().equals(l);
	}
	
	private boolean equalsProject(Project p, Project p2) {
		return equalsProject(p, p2.getId());
	}

	private boolean equalsWorkspace(Workspace w, Workspace w2) {
		return equalsWorkspace(w, w2.getId());
	}

	private Boolean equalsProfile(Profile p, Profile p2) {
		return equalsProfile(p, p2.getId());
	}


}
