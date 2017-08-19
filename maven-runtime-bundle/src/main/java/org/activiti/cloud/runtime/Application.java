package org.activiti.cloud.runtime;



import org.activiti.cloud.starter.configuration.ActivitiRuntimeBundle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ActivitiRuntimeBundle
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}