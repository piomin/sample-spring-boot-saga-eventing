package pl.piomin.samples.saga.order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pl.piomin.samples.saga.order.model.Order;
import pl.piomin.samples.saga.order.model.OrderStatus;
import pl.piomin.samples.saga.order.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderSagaFunctionTests {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private OrderRepository repository;

    @Test
    @org.junit.jupiter.api.Order(1)
    void firstConfirm() {
        Order o = new Order();
        o.setId(1);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/orders/confirm").body(o),
                Void.class);
        assertTrue(result.getStatusCodeValue() == 202);

        o = repository.findById(1).orElseThrow();
        assertEquals(OrderStatus.IN_PROGRESS, o.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void secondConfirm() {
        Order o = new Order();
        o.setId(1);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/orders/confirm").body(o),
                Void.class);
        assertTrue(result.getStatusCodeValue() == 202);

        o = repository.findById(1).orElseThrow();
        assertEquals(OrderStatus.CONFIRMED, o.getStatus());
    }

//    @Test
    void confirmNotFound() {
        Order o = new Order();
        o.setId(10);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/orders/confirm").body(o),
                Void.class);
        assertTrue(result.getStatusCodeValue() == 202);
        assertFalse(repository.findById(10).isPresent());
    }
}
