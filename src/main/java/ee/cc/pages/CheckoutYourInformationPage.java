package ee.cc.pages;

import org.apache.logging.log4j.LogManager;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutYourInformationPage extends ParentPage {

    public CheckoutYourInformationPage() {
        verifyTitle(LogManager.getLogger(), "Checkout: Your Information");
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
