package pl.piomin.samples.saga.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.GenericMessagingTemplate;
import org.springframework.web.client.RestTemplate;
import pl.piomin.samples.saga.customer.message.Order;
import pl.piomin.samples.saga.customer.message.OrderStatus;
import pl.piomin.samples.saga.customer.model.Customer;
import pl.piomin.samples.saga.customer.repository.CustomerRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class CustomerSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerSagaApplication.class, args);
    }

    private BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    @Autowired
    private CustomerRepository repository;

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
        Customer customer = repository.findById(order.getCustomerId()).orElseThrow();
        log.info("Customer: {}", customer);
        if (order.getStatus() == OrderStatus.NEW) {
            customer.setAmountReserved(customer.getAmountReserved() + order.getAmount());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getAmount());
            order.setStatus(OrderStatus.IN_PROGRESS);
            queue.offer(order);
            log.info("Reserved: {}", customer);
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getAmount());
        }
        repository.save(customer);
    }
}
