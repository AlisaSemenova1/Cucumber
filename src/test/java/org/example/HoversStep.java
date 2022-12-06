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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class HoversStep {

    @Before
    public void pageOpen() {
        webDriver = new ChromeDriver();
    }

    Logger log = Logger.getLogger(Logger.class.getName());

    WebDriver webDriver;
    int TIMEOUT = 10;

    @Дано("^открываю \"([^\"]*)\"$")
    public void openPage(String URI) {
        webDriver.get(URI);
        webDriver.manage().window().maximize();
        PageFactory.initElements(webDriver, this);
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

    @After
    public void pageClose() {
        if (webDriver != null) {
            log.info("pageClose");

            webDriver.quit();
        } else {
            log.info("pageClose failed");

        }
    }
}


