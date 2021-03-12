package pl.piomin.samples.saga.order.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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
