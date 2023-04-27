package test.java.com.lambdatest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MacScenario {
    private RemoteWebDriver driver;

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "nkharche" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "gz6Vg50u8RNXuicIh309qQJOnh1dUXQAUQIkXW8m5kqbsuf65I" : System.getenv("LT_ACCESS_KEY");

        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        EdgeOptions browserOptions = new EdgeOptions();
        caps.setCapability("platform", "macOS Sierra");
        caps.setCapability("version", "87.0");
        caps.setCapability("name", "Test-Lambda-Mac");
        caps.setCapability("tunnel", false);
        caps.setCapability("video", true);
        caps.setCapability("console", true);
        caps.setCapability("visual", true);
//        caps.setCapability("resolution", "2560x1440");
        caps.setCapability("build", "TestLambdaDemo");
        caps.setCapability("w3c", true);
        caps.setCapability("network", false);
        caps.setCapability("plugin", "java-testNG");
        browserOptions.setCapability("LT:Options", caps);
        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), browserOptions);

    }

    @Test(timeOut = 20000)
    public void openSeeAllIntegrationLink() throws InterruptedException {

        driver.get("https://www.lambdatest.com");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        WebElement seeAllIntegration = driver.findElement(By.xpath("//div[@class='text-center mt-25']//child::a"));
        WebElement JenkinsImg = driver.findElement(By.xpath("//img[@title='Jenkins']"));//if we try to scroll upto See all Integration link it scroll more than required.and Element not get clickable.
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", JenkinsImg);//to scroll till element
        waitUntilClickable(seeAllIntegration);
        Actions action = new Actions(driver);
        action.keyDown(Keys.COMMAND).build().perform();//To open on New Window
        seeAllIntegration.click();
        action.keyUp(Keys.COMMAND).build().perform();
        System.out.println("On the child window");
        Set<String> windowsiIds = driver.getWindowHandles();
        System.out.println("All Ids are" + windowsiIds);
        ArrayList<String> id = new ArrayList<String>(windowsiIds);
        String childWindowId = id.get(1);
        String parentWindowId = id.get(0);
        System.out.println("ID of Child Window" + childWindowId);
        System.out.println("ID of Parent Window" + parentWindowId);
        driver.switchTo().window(childWindowId);
        String chlidWindowURl = driver.getCurrentUrl();
        Assert.assertEquals(chlidWindowURl, "https://www.lambdatest.com/integrations", "New window URL is not same as Expected.");
    }

    public void waitUntilClickable(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
