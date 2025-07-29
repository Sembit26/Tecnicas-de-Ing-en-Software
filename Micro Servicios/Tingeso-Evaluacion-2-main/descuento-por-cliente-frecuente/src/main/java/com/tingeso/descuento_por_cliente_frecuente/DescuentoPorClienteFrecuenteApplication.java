package com.tingeso.descuento_por_cliente_frecuente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DescuentoPorClienteFrecuenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DescuentoPorClienteFrecuenteApplication.class, args);
	}

}
