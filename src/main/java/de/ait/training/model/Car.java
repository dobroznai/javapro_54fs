package de.ait.training.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


// либо просто добавляем аннотацию @data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class Car {
    private long id;
    private String color;
    private String model;
    private int price;
}
