package org.example;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.util.logging.Logger;

public class UpdateBookingSteps {
    Logger log = Logger.getLogger(Logger.class.getName());
    static String baseUri = "https://restful-booker.herokuapp.com/booking/";
    static String authUri = "https://restful-booker.herokuapp.com/auth";
    int bookingId;
    String token;
    String jsonString;

    @Дано("^авторизуюсь$")
    public void logIn() {
        RestAssured.baseURI = authUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        var response = request.body("{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}").post(authUri);
        var jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");
        int status = response.getStatusCode();
        assert status == 200;
    }

    public RequestSpecification createRequest(String body) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.headers("Cookie", "token=" + token);
        return request.body(body);
    }

    public static String findById(String id, int status) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        return request.contentType(ContentType.JSON)
                .get(id)
                .then()
                .statusCode(status)
                .extract()
                .response()
                .asString();
    }

    public static String findById(int id) {
        return findById(String.valueOf(id), 200);
    }

    @Дано("^создаю бронирование$")
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

        var response = createRequest(bodyAddBooking).post();
        var jsonString = response.asString();
        bookingId = JsonPath.from(jsonString).get("bookingid");
        log.info("Создано бронирование с id: " + bookingId + bodyAddBooking);
    }

    @Когда("^обновляю бронирование$")
    public void updateBooking() {
        String bodyUpdateBooking = "{\n" +
                "    \"firstname\" : \"Иван1\",\n" +
                "    \"lastname\" : \"Иванов1\",\n" +
                "    \"totalprice\" : 333,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-11-25\",\n" +
                "        \"checkout\" : \"2022-11-26\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast1\"\n" +
                "}";
        var response = createRequest(bodyUpdateBooking).put(String.valueOf(bookingId));
        var statusCode = response.getStatusCode();
        assert statusCode == 200 : "Неверный statusCode = " + statusCode;
        var jsonString = response.asString();
        log.info("Обновлено бронирование с id: " + bookingId + jsonString);
    }

    @Тогда("^проверяю, что бронирование обновлено$")
    public void findID() {
        jsonString = findById(String.valueOf(bookingId), 200);
        int totalprice = JsonPath.from(jsonString).get("totalprice");
        assert totalprice == 333 : "Неверный totalprice = " + totalprice;
        log.info("Проверка бронирования с id: " + bookingId);
    }

    @И("^проверяю бронирование с несуществующим id$")
    public void findNullId() {
        findById(String.valueOf(65786548), 404);
        log.info("Бронирования с таким id нет");
    }
}


