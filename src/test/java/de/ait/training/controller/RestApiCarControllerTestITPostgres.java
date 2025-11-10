package de.ait.training.controller;

import de.ait.training.model.Car;
import de.ait.training.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Запускает приложение целиком на случайном порту
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiCarControllerTestITPostgres {

    //Упрощённый HTTP-клиент, встроенный в Spring Boot
    @Autowired
    TestRestTemplate restTemplate; //обект для отправик http запросов на сервер (получает ответ и сверяет его с тем что мы ожидаем)

    // @Autowired нужен для того чтобы наш spring framework обратился к spring context достал нужный bean и поместил в это поле
    // bean обычный java обект который храниться в spring репозитории и может извлекаться при нашем запросе
    @Autowired
    CarRepository repository; //является бином, достает обьекты с CarRepository и ложит в repository

    @Test
    void getAllCarsSuccess() {

        // каждый http запрос имеет тело, но GET запрос не имеет тела так как запращивает информацию, а не отправляет
        // заголовки будущего запроса нужны для куков и т.д. их нужно создавать

        /*
        http запрос состоит из тела заголовков а также имеет тип(метод)
        Тело - содержит информацию которую мы отправляем на сервер
        (например, объект автомобиля для сохранения в БД)
        Заголовки - содержат служебную информацию о запросе
        (например, информацию об авторизации, куки и др.)
        Тип запроса (method) - GET, POST, PUT, DELETE
         */

        /*Создаем объект заголовков запроса верно, чтобы сервером было воспринято правильно.
        Хотя нам пока нечего вкладывать в заголовки, их лучше все равно создать,
        хотя бы пустые, потому что некоторые веб-сервера могут вернуть ошибку,
        если в запросе совсем не будет заголовков
        */
        HttpHeaders headers = new HttpHeaders();

        // Создаем объкт http - запроса
        // Так как нам ничего не нужно вкладывать в тело запроса,
        // параметризуем запрос типиом Void
        HttpEntity<Void> request = new HttpEntity<>(headers);
        // 1:42:57
        /*
        Отправляем на наше  тестовое приложение реальный http запрос
        и получаем реальный http ответ. Это и делает метод exchange.
        4   аргумента метода:
        1. эндпоинт на который отправляються запросы
        2. тип метод запроса
        3. обьект запроса (с вложенными в него заголовками и телом)
        4. тип данных который мы ожидаем получить с сервера
        */
        // Проблема: Iterable<Car>.Class не подходит через нарущения синтаксиса, потому
        // Решение №1: мы используем массив Car[]
        ResponseEntity<Car[]> response = restTemplate.exchange(
                "/api/cars", //1. эндпоинт на который отправляються запросы
                HttpMethod.GET,  //2. тип метод запроса
                request, //обьект запроса (с вложенными в него заголовками и телом)
                Car[].class //4. тип данных который мы ожидаем получить с сервера
        );
        // Решение №2: преобразовать полученный массив в лист
        List<Car> cars = Arrays.asList(response.getBody());

        // Решение №3: использовать клас ParametrizedTypeReference
        // В этом случае никакие преобразования не нужны, сразу получаем список.
        ResponseEntity<List<Car>> response1 = restTemplate.exchange(
                "/api/cars", HttpMethod.GET, request, new ParameterizedTypeReference<List<Car>>() {
                }
        );

        // Здесь мы проверяем что же нам пришло с сервера, действительно ли ответ с сервера пришел с правильным статусом
        // ВАЖНО: в метод assertEquals нужно передавать сначала ожидаемое значение, потом действительное. Не наоборот!
        // Object expected, Object actual
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected HTTP Status Code");
        //2:14
        // Получим тело ответа из самого обьекта ответа
        Car[] body = response.getBody();

        // Проверяем есть ли вообще тело в ответе от сервера на NULL
        assertNotNull(body, "Response body should not be null");

        for (Car car : body) {
            assertNotNull(car.getId(), "Car ID should not be null");
            assertNotNull(car.getColor(), "Car Color should not be null");
            assertNotNull(car.getModel(), "Car Model should not be null");
            assertTrue(car.getPrice() >= 0, "Car Price cannot be negative");
        }
    }

    @Test
    void postNewCarSuccess() {
        HttpHeaders headers = new HttpHeaders();
        //  Поскольку мы тестируем сохранение аптоиобиля в базу данных то нам нужно
        // cоздать тестовый обьект, который мы и будем отправляьь на сервер
        Car testCar = new Car("Test color", "Test model", 7777.77);
        // В этом случае мы отправляем автомобиль в теле запроса поэтому
        // сам запрос парпмиетризуем типом CAR и вкладываем обьект автомобиля в обьект запроса
        HttpEntity<Car> request = new HttpEntity<>(testCar, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "/api/cars", HttpMethod.POST, request, Car.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Unexpected HTTP Status Code");

        Car savedCar = response.getBody();

        assertNotNull(savedCar, "Car ID should not be null");
        assertNotNull(savedCar.getId(), "Car ID should not be null");
        assertEquals(testCar.getColor(), savedCar.getColor(), "Saved car color is incorrect");
        assertEquals(testCar.getModel(), savedCar.getModel(), "Saved car Model is incorrect");
        assertEquals(testCar.getPrice(), savedCar.getPrice(), "Saved car Price is incorrect");
    }


}