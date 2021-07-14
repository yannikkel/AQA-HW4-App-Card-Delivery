import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.*;

class AppCardDeliveryTest {

    @BeforeEach
    void goToURL() {
        open("http://localhost:9999");
    }

    String datePlusNumberOfDays(int NumberOfDays) {
        LocalDate date = LocalDate.now().plusDays(NumberOfDays);
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldTestSubmitSuccess() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(10));
        $("[data-test-id=name] input").setValue("Маша Иванова");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();
        $(".notification__content").should(visible, Duration.ofSeconds(15)).shouldHave(text(datePlusNumberOfDays(10)));
    }

    @Test
    void shouldFailIfInvalidCity() {
        $("[data-test-id=city] input").setValue("Урюпинск");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(10));
        $("[data-test-id=name] input").setValue("Маша Иванова");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldFailIfInvalidDate() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(1));
        $("[data-test-id=name] input").setValue("Маша Иванова");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldFailIfInvalidName() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(10));
        $("[data-test-id=name] input").setValue("Masha Ivanova");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldFailIfInvalidPhone() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(10));
        $("[data-test-id=name] input").setValue("Маша Иванова");
        $("[data-test-id=phone] input").setValue("123");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldFailIfAgreementUnchecked() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(datePlusNumberOfDays(10));
        $("[data-test-id=name] input").setValue("Маша Иванова");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }
}
