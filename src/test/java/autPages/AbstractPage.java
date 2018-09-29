package autPages;

import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.screenshot.ICustomScreenshotGrabber;
import driver.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;


public class AbstractPage implements ICustomScreenshotGrabber {
    protected WebDriver driver;
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPage.class);
    // wait period before you poll again
    int waitPollingInterval = 200;
    // this gives to control to fine-tune script performance as per the environment you are testing
    public int envFactor =Integer.parseInt(System.getenv("ENE_PERFORMANCE_FACTOR"));

    public AbstractPage() {
        this.driver = Driver.webDriver;
    }

    public void customWait(int i) {
        LOG.info("custom wait for {} milliseconds", i);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public boolean waitForPageToBeReady(int pageLoadTimeout)
    {
        for(int wait = 1; wait<=pageLoadTimeout; wait= wait+waitPollingInterval) {
            if (((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")&& (Boolean)((JavascriptExecutor) driver).executeScript("return !!window.jQuery && window.jQuery.active == 0"))
                return true;
            else {
                LOG.info("Waiting for page load finish");
                customWait(waitPollingInterval);
            }
        }
        return false;
    }

    public boolean isPageLoaded(int pageLoadTimeout, String expPageUrlPart )
    {
        customWait(waitPollingInterval);
        if (!driver.getCurrentUrl().contains(expPageUrlPart))
            customWait(2000*envFactor);
        else
            return waitForPageToBeReady(pageLoadTimeout-(waitPollingInterval));
        return false;
    }

    public WebElement findElement(WebDriver webDriver, By by, int iTimeOut)
    {
        int i =0;
        while (i < iTimeOut) {
            List<WebElement> oWebElements = webDriver.findElements(by);
            if (oWebElements.size() > 0) {
                LOG.info("Element found: "+ by);
                return oWebElements.get(0);
            }
            else {
                LOG.warn("cant find element: {}, retry in {} milliseconds", by, waitPollingInterval);
                try {
                    customWait(waitPollingInterval);
                    i+=waitPollingInterval;
                    LOG.info(String.format("Waited for %d milliseconds.[%s]", i ,by));
                } catch (Exception ex) {
                    LOG.error(ex.getMessage());
                    return null;
                }
            }
        }
        LOG.error("Can't find {} after {} milliseconds.", by, iTimeOut);
        return null;
    }

    public boolean input(WebDriver webDriver, By by, String objectName,String data)
    {
        String stepDetails;
        boolean bResult = false;
        try
        {
            WebElement oInput = findElement(webDriver, by, waitPollingInterval);
            if (oInput!=null){
                input(oInput, objectName,data);
                stepDetails = "Enter value for " + objectName + ": " + data;
                Gauge.writeMessage(stepDetails);
                bResult = true;
            }
            else
                LOG.error("Element is not found");
        } catch (Exception ex)
        {
            LOG.error("Fail to enter the value for " + objectName);
            ex.printStackTrace();
            LOG.error(ex.getMessage());
        }
        return bResult;
    }

    public boolean input(WebElement we, String objectName,String data)
    {
        String stepDetails;
        boolean bResult = false;
        // Input value to object
        {
            we.clear();
            we.sendKeys(data);
            if (objectName.toUpperCase().contains("PASSWORD"))
                data="*********";
            stepDetails = "Enter value for " + objectName + ": " + data;
            LOG.info(stepDetails);
            Gauge.writeMessage(stepDetails);
            bResult = true;
        }
        return bResult;
    }

    public boolean doesWebElementExist(WebDriver webDriver, By by,int iTimeOut) {
        WebElement element=findElement(webDriver, by, iTimeOut);
        return element != null;
    }

    public boolean clickAndWait(WebDriver webDriver, By by, String objectName, int waitTime)
    {
        return clickAndWait(findElement(webDriver,by, waitTime/2), objectName, waitTime/2);
    }

    public boolean clickAndWait(WebElement we, String objectName, int waitTime)
    {
        if (we == null) return false;
        try {
            customWait(200);
            Actions actions = new Actions(driver);
            actions.moveToElement(we);
            actions.perform();
            WebDriverWait wait = new WebDriverWait(driver, waitPollingInterval);
            wait.until(ExpectedConditions.elementToBeClickable(we));
            // Click on Object
            we.click();
            Gauge.writeMessage("Click on "+ objectName);
            LOG.info("Click on {} and Wait for {} milliseconds", objectName, waitTime);
            Thread.sleep(waitTime);
        } catch (Exception ex) {
            Gauge.writeMessage("Click on "+ objectName+" failed");
            LOG.error("Got an exception! ", Arrays.toString(ex.getStackTrace()));
            // Log.info(ex.getStackTrace().toString());
            return false;
        }
        return true;
    }
    @Override
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
