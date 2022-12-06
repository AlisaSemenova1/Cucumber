package org.example;

import cucumber.api.PendingException;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.logging.Logger;

public class AddBookingSteps {

    Logger log = Logger.getLogger(TokenSteps.class.getName());
    Response response;
    String firstname;
    String lastname;
    String jsonString;
    int id;
    String baseUri = "https://restful-booker.herokuapp.com/booking/";

    @Дано("^я ввожу \"([^\"]*)\"  и \"([^\"]*)\"$")
    public void addBooking(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    private void getId(String body) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body(body).post(baseUri);
    }

    @Когда("^я создаю бронирование и получаю id$")
    public void createBooking() {
        getId("{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "    \"lastname\" : \"" + lastname + "\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-11-20\",\n" +
                "        \"checkout\" : \"2022-11-21\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}");
        jsonString = response.asString();
    }

    @Тогда("^я проверяю бронирование$")
    public void checkBooking() {
        id = JsonPath.from(jsonString).get("bookingid");
        log.info(" Создано бронирование " + firstname + " " + lastname + " c id: " + id);
    }

    @И("^я создаю бронирование с неверным телом$")
    public void createFailBooking() {
        log.info("Создаю бронирование с неверным телом");
        getId("{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "}");
    }

    @Тогда("^проверяю бронирование с неверным телом$")
    public void findById() {
        int status = response.getStatusCode();
        if (status == 200)
            log.info("Запрос верен");
        else log.info("Неверный запрос");

    }
}
