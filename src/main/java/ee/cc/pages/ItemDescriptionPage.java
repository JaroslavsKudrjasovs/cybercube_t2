package ee.cc.pages;

import com.codeborne.selenide.SelenideElement;
import org.apache.logging.log4j.LogManager;

import static com.codeborne.selenide.Selenide.$;

public class ItemDescriptionPage extends ParentPage {
    public ItemDescriptionPage() {
        // no title here
        //verifyTitle(LogManager.getLogger(), "");
    }

    public double addItemToCart() {
        SelenideElement itemContainer = $("div.inventory_item_container");
        String price = itemContainer.$("div.inventory_details_price").getText();
        double itemPrice = Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        itemContainer.$("button#add-to-cart").click();
        return itemPrice;
    }

    public void backToProducts(){
        $("button#back-to-products").click();
    }
}
