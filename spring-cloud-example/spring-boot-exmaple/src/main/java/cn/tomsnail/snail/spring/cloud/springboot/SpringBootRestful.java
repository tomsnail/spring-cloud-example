package cn.tomsnail.snail.spring.cloud.springboot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/springboot")
public class SpringBootRestful {

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String info(@PathVariable("name") String name){
        return name;
    }

}
