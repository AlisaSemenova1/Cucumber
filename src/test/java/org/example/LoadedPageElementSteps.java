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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class LoadedPageElementSteps {
    @Before
    public void pageOpen() {
        webDriver = new ChromeDriver();
    }

    Logger log = Logger.getLogger(Logger.class.getName());
    WebDriver webDriver;
    int TIMEOUT = 10;

    @Дано("^открыть \"([^\"]*)\"$")
    public void openPage(String URI) {
        webDriver.get(URI);
        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(xpath = "//a[contains( text(),'Example 2')]")
    public WebElement elementRenderedAfterTheFact;

    @FindBy(xpath = "//button")
    public WebElement buttonStart;

    @FindBy(xpath = "//*[@id='finish']/h4")
    public WebElement textAfterLoader;

    @Когда("^кликаем по примеру номер два$")
    public void clickDynamicallyLoadedPageElement() {
        elementRenderedAfterTheFact.click();
    }

    @Тогда("^отоброжается кнопка старт, кликаем по ней$")
    public void clickToButtonStart() {
        buttonStart.click();
    }

    @Тогда("^ждем текста Hello World!$")
    public void textAfterLoader() {
        var webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT), Duration.ofSeconds(5));
        webDriverWait.until(ExpectedConditions.visibilityOf(textAfterLoader));
        assert textAfterLoader.getText().equals("Hello World!");
    }

    @After
    public void pageClose() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

