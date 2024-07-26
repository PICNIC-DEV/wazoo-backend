package wazoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Application {
//test2
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
