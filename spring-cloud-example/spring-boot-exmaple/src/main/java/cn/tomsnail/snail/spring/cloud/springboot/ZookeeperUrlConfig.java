package cn.tomsnail.snail.spring.cloud.springboot;

import cn.tomsnail.snail.core.util.zkclient.ZkHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author yangsong
 * @version 0.1
 * @ClassName ZookeeperUrlConfig
 * @Description TODO
 * @date 2019/5/2011:09
 **/
@Component
@ConditionalOnProperty(value = "spring.cloud.zookeeper.snail-api.enabled",havingValue = "true")
public class ZookeeperUrlConfig implements ApplicationListener<WebServerInitializedEvent> {


    private Logger logger = LoggerFactory.getLogger(SpringmvcInitializingBean.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private int serverPort;

    @Value("${spring.application.name}")
    private String serviceName;
    @Value("${spring.cloud.zookeeper.connect-string}")
    private String zookeeperAddress;


    @Value("${spring.cloud.zookeeper.snail-api.root}")
    private String zkRoot = "/snail/springboot/urls";

    @Value("${spring.cloud.zookeeper.snail-api.enabled}")
    private boolean zkEnable = true;

    @Value("${spring.cloud.zookeeper.snail-api.temp}")
    private boolean zkTemp = false;

    public int getPort() {
        return this.serverPort;
    }
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        logger.info(">>>>>>>>>>>>>>>>>>>>onApplicationEvent");
        if(!zkEnable){
            return;
        }

        this.serverPort = event.getWebServer().getPort();
        try {
            afterPropertiesSet();
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        ZkHelper zkHelper = ZkHelper.getInstance(zookeeperAddress);
        zkRoot = zkRoot+"/"+serviceName;
        if(!zkHelper.isExtisPath(zkRoot)){
            zkHelper.createPathWithParent(zkRoot,true);
        }

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            String name = method.getMethod().getName();
            if(!zkHelper.isExtisPath(zkRoot+"/"+name)){
                zkHelper.createPathWithParent(zkRoot+"/"+name,false);
            }
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                String urlStr = String.format("http://%s:%s%s",address(),getPort(),url);
                urlStr = URLEncoder.encode(urlStr,"UTF-8");
                if(!zkHelper.isExtisPath(zkRoot+"/"+name+"/"+urlStr)){
                    zkHelper.createPath(zkRoot+"/"+name+"/"+urlStr,zkTemp);
                }
                logger.info("####################### {} -- {} #######################",name,urlStr);
            }
        }
    }


    public String address(){
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
        }
        String ip = localHost.getHostAddress();  // 返回格式为：xxx.xxx.xxx
        return ip;
    }
}
