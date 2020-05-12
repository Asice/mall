package com.cloud.serviceorder.api;

import com.cloud.serviceorder.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order_provider")
public class OrderProviderController {

    @Autowired
    private OrderService orderService;

    /**
     * rabbitmq 测试
     * @return
     */
    @GetMapping("/create")
    public ResponseEntity<?> create(){
        try {
            return orderService.create();
        }catch (Exception e){
            //todo: do something
            e.printStackTrace();
            return new ResponseEntity<>("请求失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
