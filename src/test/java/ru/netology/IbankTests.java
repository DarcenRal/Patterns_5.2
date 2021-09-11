package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;

public class IbankTests {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLogIfValidUser() {
        Registration user = DataGenerator.generationNewValidUser();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").findBy(exactText("Продолжить")).click();
        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldNotLogIfBlockedUser() {
        Registration user = DataGenerator.generationNewBlockedUser();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").findBy(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text
                ("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldNotLogIfActiveUserInvalidLogin() {
        Registration user = DataGenerator.generateNewUserInvalidLogin();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").findBy(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text
                ("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldNotWalkWithAnEmptyLogin() {
        Registration user = DataGenerator.generationNewValidUser();
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").findBy(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub").shouldHave(text
                ("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldNotEnterIfThePasswordIsIncorrect() {
        Registration user = DataGenerator.generateNewActiveInvalidPassword();
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $$("button").findBy(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text
                ("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldDoNotEnterWithoutLoginAndPassword() {
        $$("button").findBy(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub").shouldHave(text
                ("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
        $("[data-test-id='password'] .input__sub").shouldHave(text
                ("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
    }

}
