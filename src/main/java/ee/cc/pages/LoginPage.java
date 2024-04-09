package ee.cc.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@SuppressWarnings("unused")
public class LoginPage extends ParentPage {
    private final Logger logger = LogManager.getLogger();

    private final SelenideElement loginContainer = $("div.login_container");

    public enum User {
        STANDARD("standard_user", "secret_sauce"),
        LOCKED("locked_out_user", "secret_sauce"),
        PROBLEM("problem_user", "secret_sauce"),
        PERFORMANCE("performance_glitch_user", "secret_sauce"),
        ERROR("error_user", "secret_sauce"),
        VISUAL("visual_user", "secret_sauce");

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        private final String username;
        private final String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public LoginPage(User user) {
        this(user.getUsername(), user.getPassword());
    }

    public LoginPage(String userName, String password) {
        open("https://www.saucedemo.com/");
        if (userName != null) {
            WebElement loginInputField = $("input#user-name");
            loginInputField.clear();
            loginInputField.sendKeys(userName);
        }
        if (password != null) {
            WebElement passwordInputField = $("input#password");
            passwordInputField.clear();
            passwordInputField.sendKeys(password);
        }
        logger.info("Attempting to login as <" + userName + ">");
        WebElement loginButton = $("input#login-button");
        loginButton.click();
    }

    public void verifyLoginPage(boolean exists) {
        logger.info("Check if we are" + (exists ? " " : " not ") + "on login page... ");
        loginContainer.should(exists ? Condition.exist : Condition.not(Condition.exist));
    }

    public String getErrorMessage() {
        return $("h3[data-test='error']").getText();
    }
}
