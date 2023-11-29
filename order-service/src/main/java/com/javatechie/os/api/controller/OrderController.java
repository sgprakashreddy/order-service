package com.javatechie.os.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javatechie.os.api.common.Payment;
import com.javatechie.os.api.common.TransactionRequest;
import com.javatechie.os.api.common.TransactionResponse;
import com.javatechie.os.api.entity.Order;
import com.javatechie.os.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/bookOrder")
//    private Order bookOrder(@RequestBody Order order){
//        return service.saveOrder(order);
// }

    private TransactionResponse bookOrder(@RequestBody TransactionRequest request) throws JsonProcessingException {
//        Order order =request.getOrder();  // retrive the order details from transaction request
//        Payment payment=request.getPayment();
//        payment.setOrderId(order.getId());
//        payment.setAmount(order.getPrice());
        return service.saveOrder(request);
    }
}
