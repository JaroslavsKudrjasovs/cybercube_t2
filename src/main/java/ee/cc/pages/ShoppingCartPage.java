package ee.cc.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ShoppingCartPage extends ParentPage {
    private final Logger logger = LogManager.getLogger();

    public ShoppingCartPage() {
        verifyTitle("Your Cart");
    }

    public CheckoutYourInformationPage clickCheckoutButton() {
        $("button#checkout").click();
        return new CheckoutYourInformationPage();
    }
}
