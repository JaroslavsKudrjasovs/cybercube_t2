package ee.cc;

import ee.cc.pages.*;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class SauceDemoTest {
    private final Logger logger = LogManager.getLogger();

    @Test
    void loginStandardUserTest() {
        new LoginPage(LoginPage.User.STANDARD);
        new InventoryPage();
    }

    @DataProvider(name = "users")
    Object[][] usersDataProvider() {
        return new Object[][]{
                {LoginPage.User.STANDARD},{LoginPage.User.VISUAL}
        };
    }

    @Test(dependsOnMethods = "loginStandardUserTest", dataProvider = "users")
    void addItemsToCartAndVerifyTotal(LoginPage.User user) {
        new LoginPage(user);
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
        CheckoutCompletePage checkoutCompletePage = checkoutOverviewPage.clickFinishButton();
        checkoutCompletePage.clickBackToProductsButton();
    }

    @Test(dependsOnMethods = "loginStandardUserTest")
    void addItemsFromDetailsToCartAndVerifyTotal() {
        double expectedTotal = 0.0;
        InventoryPage inventoryPage = new InventoryPage();
        for (int i = 0; i < 2; i++) {
            ItemDescriptionPage itemDescriptionPage = inventoryPage.gotoItemDescription(i);
            expectedTotal += itemDescriptionPage.addItemToCart();
            itemDescriptionPage.backToProducts();
        }
        ShoppingCartPage shoppingCartPage = inventoryPage.clickShoppingCartLink();
        CheckoutYourInformationPage checkoutYourInformationPage = shoppingCartPage.clickCheckoutButton();
        CheckoutOverviewPage checkoutOverviewPage =
                checkoutYourInformationPage.fillYourData("Tester", "Auto", "12345").clickContinueButton();

        CCMatcher.assertThat(logger, "Check the total", checkoutOverviewPage.getSubtotal(), equalTo(expectedTotal));
        CheckoutCompletePage checkoutCompletePage = checkoutOverviewPage.clickFinishButton();
        checkoutCompletePage.clickBackToProductsButton();
    }

    @Test(priority = 100)
    void logoutTest() {
        LoginPage loginPage = new LoginPage(LoginPage.User.STANDARD);
        InventoryPage inventoryPage = new InventoryPage();
        loginPage.verifyLoginPage(false);
        inventoryPage.clickBurgerMenuIcon().clickMenuItemByText(ParentPage.BurgerMenuItem.LOGOUT);
        loginPage.verifyLoginPage(true);
        InventoryPage.openInventoryPageByDirectUrl();
        loginPage.verifyLoginPage(true);
    }

    @Test
    void loginLockedUserTest() {
        LoginPage loginPage = new LoginPage(LoginPage.User.LOCKED);
        loginPage.verifyLoginPage(true);
        InventoryPage.openInventoryPageByDirectUrl();
        loginPage.verifyLoginPage(true);
    }
}
