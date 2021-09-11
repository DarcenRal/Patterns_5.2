package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static Faker faker =new Faker(new Locale("ru"));

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void nowRegistration(Registration registration) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(registration) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static Registration generationNewValidUser() {
        String login = faker.name().firstName().toLowerCase();
        String password = faker.internet().password();
        nowRegistration(new Registration(login, password, "active"));
        return new Registration(login, password, "active");
    }

    public static Registration generationNewBlockedUser() {
        String login = faker.name().firstName().toLowerCase();
        String password = faker.internet().password();
        nowRegistration(new Registration(login, password, "blocked"));
        return new Registration(login, password, "blocked");
    }

    public static Registration generateNewUserInvalidLogin() {
        String password = faker.internet().password();
        String status = "active";
        nowRegistration(new Registration("edik", password, status));
        return new Registration("login", password, status);
    }

    public static Registration generateNewActiveInvalidPassword() {
        String login = faker.name().firstName().toLowerCase();
        String status = "active";
        nowRegistration(new Registration(login, "password", status));
        return new Registration(login, "12345", status);
    }
}
