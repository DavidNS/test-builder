package com.dns.resttestbuilder.steps;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.exception.MainRequestException;
import com.dns.resttestbuilder.exception.NotValidStepOrderException;
import com.dns.resttestbuilder.steps.validation.AStepValidation;
import com.dns.resttestbuilder.steps.validation.DefaultData;
import com.dns.resttestbuilder.steps.validation.step.EditFieldStepValidation;
import com.dns.resttestbuilder.steps.validation.step.MainRequestStepValidaton;
import com.dns.resttestbuilder.steps.validation.step.MapFieldValidation;
import com.dns.resttestbuilder.steps.validation.step.ResquesStepValidation;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.dns.resttestbuilder.tests.Test;
import com.dns.resttestbuilder.tests.TestController;
import com.dns.resttestbuilder.users.UserController;

@Validated
@RestController
@RequestMapping
public class StepController {

	@Autowired
	DefaultData defaultData;

	@Autowired
	JsonObjectParser jsonObjectParser;

	@Autowired
	StepRepository repository;

	@Autowired
	TestController testController;

	@Autowired
	UserController userController;

	HashMap<StepKind, Class<?>> stepKindVSClassCast = new HashMap<>();

	HashMap<StepKind, AStepValidation<?>> stepKindVSHandleStep = new HashMap<>();

	public StepController(ConfigurableApplicationContext context) {
		buildStepHandleMap(context);
	}

	private void buildStepHandleMap(ConfigurableApplicationContext context) {		
		stepKindVSHandleStep.put(StepKind.EDIT_FIELD, context.getBean(EditFieldStepValidation.class));
		stepKindVSHandleStep.put(StepKind.MAP_FIELD, context.getBean(MapFieldValidation.class));
		stepKindVSHandleStep.put(StepKind.SEND_REQUEST, context.getBean(ResquesStepValidation.class));
		stepKindVSHandleStep.put(StepKind.SEND_MAIN_REQUEST, context.getBean(MainRequestStepValidaton.class));
	}

	@GetMapping("/steps")
	List<Step> getAll(Principal principal) {
		return repository.findByUserID(principal.getName());
	}

	@GetMapping("/steps/{id}")
	Step getOrThrow(Principal principal, @PathVariable Long id) {
		return repository.findById(id).map((s) -> {
			defaultData.handleNotValidUserID(Step.class, id, s.getUserID(), principal.getName());
			return s;
		}).orElseThrow(defaultData.getNotFoundSupplier(Step.class, id));
	}

	@PostMapping("/tests/{testID}/steps")
	List<Step> newItem(Principal principal, @PathVariable Long testID,
			@RequestBody @NotEmpty(message = "Input movie list cannot be empty.") List<com.dns.resttestbuilder.steps.Step> steps)
			throws IllegalArgumentException, IllegalAccessException {
		Test test = testController.getOrThrow(principal, testID);
		steps=handle(principal, steps);
		steps = saveSteps(steps);
		test.getSteps().clear();
		test.getSteps().addAll(steps);
		testController.saveFull(test);
		return steps;
	}

	private List<Step> saveSteps(List<Step> steps) {
		List<Step> stepsSaved = new ArrayList<>();
		for (Step step : steps) {
			stepsSaved.add(repository.save(step));
		}
		return stepsSaved;
	}

	public List<Step> handle(Principal principal, List<Step> steps) throws IllegalArgumentException, IllegalAccessException {
		String userID=principal.getName();
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
		steps.sort((one, two) -> {
			return one.getStepOrder().intValue() - two.getStepOrder().intValue();
		});
		List<Step> stepsToReturn=new ArrayList<>();
		Step prev = null;
		HashMap<Long, Integer> stepNumberVsInJson = new HashMap<>();
		List<Step> mainRequestSteps = new ArrayList<>();
		for (int i = 0; i < steps.size(); i++) {
			Step step =newStep(userID,steps.get(i));
			step.setUserID(userID);
			collectMainRequest(steps, mainRequestSteps, i);
			prev = checkOrder(step, prev);
			stepKindVSHandleStep.get(step.getStepKind()).handle(step, stepNumberVsInJson);
			stepsToReturn.add(step);
		}
		if (mainRequestSteps.size() < 1) {
			throw new MainRequestException();
		}
		if (mainRequestSteps.size() > 1) {
			throw new MainRequestException(mainRequestSteps.toArray(Step[]::new));
		}
		return stepsToReturn;

	}

	private Step newStep(String userID, Step step) {
		return new Step(null,userID,step.getName(),step.getStepOrder(),step.getStepKind(),step.getStepModel());
	}

	private void collectMainRequest(List<Step> steps, List<Step> mainRequestSteps, int i) {
		Step step= steps.get(i);
		StepKind kind = step.getStepKind();
		if (kind.equals(StepKind.SEND_MAIN_REQUEST)) {
			mainRequestSteps.add(step);
		}
	}

	private Step checkOrder(Step current,Step prev) {
		if (prev == null && current.getStepOrder().intValue() != 0) {
			throw new NotValidStepOrderException(current, "0", current.getStepOrder().toString());
		} else if (prev != null) {
			int previousOrder = prev.getStepOrder().intValue();
			int difference = previousOrder - current.getStepOrder().intValue();
			if (difference != -1) {
				throw new NotValidStepOrderException(current, String.valueOf(previousOrder + 1L),
						current.getStepOrder().toString());
			}
		}
		return current;
	}

}
