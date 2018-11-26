package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

// De la Documentación de Spring: "@EnableDiscoveryClient is no longer required. You can put a DiscoveryClient implementation on the classpath to cause the Spring Boot application to register with the service discovery server."
//@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
    
}

@RestController
class ServiceInstanceRestController {
	private static final Log LOGGER = LogFactory.getLog(ServiceInstanceRestController.class);

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Autowired
    private Environment enviroment;

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
    
    @RequestMapping("/get-port")
    public String getPortMicroservice() {
    	LOGGER.info("LOGSTASH EurekaClientApplication - getPort INI");
    	return this.enviroment.getProperty("local.server.port");
    }
    
    @RequestMapping("/get-host-name")
    @HystrixCommand(fallbackMethod = "defaultHostName")
    public String getHostNameMicroservice() throws UnknownHostException {
    	LOGGER.info("LOGSTASH EurekaClientApplication - getHostName INI");
    	return InetAddress.getLocalHost().getHostName();
    }
    
    @RequestMapping("/get-host-address")
    public String getHostAddressMicroservice() throws UnknownHostException {
    	LOGGER.info("LOGSTASH EurekaClientApplication - getHostAddress INI");
    	return InetAddress.getLocalHost().getHostAddress();
    }
    
    @GetMapping("/greet")
	public String getServiceByName() {
		LOGGER.info("LOGSTASH ClientServerBalancerApplication - greet INI");
		String hostname = null;
		String ip = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			hostname = "'Oh Oh ... unknown hostname'";
		}
		LOGGER.info("LOGSTASH ClientServerBalancerApplication - hostname: " + hostname);
		return "Hellow from " + hostname + " / " + ip + "!!";
	}
    
    
    public String defaultHostName() {
		return "default-hostname-client";
	}
    
}
