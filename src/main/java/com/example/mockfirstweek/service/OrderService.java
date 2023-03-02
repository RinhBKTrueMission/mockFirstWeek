package com.example.mockfirstweek.service;

import com.example.mockfirstweek.core.Template.TemplateService;
import com.example.mockfirstweek.model.Order;
import com.example.mockfirstweek.reponsitory.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends TemplateService<Order, Long> {
    @Autowired
    private OrderRepository orderRepository;


}
