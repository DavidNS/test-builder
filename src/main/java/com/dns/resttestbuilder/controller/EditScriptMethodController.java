package com.dns.resttestbuilder.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.model.execution.steps.EditScripts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path="/editMethods")
public class EditScriptMethodController {

	@GetMapping
	List<EditScript> getAll(@PathVariable Long userID) {
		Method[] methods = EditScripts.class.getMethods();
		List<EditScript> methodsResult= new ArrayList<>();
		for (Method method : methods) {
			String methodName= method.getName();
			int paramsNumber= method.getParameterCount() - 1;
			methodsResult.add(new EditScript(methodName, paramsNumber));
			
		}
		return methodsResult;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class EditScript {
		String methodName;
		int paramsNumber;
		
	}
}
