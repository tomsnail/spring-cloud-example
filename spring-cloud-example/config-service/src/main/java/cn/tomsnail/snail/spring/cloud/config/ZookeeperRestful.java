package cn.tomsnail.snail.spring.cloud.config;

import cn.tomsnail.snail.core.util.JsonUtil;
import cn.tomsnail.snail.core.util.string.StringUtils;
import cn.tomsnail.snail.core.util.zkclient.ZkHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/zk")
public class ZookeeperRestful {



    @GetMapping("/get")
    public String get(@RequestParam("path") String name){
        ZkHelper zkHelper = ZkHelper.getInstance("127.0.0.1:2181");
        boolean extis = zkHelper.isExtisPath(name);
        if(extis){
            Object t = zkHelper.readNode(name);
            return JsonUtil.toJson(t);
        }else{
            return "null";
        }
    }

    @GetMapping("/list")
    public String list(@RequestParam("path") String name){
        ZkHelper zkHelper = ZkHelper.getInstance("127.0.0.1:2181");
        List<String> list = zkHelper.getPath(name);
        return StringUtils.join(list.toArray(), ",");
    }

    @GetMapping("/put")
    public String put(@RequestParam("path") String name,@RequestParam("value") String value){
        ZkHelper zkHelper = ZkHelper.getInstance("127.0.0.1:2181");

        boolean extis = zkHelper.isExtisPath(name);
        if(extis){
            zkHelper.writeNode(name,value);
            Object t = zkHelper.readNode(name);
            return JsonUtil.toJson(t);
        }else{
            zkHelper.createPathWithParent(name,true);
            zkHelper.writeNode(name,value);
            Object t = zkHelper.readNode(name);
            return JsonUtil.toJson(t);
        }
    }
}
