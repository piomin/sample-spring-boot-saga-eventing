package pl.piomin.samples.saga.customer.message;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private Integer customerId;
    private int amount;
    private OrderStatus status;
}
