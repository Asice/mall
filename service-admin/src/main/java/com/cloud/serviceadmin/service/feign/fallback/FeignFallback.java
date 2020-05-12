package com.cloud.serviceadmin.service.feign.fallback;

import com.cloud.serviceadmin.service.feign.FeignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FeignFallback implements FeignService {
    @Override
    public ResponseEntity<?> getOrderByNo(String orderNo) {
        System.out.println("请求失败，触发熔断");
        return new ResponseEntity<>("Server 请求失败！" , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
