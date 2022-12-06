package org.example;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class TokenSteps {
    static String baseUri = "https://restful-booker.herokuapp.com/auth";
    Logger log = Logger.getLogger(TokenSteps.class.getName());

    String token;
    String username;
    String password;

    Response response;

    String jsonString;

    @Дано("^я ввожу \"([^\"]*)\" и \"([^\"]*)\"$")
    public void enter(String username, String password) {
        this.username = username;
        this.password = password;
        log.info("login username = " + username + ", password = " + password);
    }

    @Когда("^я отправляю запрос на получение токена$")
    public void getToken() {
        RestAssured.baseURI = baseUri;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body("{\n" +
                "    \"username\" : \"" + username + "\",\n" +
                "    \"password\" : \"" + password + "\"\n" +
                "}").post(baseUri);
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


}


