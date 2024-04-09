package ee.cc.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.equalTo;

public class InventoryPage extends ParentPage {

    private double total;
    private final Logger logger = LogManager.getLogger();

    private static final String directLinkText = "https://www.saucedemo.com/inventory.html";
    private final String itemNameLocator = "div.inventory_item_name";
    private final String itemPriceLocator = "div.inventory_item_price";

    private final ElementsCollection itemsCollection = $$("div.inventory_item");
    private final SelenideElement sortingSelect = $("select.product_sort_container");

    public enum SortingOrder {
        az, za, lohi, hilo
    }

    public InventoryPage() {
        verifyTitle(logger, "Products");
    }

    public static void openInventoryPageByDirectUrl() {
        LogManager.getLogger().info("Trying to open 'InventoryPage' by direct link");
        open(directLinkText);
    }


    public InventoryPage addItemToCart(int itemIndex) {
        return addItemToCart(itemIndex, false);
    }

    public InventoryPage addItemToCart(int itemIndex, boolean verify) {
        logger.info("Adding item with index " + itemIndex);
        SelenideElement item = itemsCollection.get(itemIndex);
        String itemName = item.should(Condition.exist).$(itemNameLocator).getText();
        String price = item.$(itemPriceLocator).getText();
        logger.info("Item: '" + itemName + "' cost = " + price);
        total += Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        item.$("button").click();
        if (verify) {
            logger.info("Check if item '" + itemIndex + "' has 'Remove' button");
            logger.info(item.$("button[data-test^='remove']").getText());
        }
        return this;
    }

    public int getNumberOfItems() {
        return itemsCollection.size();
    }

    public int getNumberOfAddedItems() {
        return $$("div.inventory_item button[data-test^='remove']").size();
    }

    public InventoryPage verifyNumberOfAddedItems(int numberOfItems) {
        CCMatcher.assertThat(logger, "Verify the number of added to the cart items",
                $$("div.inventory_item button[data-test^='remove']").size(),
                equalTo(numberOfItems));
        return this;
    }
    public InventoryPage verifyNumberOfAvailableItems(int numberOfItems) {
        CCMatcher.assertThat(logger, "Verify the number of available items",
                $$("div.inventory_item button[data-test^='add-to-cart']").size(),
                equalTo(numberOfItems));
        return this;
    }

    public ItemDescriptionPage gotoItemDescription(int itemIndex) {
        itemsCollection.get(itemIndex).$("a").click();
        return new ItemDescriptionPage();
    }

    public double getTotal() {
        return total;
    }

    public InventoryPage selectSortingByValue(SortingOrder sortingOrder) {
        sortingSelect.selectOptionByValue(sortingOrder.toString());
        return this;
    }

    public List<String> getListOfItemsNames() {
        List<String> result = new ArrayList<>();
        for (SelenideElement selenideElement : itemsCollection) {
            result.add(selenideElement.$(itemNameLocator).getText());
        }
        return result;
    }

    public List<Double> getListOfItemsPrices() {
        List<Double> result = new ArrayList<>();
        String itemPriceString;
        for (SelenideElement selenideElement : itemsCollection) {
            itemPriceString = selenideElement.$(itemPriceLocator).getText();
            result.add(Double.parseDouble(itemPriceString.replaceAll("[^\\d.]", "")));
        }
        return result;
    }
}
