package com.cloud.serviceadmin.service.feign;

import com.cloud.serviceadmin.service.feign.fallback.FeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-ORDER", fallback = FeignFallback.class,decode404 =true )
public interface FeignService {

    //请求SERVICE-ORDER服务的/api/order/{orderNo}接口
    @GetMapping("/api/order/{orderNo}")
    ResponseEntity<?> getOrderByNo(@PathVariable("orderNo") String orderNo);

}
