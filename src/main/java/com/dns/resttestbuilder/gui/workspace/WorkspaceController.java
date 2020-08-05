package com.dns.resttestbuilder.gui.workspace;

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
import com.dns.resttestbuilder.gui.WindowBuilder;
import com.dns.resttestbuilder.gui.workspace.choiceBox.WorkspaceChoiceBox;
import com.dns.resttestbuilder.gui.workspace.leftTab.LeftTabCell;
import com.dns.resttestbuilder.gui.workspace.leftTab.LeftTabKind;
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

		initChoicePanels();

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
	
	private void initChoicePanels() {
		Node n1 = wb.buildWindow(worskpaceChoice, WORKSPACE_CHOICE_BOX_FXML);
		workspaceCBAnchor.getChildren().setAll(n1);
		worskpaceChoice.initializeItem(Workspace::new, Workspace::getName, Workspace::setName, this::equalsWorkspace);

		worskpaceChoice.initializeModel(this::workspaceSelected, this::newWorkspace, this::editWorkspace,
				this::deleteWorkspace);

		Node n2 = wb.buildWindow(projectChoice, WORKSPACE_CHOICE_BOX_FXML);
		projectCBAnchor.getChildren().setAll(n2);
		projectChoice.initializeItem(Project::new, Project::getName, Project::setName, this::equalsProject);

		projectChoice.initializeModel(this::projectSelected, this::newProject, this::editProject, this::deleteProject);
	}

	public void updateUser(User user) {
		this.user = user;
		
		List<Workspace> workspaces = user.getWorkspaces();
		Workspace selectedWorkspace = getSelectedWorkspace(user, workspaces);
		List<Project> projects = selectedWorkspace.getProjects();
		Project selectedProject = getSelectedProject(user, projects);

		worskpaceChoice.updateStatus(selectedWorkspace, workspaces);
		projectChoice.updateStatus(selectedProject, projects);
	}

	private Project getSelectedProject(User user, List<Project> projects) {
		Long selectedProjectID=Long.parseLong(defaultData.getUserPreference(user, UserPreferencesNames.USER_PROJECT_ID));
		Project selectedProject = defaultData.getSelected(projects, selectedProjectID, this::equalsProject);
		return selectedProject;
	}

	private Workspace getSelectedWorkspace(User user, List<Workspace> workspaces) {
		Long selectedWorkspaceID=Long.parseLong(defaultData.getUserPreference(user, UserPreferencesNames.USER_WORKSPACE_ID));
		Workspace selectedWorkspace = defaultData.getSelected(workspaces,selectedWorkspaceID, this::equalsWorkspace);
		return selectedWorkspace;
	}

	private void editProject(Project editedProject) {
		editedProject = projectRepository.save(editedProject);
		projectChoice.restoreStatus(editedProject);
	}

	private void editWorkspace(Workspace editedWorkspace) {
		editedWorkspace = workspaceRepository.save(editedWorkspace);
		worskpaceChoice.restoreStatus(editedWorkspace);
	}

	public void projectSelected(Project selectedProject) {
		Long selectedProjectID=selectedProject.getId();
		Workspace selectedWorkspace = getSelectedWorkspace(user, user.getWorkspaces());
		List<Project> projects = selectedWorkspace.getProjects();

		defaultData.saveUserPreference(user, UserPreferencesNames.USER_PROJECT_ID, selectedProjectID);
		userRepository.save(user);
		
		projectChoice.updateStatus(selectedProject, projects);
	}
	
	private void newProject(Project newProject) {
		Workspace selectedWorkspace = getSelectedWorkspace(user, user.getWorkspaces());
		List<Project> projects = selectedWorkspace.getProjects();
		projects.add(newProject);
		
		userRepository.save(user);
		
		Long selectedProjectID=newProject.getId();
		defaultData.saveUserPreference(user, UserPreferencesNames.USER_PROJECT_ID, selectedProjectID);
		
		projectChoice.updateStatus(newProject, selectedWorkspace.getProjects());
	}

	public void workspaceSelected(Workspace selectedWorkspace) {

		List<Project> projects = selectedWorkspace.getProjects();
		
		
//		Project selectedProject = getSelected(projects, selectedWorkspace.getSelectProjectID(), this::equalsProject);

//		user.setSelectWorkspaceID(selectedWorkspace.getId());
//		userRepository.save(user);
//
//		projectChoice.updateStatus(selectedProject, projects);
	}



	
	private void newWorkspace(Workspace newWorkspace) {
//		Project project = defaultData.getProject();
//		List<Project> projects = defaultData.getProjects(project);
//
//		List<Workspace> workspaces = user.getWorkspaces();
//
//		defaultData.save(project, projects, newWorkspace);
//		defaultData.save(newWorkspace, workspaces, user);
//
//		userRepository.save(user);
//
//		worskpaceChoice.updateStatus(newWorkspace, workspaces);
//		projectChoice.updateStatus(project, projects);
	}


	private void deleteWorkspace(Workspace deletedWorkspace) {
		List<Workspace> workspaces = worskpaceChoice.getChoiceBox().getItems();
//		List<Project> deletedProjects = deletedWorkspace.getProjects();
//
//		Workspace newSelectedWorkspace = worskpaceChoice.deleteSelectedItem();
//		List<Project> newProjects = newSelectedWorkspace.getProjects();
//
//		user.setSelectWorkspaceID(newSelectedWorkspace.getId());
//		user.setWorkspaces(workspaces);
//
//		Project selectedProject = getSelected(newProjects, newSelectedWorkspace.getSelectProjectID(),
//				this::equalsProject);
//
//		userRepository.save(user);
//		workspaceRepository.delete(deletedWorkspace);
//		projectRepository.deleteAll(deletedProjects);
//
//		worskpaceChoice.updateStatus(newSelectedWorkspace, workspaces);
//		projectChoice.updateStatus(selectedProject, newProjects);
	}

	private void deleteProject(Project deletedProject) {
		Workspace workspace = worskpaceChoice.getChoiceBox().getValue();
		List<Project> projects = projectChoice.getChoiceBox().getItems();

//		Project newSelectedProject = projectChoice.deleteSelectedItem();
//
//		workspace.setSelectProjectID(newSelectedProject.getId());
//		workspace.setProjects(projects);
//
//		workspaceRepository.save(workspace);
//		projectRepository.delete(deletedProject);
//
//		projectChoice.updateStatus(newSelectedProject, workspace.getProjects());
	}

	private boolean equalsProject(Project p, Long l) {
		return p.getId().equals(l);
	}

	private boolean equalsWorkspace(Workspace p, Long l) {
		return p.getId().equals(l);
	}

	private boolean equalsProject(Project p, Project p2) {
		return equalsProject(p, p2.getId());
	}

	private boolean equalsWorkspace(Workspace w, Workspace w2) {
		return equalsWorkspace(w, w2.getId());
	}

}
