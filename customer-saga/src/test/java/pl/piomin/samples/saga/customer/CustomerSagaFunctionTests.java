package pl.piomin.samples.saga.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.messaging.Message;
import pl.piomin.samples.saga.customer.message.Order;
import pl.piomin.samples.saga.customer.message.OrderStatus;
import pl.piomin.samples.saga.customer.model.Customer;
import pl.piomin.samples.saga.customer.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerSagaFunctionTests {

    private static int amountAvailable;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    OutputDestination output;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @org.junit.jupiter.api.Order(1)
    void firstConfirm() {
        Order o = new Order();
        o.setId(1);
        o.setCustomerId(1);
        o.setAmount(1000);
        o.setStatus(OrderStatus.NEW);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/customers/reserve").body(o),
                Void.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());

        Customer c = repository.findById(1).orElseThrow();
        assertEquals(1000, c.getAmountReserved());
        amountAvailable = c.getAmountAvailable();
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void secondConfirm() {
        Order o = new Order();
        o.setId(1);
        o.setCustomerId(1);
        o.setAmount(1000);
        o.setStatus(OrderStatus.CONFIRMED);
        ResponseEntity<Void> result = restTemplate.exchange(
                RequestEntity.post("/customers/reserve").body(o),
                Void.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());

        Customer c = repository.findById(1).orElseThrow();
        assertEquals(0, c.getAmountReserved());
        assertEquals(amountAvailable, c.getAmountAvailable());
    }

//    @Test
//    @org.junit.jupiter.api.Order(3)
    void receive() throws JsonProcessingException {
        Message<byte[]> received = output.receive(3000, "orderEventSupplier");
        assertNotNull(received.getPayload());
        String json = new String(received.getPayload());

        Order o = mapper.readValue(json, Order.class);
        assertEquals(OrderStatus.IN_PROGRESS, o.getStatus());
        byte[] payload = output.receive().getPayload();
        assertTrue(payload.length > 0);
    }
}
