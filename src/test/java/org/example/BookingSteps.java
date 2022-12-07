package org.example;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.logging.Logger;

public class BookingSteps {
    static String baseUriToken = "https://restful-booker.herokuapp.com/auth";
    Logger log = Logger.getLogger(BookingSteps.class.getName());
    String token;
    String username;
    String password;
    String firstname;
    String lastname;
    Response response;
    String jsonString;
    int bookingId;
    static String baseUri = "https://restful-booker.herokuapp.com/booking/";

    @Дано("^я ввожу \"([^\"]*)\" и \"([^\"]*)\"$")
    public void enter(String username, String password) {
        this.username = username;
        this.password = password;
        log.info("login username = " + username + ", password = " + password);
    }

    @Когда("^я отправляю запрос на получение токена$")
    public void getToken() {
            RestAssured.baseURI = baseUriToken;
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            response = request.body("{\n" +
                    "    \"username\" : \"" + username + "\",\n" +
                    "    \"password\" : \"" + password + "\"\n" +
                    "}").post(baseUriToken);
            jsonString = response.asString();
            token = JsonPath.from(jsonString).get("token");
            int status = response.getStatusCode();
            if (status == 200)
                log.info("token " + token);
    }

    @Тогда("^я получаю токен$")
    public void existToken() {
        if (token != null)
            log.info("token получен");
        else log.info("Неверный логин/пароль");
    }

    @Дано("^я ввожу \"([^\"]*)\"  и \"([^\"]*)\"$")
    public void addBooking(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Когда("^я создаю бронирование и получаю id$")
    public void createBooking() {
        createBooking("{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "    \"lastname\" : \"" + lastname + "\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-11-20\",\n" +
                "        \"checkout\" : \"2022-11-21\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}", 200);
        jsonString = response.asString();
        bookingId = JsonPath.from(jsonString).get("bookingid");
        log.info("id :" + bookingId);
    }

    @Тогда("^я проверяю бронирование$")
    public void checkBooking() {
        bookingId = JsonPath.from(jsonString).get("bookingid");
        log.info(" Создано бронирование " + firstname + " " + lastname + " c id: " + bookingId);
    }

    @И("^я создаю бронирование с неверным телом$")
    public void createFailBooking() {
        log.info("Создаю бронирование с неверным телом");
        createBooking("{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "}");
    }

    @Тогда("^проверяю бронирование с неверным телом$")
    public void findById() {
        int status = response.getStatusCode();
        assert status != 200;
        if (status == 200)
            log.info("Запрос верен");
        else log.info("Неверный запрос");

    }

    @Дано("^авторизуюсь$")
    public void logIn() {
        RestAssured.baseURI = baseUriToken;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        var response = request.body("{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}").post(baseUriToken);
        var jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");
        int status = response.getStatusCode();
        assert status == 200;
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

    @Когда("^обновляю бронирование$")
    public void updateBooking() {
        String bodyUpdateBooking = "{\n" +
                "    \"firstname\" : \"Иван\",\n" +
                "    \"lastname\" : \"Иванов\",\n" +
                "    \"totalprice\" : 333,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2022-11-25\",\n" +
                "        \"checkout\" : \"2022-11-26\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast1\"\n" +
                "}";
        response = createBookingRequest(bodyUpdateBooking).put(String.valueOf(bookingId));
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

    @Когда("^удаляю бронирование$")
    public void deleteBooking() {
        deleteBooking(bookingId);
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

    private static String findById(int id, int status) {
      return   RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .get(String.valueOf(id))
                .then()
                .statusCode(status)
                .extract()
                .response()
                .asString();
    }

    private void deleteBooking(int id) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.headers("Cookie", "token=" + token);
        response = request.delete(String.valueOf(id));
    }

    private RequestSpecification createBookingRequest(String body) {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.headers("Cookie", "token=" + token);
        return request.body(body);
    }

    private void createBooking(String body) {
        response = createBookingRequest(body).post();
    }
    private void createBooking(String body, int status) {
        createBooking(body);
        var statusCode = response.getStatusCode();
        assert statusCode == status : "Неверный statusCode = " + statusCode;
    }
}









