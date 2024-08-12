package com.ideaaedi.zoo.example.springboot.openfeign.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final Map<Integer, String> dataMap = new ConcurrentHashMap<>(16);
    
    {
        dataMap.put(1, "Data 1");
        dataMap.put(9527, "Data 9527");
    }
    
    @GetMapping("/welcome")
    public String welcome() {
        log.info("Received welcome request.");
        return "Welcome to the demo application!";
    }

    @PostMapping("/add-data")
    public Map<Integer, String> addData(@RequestParam int id, @RequestParam String data) {
        log.info("Adding data with ID: {} and value: {}", id, data);
        dataMap.put(id, data);
        return dataMap;
    }

    @PostMapping("/add-data-json")
    public List<DataRequest> addDataJson(@RequestBody DataRequest request) {
        log.info("Adding data from JSON request: {}", request);
        dataMap.put(request.getId(), request.getData());
        return dataMap.entrySet().stream().map(entry -> {
            DataRequest dataRequest = new DataRequest();
            dataRequest.setId(entry.getKey());
            dataRequest.setData(entry.getValue());
            return dataRequest;
        }).toList();
    }

    @DeleteMapping("/delete-data/{id}")
    public Map<Integer, String> deleteData(@PathVariable int id) {
        log.info("Deleting data with ID: {}", id);
        dataMap.remove(id);
        return dataMap;
    }

    @GetMapping("/get-data/{id}")
    public String getData(@PathVariable int id) {
        log.info("Getting data for ID: {}", id);
        return dataMap.getOrDefault(id, "Data not found");
    }

    @PutMapping("/update-data/{id}")
    public Map<Integer, String> updateData(@PathVariable int id, @RequestParam String newData) {
        log.info("Updating data with ID: {} to value: {}", id, newData);
        dataMap.put(id, newData);
        return dataMap;
    }

    @GetMapping("/headers")
    public Map<String, String> getHeaders(@RequestHeader Map<String, String> headers) {
        log.info("Retrieving headers from request. {}", headers);
        return headers;
    }

    @Data
    public static class DataRequest {
        private int id;
        private String data;
    }
}
