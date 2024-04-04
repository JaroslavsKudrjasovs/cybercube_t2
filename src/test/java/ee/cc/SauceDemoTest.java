package ee.cc;

import ee.cc.pages.InventoryPage;
import ee.cc.pages.LoginPage;
import org.testng.annotations.Test;

public class SauceDemoTest {
    @Test
    void loginTest() {

        new LoginPage();
        new InventoryPage().verifyProductsLabel();
    }
}
