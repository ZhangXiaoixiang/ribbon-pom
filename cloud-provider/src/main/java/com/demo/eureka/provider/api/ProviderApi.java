package com.demo.eureka.provider.api;


import com.demo.eureka.provider.model.PlayerInfo;
import com.demo.eureka.provider.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProviderApi
 *
 * @author 10905 2019/1/26
 * @version 1.0
 */
@RestController
public class ProviderApi {
    @Autowired
    private PlayerInfoService playerInfoService;

    @RequestMapping(value = "/person/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String findPlayerList2(@PathVariable("id") Integer id, HttpServletRequest request) {
        return "你好,你输入的id是:  " + id+"   请求的URL:"+request.getRequestURL().toString();
    }

    @RequestMapping(value = "/getPlayerInfoList/{startPage}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlayerInfo> getPlayerInfoList(@PathVariable("startPage") Integer startPage, @PathVariable("pageSize") Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("startPage", startPage);
        map.put("pageSize", pageSize);
        System.out.println("进入了提供者服务调用层===>" + playerInfoService.getPlayerInfoByCondition(map));

        return playerInfoService.getPlayerInfoByCondition(map);
    }
}
