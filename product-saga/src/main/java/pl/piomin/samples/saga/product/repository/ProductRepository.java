package pl.piomin.samples.saga.product.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.saga.product.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
