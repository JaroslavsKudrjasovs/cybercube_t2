package ee.cc.pages;

import com.codeborne.selenide.ElementsCollection;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.equalTo;

public class InventoryPage extends ParentPage {

    private double total;
    private final Logger logger = LogManager.getLogger();

    private static final String directLinkText = "https://www.saucedemo.com/inventory.html";

    public InventoryPage() {
        verifyTitle(logger, "Products");
    }
    public static void openInventoryPageByDirectUrl(){
        open(directLinkText);
    }

    public InventoryPage addItemToCart(int itemIndex) {
        ElementsCollection collection = $$(By.xpath("//div[@class='inventory_item']"));
        String price = collection.get(itemIndex).$("div.inventory_item_price").getText();
        total += Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        collection.get(itemIndex).$("button").click();
        return this;
    }

    public ItemDescriptionPage gotoItemDescription(int itemIndex) {
        ElementsCollection collection = $$(By.xpath("//div[@class='inventory_item']"));
        collection.get(itemIndex).$("a").click();
        return new ItemDescriptionPage();
    }

    public double getTotal() {
        return total;
    }

    private final WebElement shoppingCartLink = $("a.shopping_cart_link");

    public ShoppingCartPage clickShoppingCartLink() {
        shoppingCartLink.click();
        return new ShoppingCartPage();
    }

    public InventoryPage verifyShoppingCartBadge(int expectedNumberOfItems) {
        CCMatcher.assertThat(logger, "Check the 'Shopping cart badge'", Integer.valueOf(shoppingCartLink.getText()), equalTo(expectedNumberOfItems));
        return this;
    }
}
