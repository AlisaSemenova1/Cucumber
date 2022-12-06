package org.example;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;


public class CheckboxStep {
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

    @After
    public void pageClose() {
        if (webDriver != null) {
            webDriver.quit();
        }

    }
}
