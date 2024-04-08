package ee.cc.pages;

import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static org.hamcrest.Matchers.equalTo;

public class ParentPage {
    protected String title;

    public enum BurgerMenuItem {
        ALL_ITEMS("All Items"),
        ABOUT("About"),
        LOGOUT("Logout"),
        RESET_APP_STATE("Reset App State");

        private final String text;

        BurgerMenuItem(String text) {
            this.text = text;
        }

        public String getValue() {
            return text;
        }

        @Override
        public String toString() {
            return String.valueOf(text);
        }
    }

    protected void verifyTitle(Logger logger, String expectedTitle) {
        CCMatcher.assertThat(logger, "Check the title", $("span.title").getText(), equalTo(expectedTitle));
    }

    private final WebElement burgerMenu = $("button#react-burger-menu-btn");

    public ParentPage clickBurgerMenuIcon() {
        burgerMenu.click();
        return this;
    }

    //$(By.xpath("//a[translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '" + menuItem.getValue().toLowerCase() + "']")).click();
    public void clickMenuItemByText(BurgerMenuItem menuItem) {
        $(By.xpath("//a[text()='" + menuItem + "']")).click();
    }
}
