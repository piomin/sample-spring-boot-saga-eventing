package pl.piomin.samples.saga.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import pl.piomin.samples.saga.product.message.Order;
import pl.piomin.samples.saga.product.message.OrderStatus;
import pl.piomin.samples.saga.product.model.Product;
import pl.piomin.samples.saga.product.repository.ProductRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class ProductSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductSagaApplication.class, args);
    }

    @Autowired
    private ProductRepository repository;

    BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    @Bean
    public Supplier<Order> orderEventSupplier() {
        return () -> queue.poll();
    }

    @Bean
    public Consumer<Message<Order>> reserve() {
        return this::doReserve;
    }

    private void doReserve(Message<Order> msg) {
        Order order = msg.getPayload();
        log.info("Body: {}", order);
        Product product = repository.findById(order.getProductId()).orElseThrow();
        log.info("Product: {}", product);
        if (order.getStatus() == OrderStatus.NEW) {
            product.setReservedItems(product.getReservedItems() + order.getProductsCount());
            product.setAvailableItems(product.getAvailableItems() - order.getProductsCount());
            order.setStatus(OrderStatus.IN_PROGRESS);
            queue.offer(order);
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            product.setReservedItems(product.getReservedItems() - order.getProductsCount());
        }
        repository.save(product);
    }
}
