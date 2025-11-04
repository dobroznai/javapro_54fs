package de.ait.training.controller;

import de.ait.training.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

//Запускает приложение целиком на случайном порту
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiCarControllerTestITPostgres {

    //Упрощённый HTTP-клиент, встроенный в Spring Boot
    @Autowired
    TestRestTemplate restTemplate; //обект для отправик http запросов на сервер (получает ответ и сверяет его с тем что мы ожидаем)

    // @Autowired нужен для того чтобы наш spring franework обратился к spring context достал нужный bean и поместил в это поле
    // bean обычный java обект который храниться с spring репозитории и может извлекаться при нашем запросе
    @Autowired
    CarRepository repository; //является бином, достает обьекты с CarRepository и ложит в repository

    @Test
    public void getAllCarsSuccess() {

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
        // Так как нам нмчего не нужно вкладывать в тело запроса,
        // параметризуем запрос типиом Void
        HttpEntity<Void> request = new HttpEntity<>(headers);
        // 1:42:57
    }


}