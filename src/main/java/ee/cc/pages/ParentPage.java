package ee.cc.pages;

import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static org.hamcrest.Matchers.*;

public class ParentPage {
    private final Logger logger = LogManager.getLogger();
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

    private final WebElement shoppingCartLink = $("a.shopping_cart_link");

    public ShoppingCartPage clickShoppingCartLink() {
        shoppingCartLink.click();
        return new ShoppingCartPage();
    }

    public String getShoppingCartText() {
        return shoppingCartLink.getText();
    }

    public ParentPage verifyShoppingCartBadge(int expectedNumberOfItems) {
        if (expectedNumberOfItems == 0)
            CCMatcher.assertThat(logger, "Check if 'Shopping cart' is empty", getShoppingCartText(), is(emptyString()));
        else
            CCMatcher.assertThat(logger, "Check the 'Shopping cart badge'", Integer.valueOf(getShoppingCartText()), equalTo(expectedNumberOfItems));
        return this;
    }
}
