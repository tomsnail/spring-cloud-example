package cn.tomsnail.snail.spring.cloud.springboot;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SPRING-BOOT-EXAMPLE")
public interface ISpringBootRestful {

    @RequestMapping(value = "/springboot/test/{name}", method = RequestMethod.GET)
    public String name(@PathVariable("name") String name);
}
