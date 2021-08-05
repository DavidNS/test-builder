package com.dns.resttestbuilder.editscriptmethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.testexecutions.execution.steps.EditScripts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path="/editmethods")
public class EditScriptMethodController {

	@GetMapping
	List<EditScript> getAll() {
		HashMap<String, Integer> methodVSParam = EditScripts.getValidMethods();
		List<EditScript> methodsResult= new ArrayList<>();
		for (Entry<String, Integer> entry : methodVSParam.entrySet()) {
			methodsResult.add(new EditScript(entry.getKey(), entry.getValue()));
			
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
