package com.dns.resttestbuilder.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.repository.ResultRepository;
import com.dns.resttestbuilder.validation.DefaultData;

@RestController
@RequestMapping(path = "/users/{userID}")
public class ResultController {

	@Autowired
	ResultRepository resultRepository;

	@Autowired
	DefaultData defaultData;
	

	@GetMapping("/results/{resultID}")
	Result getResult(@PathVariable Long userID, @PathVariable Long resultID) {
		Result result = resultRepository.findById(resultID).map((r) -> {
			defaultData.handleNotValidUserID(TestResult.class, resultID, r.getUserID(), userID);
			return r;
		}).orElseThrow(defaultData.getNotFoundSupplier(TestResult.class, resultID));
		return result;
	}

	public Result saveFull(Result result) {
		return resultRepository.save(result);
	}

	public void saveAllFull(ArrayList<Result> rs) {
		resultRepository.saveAll(rs);
		
	}

}
