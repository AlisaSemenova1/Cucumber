package org.example;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import dev.failsafe.internal.util.Assert;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.logging.Logger;

public class DeleteBookingSteps {
    Logger log = Logger.getLogger(Logger.class.getName());
    static String baseUri = "https://restful-booker.herokuapp.com/booking/";
    static String authUri = "https://restful-booker.herokuapp.com/auth";
    Response response;
    String token;
    int bookingId;

    @Дано("^я авторизуюсь$")
    public void logIn() {
        RestAssured.baseURI = authUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body("{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}").post(authUri);
        var jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");
        int status = response.getStatusCode();
        assert status == 200;
    }

    public void booking(String body) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body(body).post(baseUri);
    }
    public static String findById(int id, int status) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .get(String.valueOf(id))
                .then()
                .statusCode(status)
                .extract()
                .response()
                .toString();
    }

    @Дано("^создаем бронирование$")
    public void createBooking() {
        String bodyAddBooking = "{\n" +
                "    \"firstname\" : \"Иван\",\n" +
                "    \"lastname\" : \"Иванов\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-11-20\",\n" +
                "        \"checkout\" : \"2022-11-21\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        booking(bodyAddBooking);
        var jsonString = response.asString();
        assert response.getStatusCode() == 200;
        bookingId = JsonPath.from(jsonString).get("bookingid");
    }

    public void deleteBooking(int id) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.headers("Cookie", "token=" + token);
        response = request.delete(String.valueOf(id));
    }

    @Когда("^удаляю бронирование$")
    public void deleteBooking() {
        deleteBooking(bookingId);
        assert response.getStatusCode() == 201;
        log.info("Бронирование с id: " + bookingId + "удалено.");
    }

    @Тогда("^проверяю, что бронирование удалено$")
    public void checkDeleteBooking() {
        findById(bookingId, 404);
        log.info("Бронирование с id: " + bookingId + "удалено");
    }

    @И("^удаляю бронирование с несуществующим id$")
    public void deleteFailedId() {
        deleteBooking(123123454);
        var statusCode = response.getStatusCode();
        assert statusCode == 405 : "Неверный statusCode = " + statusCode;
        log.info("Удаление не произошло.");
    }


}
