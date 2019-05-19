package cn.tomsnail.snail.spring.cloud.eureka;

import com.netflix.appinfo.InstanceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EurekaStateChangeListener {


    @Autowired
    private DiscoveryClient discoveryClient;


    @EventListener
    public void listen(EurekaInstanceCanceledEvent eurekaInstanceCanceledEvent) {
        //服务断线事件
        System.out.println("EurekaInstanceCanceledEvent");
        String appName = eurekaInstanceCanceledEvent.getAppName();
        String serverId = eurekaInstanceCanceledEvent.getServerId();
        System.out.println(appName);
        System.out.println(serverId);
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        System.out.println("EurekaInstanceRegisteredEvent");
        InstanceInfo instanceInfo = event.getInstanceInfo();


    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        System.out.println("EurekaInstanceRenewedEvent");
        System.out.println(event.getAppName());
        System.out.println(event.getServerId());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+event.getSource());
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> i am start");
    }


}
