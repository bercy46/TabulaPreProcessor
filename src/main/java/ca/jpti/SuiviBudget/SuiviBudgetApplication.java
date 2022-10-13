package ca.jpti.SuiviBudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class })
public class SuiviBudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuiviBudgetApplication.class, args);
	}
}
