package com.viewnext.gameofarce.noel.clientribbonbalancer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

//@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
public class ClientRibbonBalancerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClientRibbonBalancerApplication.class, args);
	}

	@Bean
	@LoadBalanced()
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.setConnectTimeout(200).setReadTimeout(200).build();
	}
	
}

@RestController
class ClienteController {
	
	private static final Log LOGGER = LogFactory.getLog(ClientRibbonBalancerApplication.class);

	@Autowired
	private ClienteService service;
	
	@GetMapping("/get-services")
	public List<ServiceInstance> getServiceByName() {
		return this.service.getServiceByName();
	}
	
	@GetMapping("/get-port")
	public String getPort() {
		LOGGER.info("LOGSTASH ClientRibbonBalancerApplication - getPort INI");
		return this.service.getPort();
	}
	
	@GetMapping("/get-info")
	public String getInfo() {
		LOGGER.info("LOGSTASH ClientRibbonBalancerApplication - getInfo INI");
		String port = this.service.getPort();
		String hostName = this.service.getHostName();
		String address = this.service.getHostAddress();
		return String.format("Host: %s - IP: %s - Port: %s", hostName, address, port);
	}
	
	@GetMapping("/get-message")
	public String getMessage() {
		LOGGER.info("LOGSTASH ClientRibbonBalancerApplication - getMessage INI");
		return this.service.getMessage();
	}
	
	@GetMapping("/greet")
	public String greet() {
		LOGGER.info("LOGSTASH ClientRibbonBalancerApplication - greet INI");
		return this.service.greet();
	}
}

@Service
interface ClienteService {
	
	List<ServiceInstance> getServiceByName();
	
	String getPort();
	
	String getHostName();
	
	String getHostAddress();
	
	String getMessage();
	
	String greet();
}


@Service
@RefreshScope
class ClienteServiceImpl implements ClienteService {
	

	@Value("${message:Hello default}")
	private String message;
	
	@Value("${default-port:808x}")
	private String defaultPort;
	
	@Value("${default-host-name:localhost}")
	private String defaultHostName;
	
	@Value("${default-host-address:127.0.0.1}")
	private String defaultHostAddress;
	 
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	public List<ServiceInstance> getServiceByName() {
		return this.restTemplate.getForObject("http://a-bootiful-client/service-instances/a-bootiful-client", List.class);
	}
	
	@HystrixCommand(fallbackMethod = "defaultPort")
	public String getPort() {
		return this.restTemplate.getForObject("http://a-bootiful-client/get-port", String.class);
	}
	
	@HystrixCommand(fallbackMethod = "defaultHostName")
	public String getHostName() {
		return this.restTemplate.getForObject("http://a-bootiful-client/get-host-name", String.class);
	}
	
	@HystrixCommand(fallbackMethod = "defaultHostAddress")
	public String getHostAddress() {
		return this.restTemplate.getForObject("http://a-bootiful-client/get-host-address", String.class);
	}
	
	@GetMapping("/greet")
	public String greet() {
		return "ClientRibbonBalancerApplication: " + this.restTemplate.getForObject("http://a-bootiful-client/greet", String.class);
	}
	
	public String defaultPort() {
		return this.defaultPort;
	}
	
	public String defaultHostName() {
		return this.defaultHostName;
	}
	
	public String defaultHostAddress() {
		return this.defaultHostAddress;
	}
	
	public String getMessage() {
		return this.message;
	}
}
