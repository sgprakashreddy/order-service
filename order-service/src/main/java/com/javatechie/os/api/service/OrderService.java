package com.javatechie.os.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.os.api.common.Payment;
import com.javatechie.os.api.common.TransactionRequest;
import com.javatechie.os.api.common.TransactionResponse;
import com.javatechie.os.api.entity.Order;
import com.javatechie.os.api.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    //load the properties from Config

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String ENDPOINT_URL;

    // loggers to the Order Service
    private Logger log = LoggerFactory.getLogger(OrderService.class);

    public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
        log.info("Order-Service Request ",new ObjectMapper().writeValueAsString(request));
        String response="";
        Order order =request.getOrder();  // retrive the order details from transaction request
        Payment payment=request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        //rest Call to save the data
        //  RestTemplate restTemplate = new RestTemplate();
        //Payment paymentResponse = restTemplate.postForObject("http://localhost:9191/pay/savePayment",payment, Payment.class);
        // As we have registed with the Eureka Server there is no need to use =local host
        //Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/pay/savePayment",payment, Payment.class);
        // as we are using config Server we get the above Url from config server
        Payment paymentResponse = restTemplate.postForObject(ENDPOINT_URL,payment, Payment.class);
        log.info("Payment-Service response from OrderService (SaveOrder method) Rest call ",new ObjectMapper().writeValueAsString(paymentResponse));
        response= paymentResponse.getPaymentStatus().equals("success") ? "Payment Processed and order booked" : "Failure in payment processing";

        repository.save(order);
        return new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),response);
    }

    /*
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    */

}
