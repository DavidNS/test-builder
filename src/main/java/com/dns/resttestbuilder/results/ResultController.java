package com.dns.resttestbuilder.results;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.steps.validation.DefaultData;
import com.dns.resttestbuilder.testresults.TestResult;

@RestController
@RequestMapping
public class ResultController {

	@Autowired
	ResultRepository repository;

	@Autowired
	DefaultData defaultData;
	
	@GetMapping("/results")
	List<Result> getAll(Principal principal) {
		return repository.findByUserID(principal.getName());
	}
	
	
	@GetMapping("/results/{resultID}")
	Result getResult(Principal principal, @PathVariable Long resultID) {
		Result result = repository.findById(resultID).map((r) -> {
			defaultData.handleNotValidUserID(TestResult.class, resultID, r.getUserID(), principal.getName());
			return r;
		}).orElseThrow(defaultData.getNotFoundSupplier(TestResult.class, resultID));
		return result;
	}

	public Result saveFull(Result result) {
		return repository.save(result);
	}

	public void saveAllFull(ArrayList<Result> rs) {
		repository.saveAll(rs);
		
	}

}
