package pl.piomin.samples.saga.customer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

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
