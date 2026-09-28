package tp.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TekServiceRegisteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TekServiceRegisteryApplication.class, args);
	}

}
