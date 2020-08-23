package com.dns.resttestbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dns.resttestbuilder.entity.Method;
import com.dns.resttestbuilder.entity.Project;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.StepKind;
import com.dns.resttestbuilder.entity.embeddedstep.EditFieldStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.MapFieldStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ResponseSuccessKind;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.StressConditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class DevPurposes {

//	public static void main(String[] args) throws Exception {
//		EditFieldStepModel editField=new EditFieldStepModel();
//		Project pj=new Project();
//		pj.setName("pj1");
//		HashMap<String, String> planKeyVSMethod=new HashMap<>();
//		planKeyVSMethod.put("name", "getNewDNINumber");
//		editField.setPlainKeyVsMehtod(planKeyVSMethod);
//		editField.setInJson(new Gson().toJson(pj));
//		
//		MapFieldStepModel mapField=new MapFieldStepModel();
//		Project pj2=new Project();
//		pj2.setName("pj1");
//		String literalJSON=new Gson().toJson(pj2);
//		String referenceToOutJSON1="STEP_0.OUT";
//		HashMap<String, String> outPlanKeyVSMapCombination=new HashMap<>();
//		String outPlainKey="KEY_mergedName";
//		String mergeCombination="STEP_1.IN_0.name/AND/SOMESEPARATOR/AND/STEP_1.IN_1.name";
//		outPlanKeyVSMapCombination.put(outPlainKey, mergeCombination);
//		JsonObject jsOutMF=new JsonObject();
//		jsOutMF.add("mergedName", new JsonPrimitive("STEP_1.IN_0.name/AND/SOMESEPARATOR/AND/STEP_1.IN_1.name"));
//		mapField.setOutJson(jsOutMF);
//		
//		List<String> inJSONs=new ArrayList<>();
//		inJSONs.add(literalJSON);
//		inJSONs.add(referenceToOutJSON1);
//		mapField.setInJsons(inJSONs);
//		JsonObject jsonObject=new JsonObject();
//		jsonObject.add("mergedName", new JsonPrimitive("IN_0.name/AND/SOMESEPARATOR/AND/IN_1.name"));
//		mapField.setOutJson(jsonObject);
//		
//		RequestStepModel rsMD=new RequestStepModel();
//		rsMD.setUrl("http://localhost:8080/<requestParam>");
//		HashMap<String, String> paramCombination=new HashMap<String, String>();
//		paramCombination.put("requestParam", "STEP_0.OUT.name");
//		rsMD.setUrlParamKeyVSCombination(paramCombination);
//		rsMD.setMethod(Method.GET);
//		rsMD.setInJson("STEP_1.OUT");
//		
//		StressConditions sc=new StressConditions();
//		sc.setDelayBetweenParallelRequest(100L);
//		sc.setDelayBetweenParallelTest(0L);
//		sc.setNumberOfParallelRequest(10L);
//		sc.setNumberOfTest(3000L);
//		
//		ExpectedPerformaceResults exPR=new ExpectedPerformaceResults();
//		exPR.setExpectedPararellTime(3000L);
//		exPR.setExpectedSingleTime(3000L);
//		exPR.setExpectedTotalTime(3000L);
//		exPR.setResponseSuccessKind(ResponseSuccessKind.AS_EXPECTED);
//		exPR.setForceTimeoutByMaxExpectedTime(false);
//		
//		MainRequestStepModel mainRequest=new MainRequestStepModel();
//		
//		mainRequest.setExpectedPerformaceResults(exPR);
//		mainRequest.setRequestStepModel(rsMD);
//		mainRequest.setStressConditions(sc);
//		
//		Step step0=new Step();
//		step0.setName("EditFieldStep");
//		step0.setStepKind(StepKind.EDIT_FIELD);
//		step0.setStepOrder(0L);
//		step0.setStepModel(editField);
//		
//		
//		
//		Step step1=new Step();
//		step1.setName("MapFieldStep");
//		step1.setStepKind(StepKind.MAP_FIELD);
//		step1.setStepOrder(1L);
//		step1.setStepModel(mapField);
//		
//		Step step2=new Step();
//		step2.setName("MainRequestStep");
//		step2.setStepKind(StepKind.SEND_MAIN_REQUEST);
//		step2.setStepOrder(2L);
//		step2.setStepModel(mainRequest);
//		
//		Step[] steps=new Step[]{step0,step1,step2};
//		
//		
//		System.out.println(new Gson().toJson(steps));
//	}

}
