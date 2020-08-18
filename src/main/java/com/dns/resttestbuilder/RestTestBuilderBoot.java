package com.dns.resttestbuilder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;
import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.entity.Project;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.StepKind;
import com.dns.resttestbuilder.entity.embedded.EditFieldStepModel;
import com.dns.resttestbuilder.entity.embedded.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embedded.MapFieldStepModel;
import com.dns.resttestbuilder.entity.embedded.RequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embedded.mainRequest.StressConditions;
import com.google.gson.Gson;


/**
 * Launcher de la applicacion.
 * Base -> Spring Boot+JAVAFX,
 * Cliente rest-> OkHttp,
 * Librerias parseo json -> ORG.JSON, 
 * @author David Nowakowski Stachyra
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({AsyncConfiguration.class, ReservedNames.class /*, OtherConf.class*/})
public class RestTestBuilderBoot {


	public static void main(String[] args) {
//		EditFieldStepModel editField=new EditFieldStepModel();
//		Project pj=new Project();
//		pj.setName("pj1");
//		HashMap<String, String> planKeyVSMethod=new HashMap<>();
//		planKeyVSMethod.put("KEY_name", "getNewDNINumber");
//		editField.setPlainKeyVsMehtod(planKeyVSMethod);
//		editField.setInJson(new Gson().toJson(pj));
//		
//		MapFieldStepModel mapField=new MapFieldStepModel();
//		Project pj2=new Project();
//		pj2.setName("pj1");
//		String literalJSON=new Gson().toJson(pj2);
//		String referenceToOutJSON1="STEP_0:OUT";
//		HashMap<String, String> outPlanKeyVSMapCombination=new HashMap<>();
//		String outPlainKey="KEY_mergedName";
//		String mergeCombination="IN_0:KEY_name/AND/SOMESEPARATOR/AND/IN_1:KEY_name";
//		outPlanKeyVSMapCombination.put(outPlainKey, mergeCombination);
//		List<String> inJSONs=new ArrayList<>();
//		inJSONs.add(literalJSON);
//		inJSONs.add(referenceToOutJSON1);
//		mapField.setInJson(inJSONs);
//		mapField.setOutPlainKeyVsMapCombination(outPlanKeyVSMapCombination);
//		
//		RequestStepModel rsMD=new RequestStepModel();
//		rsMD.setUrl("http://someulrl.com/<<requestParam>>");
//		HashMap<String, String> paramCombination=new HashMap<String, String>();
//		paramCombination.put("requestParam", "STEP_0:OUT:KEY_name");
//		rsMD.setUrlParamKeyVSCombination(paramCombination);
//		rsMD.setMethod("POST");
//		rsMD.setInJson("STEP_1:OUT");
//		
//		StressConditions sc=new StressConditions();
//		sc.setDelayBetweenParallelRequest(100L);
//		sc.setDelayBetweenParallelTest(10L);
//		sc.setNumberOfParallelRequest(1L);
//		sc.setNumberOfTest(1L);
//		
//		ExpectedPerformaceResults exPR=new ExpectedPerformaceResults();
//		exPR.setExpectedPararellTime(1L);
//		exPR.setExpectedRequestTime(1L);
//		exPR.setExpectedTotalTime(1L);
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
//		step0.setStepModel(new Gson().toJson(editField));
//		
//		
//		
//		Step step1=new Step();
//		step1.setName("MapFieldStep");
//		step1.setStepKind(StepKind.MAP_FIELD);
//		step1.setStepOrder(1L);
//		step1.setStepModel(new Gson().toJson(mapField));
//		
//		Step step2=new Step();
//		step2.setName("MainRequestStep");
//		step2.setStepKind(StepKind.SEND_MAIN_REQUEST);
//		step2.setStepOrder(2L);
//		step2.setStepModel(new Gson().toJson(mainRequest));
//		
//		Step[] steps=new Step[]{step0,step1,step2};
//		
//		
//		System.out.println(new Gson().toJson(steps));
		

		SpringApplication.run(RestTestBuilderBoot.class, args);
	}
	


}
