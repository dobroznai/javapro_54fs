package de.ait.training.controller;

import de.ait.training.model.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//Запускает приложение целиком на случайном порту
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
// Использует тестовый профиль и базу H2
@ActiveProfiles("test")


public class RestApiControllerIT {

    //Упрощённый HTTP-клиент, встроенный в Spring Boot
    @Autowired
    TestRestTemplate restTemplate;

    //Подставляет фактический порт, на котором работает встроенный сервер
    @LocalServerPort
    private int port;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // Обобщенный метод, чтобы не повторять доп строчки кода
    private List<Car> getCarsFromUrl(String path, HttpStatus expectedStatus) {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url(path),
                Car[].class);
        assertThat(response.getStatusCode())
                .isEqualTo(expectedStatus);
        assertThat(response.getHeaders().getContentType().toString())
                .isEqualTo("application/json");
        return Arrays.asList(response.getBody());
    }

    // ------------------- GET /cars -------------------
    // тесты всех авто

    @Test
    // Добавляет читаемое описание теста в отчётах
    @DisplayName("GET /api/cars - all 4 cars in database were found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturnAllCarsWhenDataExists() {
        List<Car> cars = getCarsFromUrl("/api/cars", HttpStatus.OK);
        // Нашел вариант проверки всех авто со списка в seed_cars.sql без учета порядка в списке но по наличию с единым стилем AssertJ.
        assertThat(cars)
                .hasSize(4)
                .extracting(Car::getModel)
                .containsExactlyInAnyOrder("BMW x5", "Audi A4", "MB A220", "Ferrari");
        // Второй вариант проверить на возврат верных данные с базы через первый елемент в seed_cars.sql
        // AssertionsForClassTypes.assertThat(cars.get(0).getModel()).isEqualTo("BMW x5");
    }

    // сделано по заданию, но я все же добавил в контрллер логирование по даному get запросу с возможностью прописать статус NOT_FOUND, если база будет пустая
    @Test
    @DisplayName("GET /api/cars - empty DB, 0 cars were found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql"})
    void testShouldReturnEmptyListWhenNoCars() {
        List<Car> cars = getCarsFromUrl("/api/cars", HttpStatus.OK);
        assertThat(cars).isEmpty();
    }

    // ------------------- GET /cars/color -------------------
    // тесты по цвету

    @Test
    @DisplayName("GET /api/cars/color/red - 1 car was found with color: ReD, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturnCarWhenColorFound() {
        List<Car> cars = getCarsFromUrl("/api/cars/color/ReD", HttpStatus.OK);

        // 1 вариант AssertJ
        assertThat(cars)
                .hasSize(1)
                .extracting(Car::getColor)
                .containsExactly("red");

        // 2 вариант
        //assertThat(cars.get(0).getColor()).isEqualTo("red");
    }

    @Test
    @DisplayName("GET /api/cars/color/purple - color not found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturn404WhenColorNotFound() {
        List<Car> cars = getCarsFromUrl("/api/cars/color/purple", HttpStatus.NOT_FOUND);
        assertThat(cars).isEmpty();
    }

    // ------------------- GET /cars/price -------------------
    // тесты по цене

    @Test
    @DisplayName("GET /api/cars/price/between/10000/30000 - 3 cars were found, status OK ")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturnCarsWithinPriceRange() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/between/10000/30000", HttpStatus.OK);
        assertThat(cars)
                .hasSize(3)
                .extracting(Car::getModel)
                .containsExactlyInAnyOrder("BMW x5", "Audi A4", "MB A220");
    }

    @Test
    @DisplayName("GET /api/cars/price/between/100/500 - 0 cars were found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturn404WhenPriceRangeNotFound() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/between/100/500", HttpStatus.NOT_FOUND);
        assertThat(cars).isEmpty();
    }

    @Test
    @DisplayName("GET /api/cars/price/between/30000/10000 - wrong min and max price, 0 cars were found, status BAD_REQUEST")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturn400WhenPriceRangeInvalid() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/between/30000/10000", HttpStatus.BAD_REQUEST);
        assertThat(cars).isEmpty();
    }

    @Test
    @DisplayName("GET /api/cars/price/between/15000/15000 - 1 car was found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldIncludeCarsWithPriceEqualToMinAndMax() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/between/15000/15000", HttpStatus.OK);
        assertThat(cars)
                .hasSize(1)
                .extracting(Car::getModel)
                .containsExactly("Audi A4");
    }

    @Test
    @DisplayName("GET /api/cars/price/under/20000 - 2 cars were found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturnCarsWithPriceUnder20000() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/under/20000", HttpStatus.OK);
        assertThat(cars)
                .hasSize(2)
                .extracting(Car::getModel)
                .containsExactly("Audi A4", "MB A220");
    }

    @Test
    @DisplayName("GET /api/cars/price/under/1000 - 0 cars were found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturn404WhenNoCarsWithPriceUnder1000() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/under/1000", HttpStatus.NOT_FOUND);
        assertThat(cars).isEmpty();
    }

    @Test
    @DisplayName("GET /api/cars/price/over/25000 - 2 cars were found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturnCarsWithPriceOver25000() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/over/25000", HttpStatus.OK);
        assertThat(cars)
                .hasSize(2)
                .extracting(Car::getModel)
                .containsExactly("BMW x5", "Ferrari");
    }

    @Test
    @DisplayName("GET /api/cars/price/over/1000000 - 0 cars were found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testShouldReturn404WhenNoCarsWithPriceOver1000000() {
        List<Car> cars = getCarsFromUrl("/api/cars/price/over/1000000", HttpStatus.NOT_FOUND);
        assertThat(cars).isEmpty();
    }
}

