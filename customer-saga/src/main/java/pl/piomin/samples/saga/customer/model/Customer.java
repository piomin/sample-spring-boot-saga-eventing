package pl.piomin.samples.saga.customer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {

    @Id
    private Integer id;
    private String name;
    private int amountAvailable;
    private int amountReserved;
}
