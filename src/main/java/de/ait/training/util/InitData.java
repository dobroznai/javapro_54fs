package de.ait.training.util;

import de.ait.training.model.Car;
import de.ait.training.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// При старте приложения происходит инициализация приложения с заполненными данными в базу данных
@Configuration
public class InitData {
    // выполняет run один раз за всю жизнь приложения после того как все контакты с базы данных подключены, одним щелчком вписать в БД тестовые данные
    @Bean
    CommandLineRunner init(CarRepository carRepository) {
        return args -> {
            // проверка на дублирование данных
            if (carRepository.count() == 0) {
                carRepository.save(new Car("black", "BMW X5", 25000));
                carRepository.save(new Car("green", "Audi A4", 15000));
                carRepository.save(new Car("white", "MB A220", 18000));
                carRepository.save(new Car("red", "Ferrari", 250000));
            }
        };
    }
}

