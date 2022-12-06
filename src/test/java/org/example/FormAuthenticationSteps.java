package org.example;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class FormAuthenticationSteps {
    @Before
    public void pageOpen() {
        webDriver = new ChromeDriver();
    }

    Logger log = Logger.getLogger(Logger.class.getName());
    WebDriver webDriver;
    int TIMEOUT = 10;

    @Дано("^захожу на \"([^\"]*)\"$")
    public void openPage(String URI) {
        webDriver.get(URI);
        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(xpath = "//input[@name='username']")
    public WebElement usernameInput;

    @FindBy(xpath = "//input[@name='password']")
    public WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement buttonLogin;
    String username;
    String password;

    @Когда("^ввожу в соответствующие поля \"([^\"]*)\" и \"([^\"]*)\"$")
    public void enterUsernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
    }

    @Тогда("^нажимаю на кнопку логин$")
    public void clickLoginButton() {
        buttonLogin.click();
    }

    @Тогда("^если пароль верен вижу сообщение \"([^\"]*)\"$")
    public void correctFormAuthentication(String message) {
        var webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT), Duration.ofSeconds(5));
        webDriverWait.withMessage(message);
        assert message.equals("You logged into a secure area!");
        log.info("ВЫ вошли");
    }


    @Тогда("^если пароль не вререн вижу сообщение \"([^\"]*)\"$")
    public void inCorrectFormAuthentication(String message) {
        var webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT), Duration.ofSeconds(5));

        webDriverWait.withMessage(message);
        assert message.equals("Your username is invalid!");
        log.info("Неверный логин/пароль");
    }

    @After
    public void pageClose() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
