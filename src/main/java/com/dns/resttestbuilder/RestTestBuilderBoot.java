package com.dns.resttestbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;


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
		SpringApplication.run(RestTestBuilderBoot.class, args);
	}

}
