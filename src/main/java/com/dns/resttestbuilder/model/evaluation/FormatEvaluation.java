package com.dns.resttestbuilder.model.evaluation;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.ResultController;
import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ResponseSuccessKind;
import com.dns.resttestbuilder.model.JsonObjectParser;
import com.dns.resttestbuilder.model.ReservedNamesParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FormatEvaluation {

	private static final String STRING_IDENTIFIER = "\"";

	@Autowired
	JsonObjectParser genericParser;

	@Autowired
	ReservedNamesParser reservedNamesParser;

	@Autowired
	ResultController resultController;

	@Autowired
	TestExecutorController testExecutorController;

	private HashMap<ResponseSuccessKind, CheckFormat> kindVsCheckFormat = new HashMap<>();

	@FunctionalInterface
	interface CheckFormat {
		Boolean evaluateFormat(ExpectedPerformaceResults exRS, Result result,
				HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
				HashMap<Long, String> stepNumberVSOutJSON);
	}

	public FormatEvaluation() {
		kindVsCheckFormat.put(ResponseSuccessKind.NONE, this::returnTrue);
		kindVsCheckFormat.put(ResponseSuccessKind.RECEIVED, this::evaluateResponseReceived);
		kindVsCheckFormat.put(ResponseSuccessKind.SUCCESS, this::evaluateResponseSuccess);
		kindVsCheckFormat.put(ResponseSuccessKind.AS_EXPECTED, this::evaluateExactAsExpected);
	}

	public boolean evaluateFormat(TestResult testResult, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		ExpectedPerformaceResults exRS = testResult.getMainRequestStepModel().getExpectedPerformaceResults();
		boolean passed = tryCheckFormat(exRS, result, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		result.getEvaluation().setFormatPassed(passed);
		updateFormatPassed(testResult, passed);
		return passed;
	}

	private boolean tryCheckFormat(ExpectedPerformaceResults exRS, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		boolean passed = false;
		try {
			passed = kindVsCheckFormat.get(exRS.getResponseSuccessKind()).evaluateFormat(exRS, result,
					stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		} catch (Exception e) {
			log.trace("Unnable check kind format: {} of: {} and: {} caused by: {}", exRS.getResponseSuccessKind(),
					exRS.getOutput(), result.getRequestInfo().getResponse(), e);
		}
		return passed;
	}

	public TestResult updateFormatPassed(TestResult testResult, boolean passed) {
		if (passed) {
			Long ok = testResult.getFormatOK();
			ok++;
			testResult.setFormatOK(ok);
		} else {
			Long ko = testResult.getFormatKO();
			ko++;
			testResult.setFormatKO(ko);
		}
		return testResult;

	}

	private Boolean returnTrue(ExpectedPerformaceResults ex, Result r,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		return true;
	}

	private Boolean evaluateResponseReceived(ExpectedPerformaceResults exRS, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		return result.getRequestInfo().getResponseReceived();
	}

	private Boolean evaluateResponseSuccess(ExpectedPerformaceResults exRS, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		return evaluateResponseReceived(exRS, result, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON)
				&& exRS.getExpectedHttpStatus().equals(result.getRequestInfo().getResponseCode());
	}

	private Boolean evaluateExactAsExpected(ExpectedPerformaceResults exRS, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		return evaluateResponseSuccess(exRS, result, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON)
				&& evaluateExpected(exRS, result, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);

	}

	private Boolean evaluateExpected(ExpectedPerformaceResults exRS, Result result,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		try {
			JsonElement expected = buildResponseExpected(exRS, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
			JsonElement response = JsonParser.parseString(result.getRequestInfo().getResponse());
			return expected.toString().equals(response.toString());
		} catch (Exception e) {
			String expected = exRS.getOutput().toString();
			if(expected.startsWith(STRING_IDENTIFIER)) {
				expected=expected.substring(1,expected.length()-1);
			}
			return expected.equals(result.getRequestInfo().getResponse());
		}
	}

	private JsonElement buildResponseExpected(ExpectedPerformaceResults exRS,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		JsonElement expected = genericParser.objectToModel(exRS.getOutput(), JsonElement.class, exRS::setOutput);
		reservedNamesParser.mapOutJson(expected, new HashMap<>(), stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		return expected;
	}

}
