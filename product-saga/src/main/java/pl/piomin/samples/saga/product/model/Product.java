package pl.piomin.samples.saga.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private Integer id;
    private String name;
    private int availableItems;
    private int reservedItems;

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public int getReservedItems() {
        return reservedItems;
    }

    public void setReservedItems(int reservedItems) {
        this.reservedItems = reservedItems;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", availableItems=" + availableItems +
                ", reservedItems=" + reservedItems +
                '}';
    }
}
