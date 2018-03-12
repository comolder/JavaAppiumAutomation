import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName","AndroidTestDevice");
        capabilities.setCapability("platformVersion","8.1");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","/Users/vitalijkotov/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void firstTest()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );
        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );
    }

    @Test
    public void testCancelSearch()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"), // same search input but set by element ID
                "Cannot find 'Search Wikipedia' input.",
                5
        );
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );
        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X still present, cannot cancel search.",
                1
        );
    }

    @Test
    public void testCompareArticleTitle()
    {
        /* same as in firstTest - start */
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );
        /* same as in firstTest - end */

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );

        WebElement title_element = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title.",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );

        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected title!", // error message
                "Java (programming language)", // expected value
                article_title // actual value

        );
    }

    @Test
    public void swipeArticleToTheBottom()
    {
        /* same as in testCompareArticleTitle - start */
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title.",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );
        /* same as in testCompareArticleTitle - end */

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"), // bottom
                "Cannot find the end of the article.",
                //1 // to fail
                30 // to find for sure
        );
    }

    @Test
    public void saveFirstArticleToMyList()
    {
        /* same as in testCompareArticleTitle - start */
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title.",
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );
        /* same as in testCompareArticleTitle - end */

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find option to add article to reading list",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@text='GOT IT']"),
                "Cannot find 'Got It' tip overlay",
                5
        );

        // if we've already found element, there is no need to find it in second time
        WebElement input_element = waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to set name of articles folder",
                5
        );
        String name_of_folder = "Learning programing"; // to check it in future folder
        input_element.sendKeys(name_of_folder);

        // sometimes when an element clearly already exist, we could use simple click instead of waiting
        driver.findElementByXPath("//*[@text='OK']").click();

        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article, cannot find X link.",
                5
        );

        // now we check saved article
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find navigation button to My List",
                5
        );

        // xpath depends on text we set previously
        // we need to click on one level up, so we've added /..
        String xpath_to_new_folder = "//*[@text='" + name_of_folder + "']/..";
        waitForElementAndClick(
                By.xpath(xpath_to_new_folder),
                "Cannot find created folder",
                5
        );

        // this action will delete an article
        swipeOnElementToLeft(
                By.xpath("//*[@text='Java (programming language)']/.."),
                "Cannot find saved article"
        );

        // make sure an article've been deleted
        waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article.",
                10
        );
    }

    /**
     * We need this one and testAmountOfEmptySearch to show how "find elements" method works
     * Also we'll use assertGreaterThen and assertLessThen functions
     * Also we'll refactor this two tests in one with dataProvider
     */
    @Test
    public void testAmountOfNotEmptySearch()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );

        String search_line = "Linkin Park Diskography"; // we need it to reuse now and to make it a dataProvider param in future
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by the request " + search_line,
                15
        );

        int amount_of_search_results = getAmountOfElements(
                By.xpath(search_result_locator)
        );


        /*
        * А вот нам и понадобился hamcrest-либа
        * Знакомься, это аналог assertGreaterThan :)
        * Я считаю, это надо показать в курсе и рассказать об этом.
        * Но смотри сам - ниже есть просто assertTrue.
        * */
        Assert.assertThat(
                "We found too few results!",
                amount_of_search_results,
                //Matchers.greaterThan(50) // to fail
                Matchers.greaterThan(0) // to pass
        );

        /*Assert.assertTrue(
                "We found too few results!",
                amount_of_search_results > 0
        );*/
    }

    /**
     * We need this one and testAmountOfNotEmptySearch to show how "find elements" method works
     * Also we'll use assertGreaterThen and assertLessThen functions
     * Also we'll refactor this two tests in one with dataProvider
     */
    @Test
    public void testAmountOfEmptySearch()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );

        String search_line = "Labudabudayda"; // we need it to reuse now and to make it a dataProvider param in future
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot find search input",
                5
        );

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//*[@text='No results found']";

        // make sure we found nothing
        waitForElementPresent(
                By.xpath(empty_result_label),
                "Cannot find anything by the request " + search_line,
                15
        );
        assertElementNotPresent(
                By.xpath(search_result_locator),
                "We supposed to not found any results by request " + search_line
        );

        // check amount of elements
        int amount_of_search_results = getAmountOfElements(
                By.xpath(search_result_locator)
        );

        /*
         * Это аналог assertLessThan
         * Я считаю, это надо показать в курсе и рассказать об этом.
         * Но смотри сам - ниже есть просто assertTrue.
         * */
        Assert.assertThat(
                "We found too many results!",
                amount_of_search_results,
                //Matchers.lessThan(-1) // to fail
                Matchers.lessThan(1) // to pass
        );

        /*Assert.assertTrue(
                "We found too many results!",
                amount_of_search_results < 1
        );*/
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults()
    {
        // hmmm, похоже эмулятор запоминает последнюю позицию экрана
        // стоит делать вот так в setUp, но мы джобавим уже во время рефакторинга это
        driver.rotate(ScreenOrientation.PORTRAIT);

        // open an article
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input.",
                5
        );
        String search_line = "Java";
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                search_line,
                "Cannot find search input",
                5
        );
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by " + search_line,
                15 // более длинный таймаут, так как это ожидание взаимодействия с сервером
        );

        // we need it to compare title after screen changing
        String title_of_article_before_change_orientation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        // to compare size of screen after screen've been changed
        int screen_width_before_change_orientation = driver.manage().window().getSize().getWidth();
        int screen_height_before_change_orientation = driver.manage().window().getSize().getHeight();

        // CHANGE SCREEN FIRST TIME
        driver.rotate(ScreenOrientation.LANDSCAPE);

        // get screen size
        int screen_width_after_change_orientation = driver.manage().window().getSize().getWidth();
        int screen_height_after_change_orientation = driver.manage().window().getSize().getHeight();

        // make sure screen changed
        Assert.assertThat(
                "Height supposed to become less then it was before",
                screen_height_before_change_orientation,
                Matchers.greaterThan(screen_height_after_change_orientation)
        );
        Assert.assertThat(
                "Width supposed to become bigger then it was before",
                screen_width_before_change_orientation,
                Matchers.lessThan(screen_width_after_change_orientation)
        );


        // check title of article
        String title_of_article_after_change_orientation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );
        Assert.assertEquals(
                "Article title have been changed after screen rotation!",
                title_of_article_before_change_orientation,
                title_of_article_after_change_orientation

        );

        // CHANGE SCREEN SECOND TIME
        driver.rotate(ScreenOrientation.PORTRAIT);

        // get screen size
        int screen_width_after_back_orientation = driver.manage().window().getSize().getWidth();
        int screen_height_after_back_orientation = driver.manage().window().getSize().getHeight();

        // make sure screen back to previous size
        Assert.assertEquals(
                "Height supposed to become previous size",
                screen_height_before_change_orientation,
                screen_height_after_back_orientation
        );
        Assert.assertEquals(
                "Width supposed to become previous size",
                screen_width_before_change_orientation,
                screen_width_after_back_orientation
        );

        // check title of article
        String title_of_article_after_back_orientation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );
        Assert.assertEquals(
                "Article title have been changed after screen rotation!",
                title_of_article_before_change_orientation,
                title_of_article_after_back_orientation
        );
    }

    /**
     * Это тест с плохой активити, он падает
     */
    @Test
    public void testSettingsInBackground()
    {
        String open_menu_button = "org.wikipedia:id/menu_overflow_button";
        String open_settings_link = "org.wikipedia:id/explore_overflow_settings";
        String show_image_checker_xpath = "//*[@text='Show images']/../../*[@resource-id='android:id/widget_frame']/*[@resource-id='org.wikipedia:id/switchWidget']";

        // go to the settings
        waitForElementAndClick(
                By.id(open_menu_button),
                "Cannot wait for settings button",
                10
        );
        waitForElementAndClick(
                By.id(open_settings_link),
                "Cannot find lint to settings page",
                5
        );

        unselectSwitcherIfSelect(
                By.xpath(show_image_checker_xpath),
                "Cannot find switcher for 'Show images' setting"
        );

        sendAppToBackgroundAndBack(2);

        // go to the settings (yeah, once again)
        waitForElementAndClick(
                By.id(open_menu_button),
                "Cannot wait for settings button",
                10
        );
        waitForElementAndClick(
                By.id(open_settings_link),
                "Cannot find lint to settings page",
                5
        );

        // сломается, потому что сосотояние не сохранилось, потому что аппа вернулась из catch
        selectSwitcherIfUnselect(
                By.xpath(show_image_checker_xpath),
                "Cannot find switcher for 'Show images' setting"
        );
    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private void assertElementNotPresent(By by, String error_message)
    {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' supposed to not be present.";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_message)
    {
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    private int getAmountOfElements(By by)
    {
        List elements = driver.findElements(by);
        return elements.size();
    }

    private WebElement selectSwitcherIfUnselect(By by, String error_message)
    {
        WebElement element = waitForElementPresent(by, error_message);
        if (isElementChecked(by)) {
            throw new AssertionError("Element have already been selected.");
        }

        element.click();
        return element;
    }

    private WebElement unselectSwitcherIfSelect(By by, String error_message)
    {
        WebElement element = waitForElementPresent(by, error_message);
        if (!isElementChecked(by)) {
            throw new AssertionError("Element is unselected.");
        }

        element.click();
        return element;
    }

    private boolean isElementChecked(By by)
    {
        String checked_attr = driver.findElement(by).getAttribute("checked");
        return checked_attr.equals("true");
    }

    private void sendAppToBackgroundAndBack(int seconds)
    {
        try {
            driver.runAppInBackground(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            driver.launchApp();
        }
    }

    /* SWIPES */
    protected void swipeUp(int timeOfSwipe)
    {
        Dimension size = driver.manage().window().getSize(); // get Size of our screen
        int x = (int) (size.width / 2); // get half of X - this is line on X we are going to swipe on
        int starty = (int) (size.height * 0.8); // get part of Y - this is start point on Y we are going to swipe from
        int endy = (int) (size.height * 0.2); // get part of Y - this is end point on Y we are going to swipe to

        TouchAction action = new TouchAction(driver);
        action.press(x, starty);
        action.waitAction(Duration.ofMillis(timeOfSwipe));
        action.moveTo(x, endy);
        action.release();
        action.perform();
    }

    protected void swipeUpQuick()
    {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String error_message, int max_swipes)
    {
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) { // will stop while if element found
            ++already_swiped;

            // will throw exception if element still not found after max_swipes swipes
            if (already_swiped > max_swipes) {
                waitForElementPresent(by, "Cannot find element by swiping up.\n" + error_message, 0);
                return;
            }
            //System.out.println(already_swiped); // we could print an amount of swipes
            swipeUpQuick(); // will swipe of element still not found
        }
    }

    protected void swipeOnElementToLeft(By by, String error_message)
    {
        WebElement element = waitForElementPresent(by, error_message, 10);

        int left_X = element.getLocation().getX();
        int right_X = left_X + element.getSize().getWidth();
        int upper_Y = element.getLocation().getY();
        int lower_Y = upper_Y + element.getSize().getHeight();
        int middle_Y = (upper_Y + lower_Y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_X, middle_Y)
                .waitAction(Duration.ofMillis(150))
                .moveTo(left_X, middle_Y)
                .release()
                .perform();
    }
    /* SWIPES */
}
