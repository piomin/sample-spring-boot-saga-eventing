package pl.piomin.samples.saga.customer.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.saga.customer.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
