package ee.cc.pages;

import org.apache.logging.log4j.LogManager;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutCompletePage extends ParentPage {
    public CheckoutCompletePage() {
        verifyTitle(LogManager.getLogger(),"Checkout: Complete!");
    }
    public void clickBackToProductsButton(){
        $("button#back-to-products").click();
    }
}
