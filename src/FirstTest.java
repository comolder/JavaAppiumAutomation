import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

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

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
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
