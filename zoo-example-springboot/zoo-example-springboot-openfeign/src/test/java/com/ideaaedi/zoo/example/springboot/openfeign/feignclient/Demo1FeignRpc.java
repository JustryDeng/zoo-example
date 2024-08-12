package com.ideaaedi.zoo.example.springboot.openfeign.feignclient;

import com.ideaaedi.zoo.example.springboot.openfeign.config.SimpleFeignClientConfig;
import com.ideaaedi.zoo.example.springboot.openfeign.controller.DemoController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(url = "http://localhost:8080", name = "target-micro-service-name", configuration = SimpleFeignClientConfig.class)
@RequestMapping("/demo")
public interface Demo1FeignRpc {
    
    /**
     * 发送http请求，调用{@link DemoController#welcome()}
     */
    @GetMapping("/welcome")
    String welcome();
    
    /**
     * 发送http请求，调用{@link DemoController#addData(int, String)}
     */
    @PostMapping("/add-data")
    Map<Integer, String> addData(@RequestParam int id, @RequestParam String data);
    
    /**
     * 发送http请求，调用{@link DemoController#addDataJson(DemoController.DataRequest)}
     */
    @PostMapping("/add-data-json")
    List<DemoController.DataRequest> addDataJson(@RequestBody DemoController.DataRequest request);
    
    /**
     * 发送http请求，调用{@link DemoController#deleteData(int)}
     */
    @DeleteMapping("/delete-data/{id}")
    Map<Integer, String> deleteData(@PathVariable int id);
    
    /**
     * 发送http请求，调用{@link DemoController#getData(int)}
     */
    @GetMapping("/get-data/{id}")
    String getData(@PathVariable int id);
    
    /**
     * 发送http请求，调用{@link DemoController#updateData(int, String)}
     */
    @PutMapping("/update-data/{id}")
    Map<Integer, String> updateData(@PathVariable int id, @RequestParam String newData);
    
    /**
     * 发送http请求，调用{@link DemoController#getHeaders(Map)}
     */
    @GetMapping("/headers")
    Map<String, String> getHeaders();
}
