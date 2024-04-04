package ee.cc.pages;

import ee.cc.utils.CCMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static org.hamcrest.Matchers.equalTo;

public class ParentPage {
    private Logger logger = LogManager.getLogger();
    protected String title;

    public ParentPage verifyTitle(String expectedTitle) {
        CCMatcher.assertThat(logger, "Check the title", $("span.title").getText(), equalTo(expectedTitle));
        return this;
    }
}
