package org.example;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class KeyPressesStep {
    @Before
    public void pageOpen() {
        webDriver = new ChromeDriver();
    }

    Logger log = Logger.getLogger(Logger.class.getName());
    WebDriver webDriver;

    int TIMEOUT = 20;

    @Дано("^открываем \"([^\"]*)\"$")
    public void openPage(String URI) {
        webDriver.get(URI);
        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(xpath = "//*[@id='target']")
    public WebElement target;

    @FindBy(xpath = "//*[@id='result']")
    public WebElement result;

    @Когда("^в поле для ввода нажимаем клавишу \"([^\"]*)\"$")
    public void enterCommand(Keys key) {
        target.sendKeys(key);
    }

    @Когда("^в поле для ввода вводим текст \"([^\"]*)\"$")
    public void enterCommand(String key) {
        target.sendKeys(key);
    }

    @Тогда("^текст не появился$")
    public void invisibleText() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        assert !result.isDisplayed();
    }

    @Тогда("^в поле ввода появилось \"([^\"]*)\"$")
    public void waiting(String text) {
        String resultText = result.getText();
        var webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT), Duration.ofSeconds(5));
        webDriverWait.until(ExpectedConditions.visibilityOf(result));
        webDriverWait.withMessage(text);
        log.info(resultText + " != " + text);
    }

    @After
    public void pageClose() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

