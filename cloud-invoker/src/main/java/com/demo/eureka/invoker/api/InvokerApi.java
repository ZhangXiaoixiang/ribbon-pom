package com.demo.eureka.invoker.api;

import com.demo.eureka.invoker.ribbon.MyLoadBalanced;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * spring 使用ribbon的API
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 服务查询
     *
     * @return
     */
    @Autowired
    private DiscoveryClient discoveryClient;
    /**
     * 使用springcloud默认的实现(原生ribbon)
     */
    @Autowired
    private SpringClientFactory factory;

    /**
     * 使用自定义的注解拦截器实现
     * @return
     */
    @Bean
    @MyLoadBalanced
    public  RestTemplate getMyRestTemplate(){
        return new RestTemplate();
    }

    @RequestMapping(value = "/router", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String router() {
        RestTemplate restTemplate = getRestTempalte();
//        服务名称调用
        System.out.println("使用了ribbon的配置文件");
       String json = restTemplate.getForObject("http://provider/person/10086", String.class);

        return json;

    }

@RequestMapping(value = "/userLb",method =RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
public ServiceInstance userLb(){
    ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
    System.out.println("ServiceInstance==============>"+serviceInstance);
    return serviceInstance;
}

    @RequestMapping(value = "/defaultValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String defaultValue() {
        System.out.println("==== 输出默认配置：");
        // 获取默认的配置
        ZoneAwareLoadBalancer alb = (ZoneAwareLoadBalancer) factory
                .getLoadBalancer("default");
        System.out.println("    IClientConfig: "
                + factory.getLoadBalancer("default").getClass().getName());
        System.out.println("    IRule: " + alb.getRule().getClass().getName());
        System.out.println("    IPing: " + alb.getPing().getClass().getName());
        System.out.println("    ServerList: "
                + alb.getServerListImpl().getClass().getName());
        System.out.println("    ServerListFilter: "
                + alb.getFilter().getClass().getName());
        System.out.println("    ILoadBalancer: " + alb.getClass().getName());
        System.out.println("    PingInterval: " + alb.getPingInterval());
        System.out.println("==== 输出 provider 配置：");
        // 获取 provider 的配置
        ZoneAwareLoadBalancer alb2 = (ZoneAwareLoadBalancer) factory
                .getLoadBalancer("provider");
        System.out.println("    IClientConfig: "
                + factory.getLoadBalancer("provider").getClass()
                .getName());
        System.out.println("    IRule: " + alb2.getRule().getClass().getName());
        System.out.println("    IPing: " + alb2.getPing().getClass().getName());
        System.out.println("    ServerList: "
                + alb2.getServerListImpl().getClass().getName());
        System.out.println("    ServerListFilter: "
                + alb2.getFilter().getClass().getName());
        System.out.println("    ILoadBalancer: " + alb2.getClass().getName());
        System.out.println("    PingInterval: " + alb2.getPingInterval());
        return "看idea控制台哈";
    }

    /**
     * 浏览器访问的请求,自定义拦截器
     */
    @RequestMapping(value = "/byMe", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String byMe() {
        RestTemplate restTpl = getMyRestTemplate();
        // 根据名称来调用服务，这个URI会被拦截器所置换,test任意写(反正会重定向)
        String json = restTpl.getForObject("http://test/lanJie", String.class);
        return json;
    }

    /**
     * 最终的请求都会转到这个服务
     */
    @RequestMapping(value = "/lanJie", method = RequestMethod.GET)
    @ResponseBody
    public String lanJie() {
        System.out.println("-----------调用了我,说明拦截生效了--------------哈哈");
        return "你好,我是自定义拦截器的一个简单手写---";
    }





}
