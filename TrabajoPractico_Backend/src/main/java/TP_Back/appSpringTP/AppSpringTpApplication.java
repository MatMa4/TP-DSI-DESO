package TP_Back.appSpringTP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "TP_Back.appSpringTP.mappers")
@ComponentScan("TP_Back.appSpringTP.controladores")
@ComponentScan("TP_Back.appSpringTP.gestores")
@ComponentScan("TP_Back.appSpringTP.excepciones")
@ComponentScan("TP_Back.appSpringTP.modelo")
@ComponentScan("TP_Back.appSpringTP.DAOs")
@ComponentScan("TP_Back.appSpringTP.DTOs")
public class AppSpringTpApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSpringTpApplication.class, args);
	}

}
