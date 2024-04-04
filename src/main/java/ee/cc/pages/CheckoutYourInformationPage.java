package ee.cc.pages;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutYourInformationPage extends ParentPage {

    public CheckoutYourInformationPage() {
        verifyTitle("Checkout: Your Information");
    }

    public CheckoutOverviewPage clickContinueButton() {
        $("input#continue").click();
        return new CheckoutOverviewPage();
    }

    public CheckoutYourInformationPage fillYourData(String fname, String lName, String zip) {
        $("input#first-name").sendKeys(fname);
        $("input#last-name").sendKeys(lName);
        $("input#postal-code").sendKeys(zip);
        return this;
    }
}
