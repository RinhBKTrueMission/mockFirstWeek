package com.example.mockfirstweek.model;

import com.example.mockfirstweek.core.Template.TemplateEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "order_details")
public class OrderDetail extends TemplateEntity {

    private Integer quanity;

    private BigDecimal price;

    private Integer amount;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Order order;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Product product;




}