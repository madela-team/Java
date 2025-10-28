package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInput {
    private String userId;
    private List<OrderItemInput> items;
    private String deliveryAddress;
}

