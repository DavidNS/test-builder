package com.dns.resttestbuilder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;

import javafx.application.Application;


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
		Application.launch(RestTestApplication.class, args);
	}

}
