package cn.tomsnail.snail.spring.cloud.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;


public class SpringmvcInitializingBean implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SpringmvcInitializingBean.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private ZookeeperUrlConfig serverConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                logger.info("####################### http://{}:{}{}#######################",serverConfig.address(),serverConfig.getPort(),url);
            }
        }
    }
}
