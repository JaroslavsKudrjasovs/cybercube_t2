package ee.cc.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private final Logger logger;

    private final WebElement loginInputField = $("input#user-name");
    private final WebElement passwordInputField = $("input#password");
    private final WebElement loginButton = $("input#login-button");
    private final SelenideElement loginContainer = $("div.login_container");

    public LoginPage() {
        logger = LogManager.getLogger();
        if (System.getProperty("selenide.browser") == null)
            setupChrome();
        open("https://www.saucedemo.com/");
        loginInputField.sendKeys("standard_user");
        passwordInputField.sendKeys("secret_sauce");
        loginButton.click();
        logger.info("Login attempted");
    }

    // Workaround for chrome. By default, chrome starts with profile password manager that ruins test execution.
    private void setupChrome() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--guest");
        WebDriver driver = WebDriverManager.chromedriver()
                .capabilities(options)
                .create();
        WebDriverRunner.setWebDriver(driver);
    }


    public void verifyLoginPage(boolean exists) {
        logger.info("Check if we are" + (exists ? " " : " not ") + "on login page... ");
        loginContainer.should(exists ? Condition.exist : Condition.not(Condition.exist));
    }
}
