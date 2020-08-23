package com.dns.resttestbuilder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;
import com.dns.resttestbuilder.configuration.ReservedNames;


/**
 * Launcher de la applicacion.
 * @author David Nowakowski Stachyra
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({AsyncConfiguration.class, ReservedNames.class /*, OtherConf.class*/})
public class RestTestBuilderBoot {


	public static void main(String[] args) {
		SpringApplication.run(RestTestBuilderBoot.class, args);
	}
	


}
