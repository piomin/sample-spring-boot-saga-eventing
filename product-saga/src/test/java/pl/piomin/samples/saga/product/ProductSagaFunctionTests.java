package pl.piomin.samples.saga.product;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pl.piomin.samples.saga.product.message.Order;
import pl.piomin.samples.saga.product.message.OrderStatus;
import pl.piomin.samples.saga.product.model.Product;
import pl.piomin.samples.saga.product.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductSagaFunctionTests {

    private static int amountAvailable;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository repository;
    @Autowired
    OutputDestination output;

    @Test
    @org.junit.jupiter.api.Order(1)
    void firstConfirm() {
        Order o = new Order();
        o.setId(1);
        o.setProductId(1);
        o.setProductsCount(10);
        o.setStatus(OrderStatus.NEW);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/products/reserve").body(o),
                Void.class);
        assertTrue(result.getStatusCodeValue() == 202);

        Product p = repository.findById(1).orElseThrow();
        assertEquals(10, p.getReservedItems());
        amountAvailable = p.getAvailableItems();
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void secondConfirm() {
        Order o = new Order();
        o.setId(1);
        o.setProductId(1);
        o.setProductsCount(10);
        o.setStatus(OrderStatus.CONFIRMED);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/products/reserve").body(o),
                Void.class);
        assertTrue(result.getStatusCodeValue() == 202);

        Product p = repository.findById(1).orElseThrow();
        assertEquals(0, p.getReservedItems());
        assertEquals(amountAvailable, p.getAvailableItems());
    }

//    @Test
//    @org.junit.jupiter.api.Order(3)
    void receive() {
        byte[] payload = output.receive(3000).getPayload();
        assertTrue(payload.length > 0);
    }
}
