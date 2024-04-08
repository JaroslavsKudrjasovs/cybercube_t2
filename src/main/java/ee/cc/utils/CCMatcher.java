package ee.cc.utils;

import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testng.Reporter;

public class CCMatcher {
    public static <T> void assertThat(Logger logger, String reason, T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(reason, actual, matcher);
        String message = "Successfully matched actual '" + actual + "' with expected " + matcher;
        if (reason != null)
            message = reason + ". " + message;
        logger.info(message);
        Reporter.log(logger.getName() + " - " + message);
    }
}
