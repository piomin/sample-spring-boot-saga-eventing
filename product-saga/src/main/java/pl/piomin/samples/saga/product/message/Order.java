package pl.piomin.samples.saga.product.message;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private Integer productId;
    private int productsCount;
    private OrderStatus status;
}
