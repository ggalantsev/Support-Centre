package ggalantsev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SpringbootandbootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootandbootstrapApplication.class, args);
	}

}
