package ee.cc.pages;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutOverviewPage extends ParentPage {
    public CheckoutOverviewPage() {
        verifyTitle("Checkout: Overview");
    }

    public double getSubtotal() {
        String subTotal = $("div.summary_subtotal_label").getText();
        return Double.parseDouble(subTotal.replaceAll("[^\\d.]", ""));
    }

    public CheckoutCompletePage clickFinishButton() {
        $("button#finish").click();
        return new CheckoutCompletePage();
    }
}
