package ee.cc;

import ee.cc.pages.*;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.refresh;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SauceDemoTest extends BaseTest {
    private final Logger logger = LogManager.getLogger();

    @DataProvider(name = "userNamesAndPasswords")
    Object[][] userNamesAndPasswords() {
        String userNameRequired = "Epic sadface: Username is required";
        String passwordRequired = "Epic sadface: Password is required";
        String wrongUsernameOrPassword = "Epic sadface: Username and password do not match any user in this service";
        String lockedUserError = "Epic sadface: Sorry, this user has been locked out.";
        return new Object[][]{
                {null, null, userNameRequired}
                , {null, "qwerty", userNameRequired}
                , {"", "", userNameRequired}
                , {"", "qwerty", userNameRequired}
                , {"test", null, passwordRequired}
                , {"test", "", passwordRequired}
                , {"test", "test", wrongUsernameOrPassword}
                , {LoginPage.User.LOCKED.getUsername(), LoginPage.User.LOCKED.getPassword(), lockedUserError}
        };
    }


    @Test(dataProvider = "userNamesAndPasswords", dependsOnMethods = "logoutTest")
    void loginNegativeTest(String userName, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(userName, password);
        CCMatcher.assertThat(logger, "Check error message when trying to log in as <" + userName + ">",
                loginPage.getErrorMessage(), containsString(expectedError));
        loginPage.verifyLoginPage(true);
        InventoryPage.openInventoryPageByDirectUrl();
        loginPage.verifyLoginPage(true);
    }

    @DataProvider(name = "users")
    Object[][] usersDataProvider() {
        return new Object[][]{
                {LoginPage.User.STANDARD}, {LoginPage.User.ERROR}, {LoginPage.User.VISUAL}
        };
    }

    @Test(dataProvider = "users")
    void addItemsToCartAndVerifyTotal(LoginPage.User user) {
        new LoginPage(user);
        InventoryPage inventoryPage = new InventoryPage();
        if (inventoryPage.getShoppingCartText().equals(""))
            logger.info("'Shopping cart' is empty");
        else {
            logger.warn("'Shopping cart' is not empty. Let's try to clear it.");
            inventoryPage.clickBurgerMenuIcon().clickMenuItemByText(ParentPage.BurgerMenuItem.RESET_APP_STATE);
            logger.info("Check if '" + ParentPage.BurgerMenuItem.RESET_APP_STATE + "' has worked correctly");
            inventoryPage.verifyShoppingCartBadge(0);
            inventoryPage.verifyNumberOfAddedItems(0);
        }

        for (int i = 0; i < inventoryPage.getNumberOfItems(); i++) {
            inventoryPage.addItemToCart(i, true);
            inventoryPage.verifyShoppingCartBadge(i + 1);
        }
        double expectedTotal = inventoryPage.getTotal();
        ShoppingCartPage shoppingCartPage = inventoryPage.clickShoppingCartLink();
        CheckoutYourInformationPage checkoutYourInformationPage = shoppingCartPage.clickCheckoutButton();
        CheckoutOverviewPage checkoutOverviewPage =
                checkoutYourInformationPage.fillYourData("Tester", "Auto", "12345").clickContinueButton();

        CCMatcher.assertThat(logger, "Check the total", checkoutOverviewPage.getSubtotal(), equalTo(expectedTotal));
        CheckoutCompletePage checkoutCompletePage = checkoutOverviewPage.clickFinishButton();
        checkoutCompletePage.clickBackToProductsButton();
        //todo: check if we are on products page
    }

    @Test
    void addItemsFromDetailsToCartAndVerifyTotal() {
        new LoginPage(LoginPage.User.STANDARD);
        double expectedTotal = 0.0;
        InventoryPage inventoryPage = new InventoryPage();
        for (int i = 0; i < 3; i++) {
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

    @Test
    void loginTest() {
        LoginPage loginPage = new LoginPage(LoginPage.User.STANDARD);
        new InventoryPage();
        loginPage.verifyLoginPage(false);
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
    void inventorySortingTest() {
        new LoginPage(LoginPage.User.STANDARD);
        InventoryPage inventoryPage = new InventoryPage();
        inventoryPage.selectSortingByValue(InventoryPage.SortingOrder.lohi);
        List<Double> itemsPricesListSeenOnPage = inventoryPage.getListOfItemsPrices();
        List<Double> itemsPricesListSorted = new ArrayList<>(itemsPricesListSeenOnPage);
        Collections.sort(itemsPricesListSorted);
        CCMatcher.assertThat(logger, "Check lohi sorting", itemsPricesListSeenOnPage, equalTo(itemsPricesListSorted));

        inventoryPage.selectSortingByValue(InventoryPage.SortingOrder.hilo);
        itemsPricesListSeenOnPage = inventoryPage.getListOfItemsPrices();
        Collections.reverse(itemsPricesListSorted);
        CCMatcher.assertThat(logger, "Check hilo sorting", itemsPricesListSeenOnPage, equalTo(itemsPricesListSorted));

        inventoryPage.selectSortingByValue(InventoryPage.SortingOrder.az);
        List<String> itemsNamesListSeenOnPage = inventoryPage.getListOfItemsNames();
        List<String> itemsNamesListSorted = new ArrayList<>(itemsNamesListSeenOnPage);
        Collections.sort(itemsNamesListSorted);
        CCMatcher.assertThat(logger, "Check az sorting", itemsNamesListSeenOnPage, equalTo(itemsNamesListSorted));

        inventoryPage.selectSortingByValue(InventoryPage.SortingOrder.za);
        itemsNamesListSeenOnPage = inventoryPage.getListOfItemsNames();
        Collections.reverse(itemsNamesListSorted);
        CCMatcher.assertThat(logger, "Check za sorting", itemsNamesListSeenOnPage, equalTo(itemsNamesListSorted));
    }

    @Test
    void fillCartByUser1LoginByUser2Test() {
        new LoginPage(LoginPage.User.STANDARD);
        InventoryPage inventoryPage = new InventoryPage();
        if (inventoryPage.getShoppingCartText().equals("") && inventoryPage.getNumberOfAddedItems() == 0)
            logger.info("'Shopping cart' is empty");
        else {
            logger.warn("'Shopping cart' is not empty. Let's try to clear it.");
            inventoryPage.clickBurgerMenuIcon().clickMenuItemByText(ParentPage.BurgerMenuItem.RESET_APP_STATE);
            logger.info("Check if '" + ParentPage.BurgerMenuItem.RESET_APP_STATE + "' has worked correctly");
            inventoryPage.verifyShoppingCartBadge(0);
            if (inventoryPage.getNumberOfAddedItems() > 0)
                logger.warn("Number of items listed as they are in the cart is > 0!");
            refresh(); // temporary let's proceed
            CCMatcher.assertThat(logger, "Check the number of items with 'Remove' button", inventoryPage.getNumberOfAddedItems(), equalTo(0));
        }

        new LoginPage(LoginPage.User.VISUAL);
        for (int i = 0; i < 3; i++) {
            inventoryPage.addItemToCart(i, true);
        }
        inventoryPage.clickBurgerMenuIcon().clickMenuItemByText(ParentPage.BurgerMenuItem.LOGOUT);
        new LoginPage(LoginPage.User.STANDARD);
        inventoryPage.verifyNumberOfAddedItems(0);
        inventoryPage.verifyShoppingCartBadge(0);
    }
}
