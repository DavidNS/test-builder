package com.dns.resttestbuilder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/users/{userID}")
public class JSONModelController {

//	@Autowired
//	DefaultData defaultData;
//	
//	@Autowired
//	JSONModelRepository repository;
//	
//	
//	@Autowired
//	ProjectController projectController;
//
//	@Autowired
//	UserController userController;
//	
//
//	@GetMapping("/jsonmodels")
//	List<JSONModel> getAll(@PathVariable Long userID) {
//		return repository.findByUserID(userID);
//	}
//	
//	@GetMapping("/jsonmodels/{id}")
//	JSONModel getOrThrow(@PathVariable Long userID,@PathVariable Long id) {
//		return repository.findById(id).map((pj)->{
//			defaultData.handleNotValidUserID(JSONModel.class, id, pj.getUserID(), userID);
//			return pj;
//		}).orElseThrow(defaultData.getNotFoundSupplier(JSONModel.class, id));
//	}
//	
//	@PutMapping("/jsonmodels/{id}")
//	JSONModel replace(@PathVariable Long userID, @PathVariable Long id, @RequestBody JSONModel jsonModel) {
//		return repository.findById(id).map(pj -> {
//			return repository.save(handle(userID,pj,jsonModel));
//		}).orElseThrow(defaultData.getNotFoundSupplier(JSONModel.class, id));
//	}
//	
//	@PostMapping("/projects/{projectID}/jsonmodels")
//	JSONModel newItem(@PathVariable Long userID, @PathVariable Long projectID,@RequestBody JSONModel jsonModel) {
//		Project project=projectController.getOrThrow(userID,projectID);
//		List<JSONModel> jsonModels=project.getJsonModels();
//		jsonModel=repository.save(handle(userID, new JSONModel(), jsonModel));
//		jsonModels.add(jsonModel);
//		projectController.replace(userID,projectID,project);
//		return jsonModel;
//	}
//	
//	@DeleteMapping("/projects/{projectID}/jsonmodels/{id}")
//	void delete(@PathVariable Long userID,@PathVariable Long projectID,@PathVariable Long id) {
//		Project project=projectController.getOrThrow(userID, projectID);
//		project.getJsonModels().removeIf((jsM)->{return jsM.getId().equals(id);});
//		projectController.saveFull(project);
//	}
//
//	
//	public JSONModel handle(Long userID, JSONModel dataToSave, JSONModel newData) {
//		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
//		defaultData.handleNotValidUserID(JSONModel.class, dataToSave.getId(), dataToSave.getUserID(), userID);
//		
//		dataToSave.setName(newData.getName());
//		dataToSave.setUserID(userID);
//		dataToSave.setStringJSON(newData.getStringJSON());
//		
//		return dataToSave;
//	}


}