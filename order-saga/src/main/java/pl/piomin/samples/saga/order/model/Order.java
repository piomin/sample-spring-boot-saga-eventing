package pl.piomin.samples.saga.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Id
    private Integer id;
    private Integer customerId;
    private Integer productId;
    private int amount;
    private int productCount;
    @Enumerated
    private OrderStatus status = OrderStatus.NEW;
}
