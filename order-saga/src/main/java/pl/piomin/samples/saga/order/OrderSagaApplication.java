package pl.piomin.samples.saga.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import pl.piomin.samples.saga.order.model.Order;
import pl.piomin.samples.saga.order.model.OrderStatus;
import pl.piomin.samples.saga.order.repository.OrderRepository;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class OrderSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSagaApplication.class, args);
    }

    private static int num = 0;
    private BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    @Bean
    public Supplier<Order> orderEventSupplier() {
        return () -> repository.save(new Order(++num, num%10+1, num%10+1, 100, 1, OrderStatus.NEW));
    }

    @Bean
    public Supplier<Order> orderConfirmSupplier() {
        return () -> queue.poll();
    }

    @Bean
    public Consumer<Message<Order>> confirm() {
        return this::doConfirm;
    }

    @Autowired
    OrderRepository repository;

    private void doConfirm(Message<Order> msg) {
        Order o = msg.getPayload();
        log.info("Order received : {}", o);
        Order order = repository.findById(o.getId()).orElseThrow();
        if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.CONFIRMED);
            log.info("Order confirmed : {}", order);
            queue.offer(order);
        }
        repository.save(order);
    }

}
