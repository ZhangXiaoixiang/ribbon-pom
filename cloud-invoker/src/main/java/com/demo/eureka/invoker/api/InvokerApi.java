package com.demo.eureka.invoker.api;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * InvokerApi
 *
 * @author 10905 2019/1/26
 * @version 1.0
 */
@RestController
@Configuration
public class InvokerApi {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTempalte() {
        return new RestTemplate();
    }

    /**
     * 服务查询
     *
     * @return
     */
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/router", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String router() {
        RestTemplate restTemplate = getRestTempalte();
//        服务名称调用
        System.out.println("使用了ribbon的配置文件");
       String json = restTemplate.getForObject("http://provider/person/10086", String.class);

        return json;

    }









    @RequestMapping(value = "/getPlayerInfoList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getPlayerInfoList() {
        RestTemplate restTemplate = getRestTempalte();
//        服务名称调用
        String json = restTemplate.getForObject("http://provider/getPlayerInfoList/1/5", String.class);
        return json;

    }



    /**
     * 添加查询服务列功能
     */
    private List<ServiceInstance> getServiceInstances() {
//        获取访问列表
        List<String> instances = discoveryClient.getServices();
        List<ServiceInstance> result = new ArrayList<ServiceInstance>();
        for (String instance : instances) {
            /**
             * 将列表转换成服务集合
             */
            List<ServiceInstance> list = discoveryClient.getInstances(instance);
            result.addAll(list);
        }
        return result;

    }


    /**
     * 服务查询例子
     * @return
     */
    @RequestMapping(value = "/router2", method = RequestMethod.GET)
    public String router2() {

/**
 * 查询服务列表
 */
        List<ServiceInstance> serviceInstances = getServiceInstances();
        /**
         * 输出服务信息列表
         */
        for (ServiceInstance serviceInstance : serviceInstances) {
            EurekaDiscoveryClient.EurekaServiceInstance esi = (EurekaDiscoveryClient.EurekaServiceInstance) serviceInstance;
            InstanceInfo instanceInfo = esi.getInstanceInfo();
            System.out.println("没有查询到服务,那么不会显示DOWN的,只有UP会显示");

            System.out.println("服务名称:  "+instanceInfo.getAppName() + "   服务ID:  " + instanceInfo.getInstanceId() + "   服务目前的状态:     " + instanceInfo.getStatus());

        }

        return "";


    }


}
