package ee.cc;

import ee.cc.pages.*;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class SauceDemoTest {
    private final Logger logger = LogManager.getLogger();

    @Test
    void loginTest() {

        new LoginPage();
        new InventoryPage();
    }

    @Test(dependsOnMethods = "loginTest")
    void addItemToCartAndVerifyTotal() {
        InventoryPage inventoryPage = new InventoryPage();
        double expectedTotal = inventoryPage
                .addItemToCart(0).verifyShoppingCartBadge(1)
                .addItemToCart(1).verifyShoppingCartBadge(2)
                .getTotal();
        ShoppingCartPage shoppingCartPage = inventoryPage.clickShoppingCartLink();
        CheckoutYourInformationPage checkoutYourInformationPage = shoppingCartPage.clickCheckoutButton();
        CheckoutOverviewPage checkoutOverviewPage =
                checkoutYourInformationPage.fillYourData("Tester", "Auto", "12345").clickContinueButton();

        CCMatcher.assertThat(logger, "Check the total", checkoutOverviewPage.getSubtotal(), equalTo(expectedTotal));
        checkoutOverviewPage.clickFinishButton();
    }
}
