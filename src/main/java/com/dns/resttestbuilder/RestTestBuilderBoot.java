package com.dns.resttestbuilder;

import java.awt.List;
import java.util.HashMap;

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
import com.dns.resttestbuilder.entity.embedded.RequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embedded.mainRequest.StressConditions;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
//		planKeyVSMethod.put("___KEY___name", "getNewDNINumber");
//		editField.setPlainKeyVsMehtod(planKeyVSMethod);
//		editField.setInJson(new Gson().toJson(pj));
//		
//		
//		
//		RequestStepModel rsMD=new RequestStepModel();
//		rsMD.setUrl("http://someulrl.com");
//		rsMD.setMethod("POST");
//		rsMD.setInJson("STEP_0_OUT");
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
//		Step step1=new Step();
//		step1.setName("MainRequestStep");
//		step1.setStepKind(StepKind.SEND_MAIN_REQUEST);
//		step1.setStepOrder(1L);
//		step1.setStepModel(new Gson().toJson(mainRequest));
//		
//		Step[] steps=new Step[]{step0,step1};
//		
//		
//		System.out.println(new Gson().toJson(steps));
		
		
		SpringApplication.run(RestTestBuilderBoot.class, args);
	}
	


}
