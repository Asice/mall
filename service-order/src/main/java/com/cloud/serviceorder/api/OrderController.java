package com.cloud.serviceorder.api;

import com.cloud.serviceorder.api.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private Logger logger= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * 远程调用测试
     * @param orderNo
     * @return
     */
    @GetMapping("/{orderNo}")
    public ResponseEntity<?> getOrderByNo(@PathVariable("orderNo")String orderNo){
        try {
            return orderService.getOrderByNo(orderNo);
        }catch (Exception e){
            //todo: do something
            e.printStackTrace();
            return new ResponseEntity<>("请求失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * zipkin链路监控测试
     * @return
     */
    @GetMapping("/create")
    public String create(){
        logger.info("create__生产订单号555688555");
        return "create order 555688555";
    }
}
