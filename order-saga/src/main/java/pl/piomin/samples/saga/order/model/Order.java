package pl.piomin.samples.saga.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private Integer id;
    private Integer customerId;
    private Integer productId;
    private int amount;
    private int productCount;
    @Enumerated
    private OrderStatus status = OrderStatus.NEW;

    public Order() {
    }

    public Order(Integer id, Integer customerId, Integer productId, int amount, int productCount, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.amount = amount;
        this.productCount = productCount;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", amount=" + amount +
                ", productCount=" + productCount +
                ", status=" + status +
                '}';
    }
}
