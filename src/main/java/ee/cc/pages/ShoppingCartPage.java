package ee.cc.pages;

import org.apache.logging.log4j.LogManager;

import static com.codeborne.selenide.Selenide.$;

public class ShoppingCartPage extends ParentPage {

    public ShoppingCartPage() {
        verifyTitle(LogManager.getLogger(), "Your Cart");
    }

    public CheckoutYourInformationPage clickCheckoutButton() {
        $("button#checkout").click();
        return new CheckoutYourInformationPage();
    }
}
