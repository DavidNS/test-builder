package com.dns.resttestbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;
import com.dns.resttestbuilder.entity.Project;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.StepKind;
import com.dns.resttestbuilder.entity.embedded.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embedded.StressConditions;
import com.dns.resttestbuilder.entity.stepModel.MainRequestStepModel;
import com.dns.resttestbuilder.entity.stepModel.RequestStepModel;
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
@EnableConfigurationProperties({AsyncConfiguration.class/*, OtherConf.class*/})
public class RestTestBuilderBoot {


	public static void main(String[] args) {
		
		
		
//		RequestStepModel rsMD=new RequestStepModel();
//		rsMD.setUrl("http://someulrl.com");
//		rsMD.setMethod("POST");
//		Project pj=new Project();
//		pj.setName("pj1");
//	
//		
//		
//		rsMD.setInJson(new Gson().toJson(pj));
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
//		MainRequestStepModel mr=new MainRequestStepModel();
//		
//		mr.setExpectedPerformaceResults(exPR);
//		mr.setRequestStepModel(rsMD);
//		mr.setStressConditions(sc);
//		String gs=new Gson().toJson(mr);
//		
//		Step step=new Step();
//		step.setName("Step1");
//		step.setStepKind(StepKind.SEND_MAIN_REQUEST);
//		step.setStepOrder(1L);
//		step.setStepModel(gs);
//		
//		
//		
//		
//		System.out.println(new Gson().toJson(step));
		
		
		SpringApplication.run(RestTestBuilderBoot.class, args);
	}
	


}
