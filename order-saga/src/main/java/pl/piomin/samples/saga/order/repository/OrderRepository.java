package pl.piomin.samples.saga.order.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.saga.order.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
