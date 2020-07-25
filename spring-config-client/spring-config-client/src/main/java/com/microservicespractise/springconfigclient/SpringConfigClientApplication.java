package com.microservicespractise.springconfigclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@EnableEurekaClient
@EnableAutoConfiguration
@SpringBootApplication
public class SpringConfigClientApplication {

	 
	    public static void main(String[] args) {
	        SpringApplication.run(SpringConfigClientApplication.class, args);
	    }
	     
	    @Autowired
	    public void setEnv(Environment e)
	    {
	        System.out.println(e.getProperty("msg"));
	    }
	}
	 
	@RefreshScope
	@RestController
	class MessageRestController {
		
		
	    RestTemplate restTemplate;
		
	    public RestTemplate getRestTemplate() {
			return restTemplate;
		}
	    
	    @Autowired
		public void setRestTemplate(@Lazy RestTemplate restTemplate) {
			this.restTemplate = restTemplate;
		}

		@Value("${msg:Config Server is not working. Please check...}")
	    private String msg;
	 
	    @GetMapping("/msg")
	    public String getMsg() {
	    	ResponseEntity<LimitConfiguration> response = restTemplate.getForEntity("http://limits-service/limits", LimitConfiguration.class);
	        
	    	return this.msg+ ":maximum value from limits service is:"+response.getBody().getMaximum();
	    }
	    
	    @Bean
	    @LoadBalanced
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	}

