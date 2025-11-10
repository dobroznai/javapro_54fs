package de.ait.training.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

// lombok
// Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.
@Data
@NoArgsConstructor
// Таблица в БД будет представлена через клас Car
@Entity
@Table(name = "cars")


public class Car {
    // уникальный идентификатор для каждой транзакции
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // перед каждым сохранением в БД проверяет на null
    @Column(nullable = false, name = "color")
    private String color;

    @Column(nullable = false, name = "model")
    private String model;

    @Column(nullable = false, name = "price")
    private double price;

    public Car(String color, String model, double price) {
        this.color = color;
        this.model = model;
        this.price = price;
    }
}
