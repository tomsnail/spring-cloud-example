package cn.tomsnail.snail.spring.cloud.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/springboot")
@RefreshScope
public class SpringBootRestful implements ISpringBootRestful{

    @Value("${testconfig}")
    private String testConfig;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISpringBootRestful springBootRestful;


    @RequestMapping(value = "/test/{name}", method = RequestMethod.GET)
    @Override
    public String name(@PathVariable("name") String name){
        return name + new Date();
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String test1(){
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("Spring-Boot-Example1");
        if(CollectionUtils.isEmpty(serviceInstanceList)){
            return "null";
        }
        String serviceUri = String.format("%s/springboot/test/test1",serviceInstanceList.get(0).getUri().toString());
        RestTemplate restTemplate = new RestTemplate();
        String v = restTemplate.getForObject(serviceUri,String.class);
        return v;
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test2(){
        String v = restTemplate.getForObject("http://Spring-Boot-Example1/springboot/test/test2",String.class);
        return v;
    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public String test3(){
        return springBootRestful.name("test3");
    }

    @RequestMapping(value = "/testconfig", method = RequestMethod.GET)
    public String getTestConfig(){
        return testConfig;
    }

}
