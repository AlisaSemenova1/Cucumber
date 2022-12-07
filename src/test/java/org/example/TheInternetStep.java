package org.example;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;


public class TheInternetStep {
    @Before
    public void pageOpen() {
        webDriver = new ChromeDriver();
    }
    Logger log = Logger.getLogger(Logger.class.getName());

    WebDriver webDriver;
    int TIMEOUT = 10;

    @Дано("^захожу на страницу \"([^\"]*)\"$")
    public void openPage(String URI) {
        webDriver.get(URI);
        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(xpath = "//input[@type='checkbox']")
    public WebElement checkboxInput;

    @Когда("^нажимаю чекбокс$")
    public void clickCheckbox() {
        checkboxInput.click();
    }

    @Тогда("^проверяю, что чекбокс нажат$")
    public void checkingCheckbox() {
        webDriver.findElement(By.xpath("//input[@type='checkbox']"));
        if (checkboxInput.isSelected())
            log.info(("checkbox нажат"));
        else {
            log.info(("checkbox не нажат"));
        }
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

    @FindBy(xpath = "//img[@alt='User Avatar']")
        public WebElement userAvatar;

        @FindBy(xpath = "//a[contains(text(),'View profile')]")
        public WebElement textUnderUserAvatar;

        @Когда("^навожу курсор на пользователя$")
        public void hoverToUser() {
            Actions actions = new Actions(webDriver);
            actions.moveToElement(userAvatar).perform();
        }

        @Тогда("^отоброжается текст$")
        public void userVisibleText() {
            var webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT), Duration.ofSeconds(5));

            webDriverWait.until(ExpectedConditions.visibilityOf(textUnderUserAvatar));

            if (textUnderUserAvatar.isDisplayed()) {
                log.info("Текст отображается");
            } else {
                log.info("Текст не отображается");
            }
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




