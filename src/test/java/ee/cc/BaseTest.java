package ee.cc;

import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {
    public BaseTest() {
        // Workaround for chrome. By default, chrome starts with profile password manager that ruins test execution.
        // To run the tests against other browser add -Dselenide.browser to JVM options, e.g. -Dselenide.browser=firefox
        if (WebDriverManager.chromedriver().getWebDriver() != null) return;
        if (System.getProperty("selenide.browser") == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--guest");
            WebDriver driver = WebDriverManager.chromedriver()
                    .capabilities(options)
                    .create();
            WebDriverRunner.setWebDriver(driver);
            WebDriverManager.chromedriver().setup();
        }
    }
}
