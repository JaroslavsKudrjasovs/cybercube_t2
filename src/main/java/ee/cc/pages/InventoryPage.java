package ee.cc.pages;

import com.codeborne.selenide.WebDriverRunner;
import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static org.hamcrest.Matchers.equalTo;

public class InventoryPage {

    private Logger logger = LogManager.getLogger();
    @FindBy()
    private WebElement productsLabel = $("div.product_label,span.title");

    public InventoryPage() {
    }

    public InventoryPage verifyProductsLabel() {
        CCMatcher.assertThat(logger, "Check the 'Products' label", productsLabel.getText(), equalTo("Products"));

        return this;
    }
}
