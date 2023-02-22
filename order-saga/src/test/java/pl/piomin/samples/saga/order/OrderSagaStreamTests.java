package pl.piomin.samples.saga.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class OrderSagaStreamTests {

    @Autowired
    OutputDestination output;

    @Test
    void receive() {
        byte[] payload = output.receive().getPayload();
        assertTrue(payload.length > 0);
    }
}
