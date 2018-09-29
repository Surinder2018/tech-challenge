package driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    // Get a new WebDriver Instance.
    // There are various implementations for this depending on browser. The required browser can be set as an environment variable.
    public static WebDriver getDriver()  throws MalformedURLException{

        String browser = System.getenv("BROWSER");
        browser = (browser == null) ? "CHROME": browser;
        DesiredCapabilities desiredCapabilities;
        if(System.getenv("SELENIUM_GRID").equalsIgnoreCase("true")) {
            switch (browser) {
                case "IE":
                    InternetExplorerDriverManager.getInstance().setup();
                    return new InternetExplorerDriver();
                case "FIREFOX":
                    desiredCapabilities = DesiredCapabilities.firefox();
                    return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities);
                case "CHROME":
                default:
                    ChromeOptions options = new ChromeOptions();
                    if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                        options.addArguments("--headless");
                        options.addArguments("--disable-gpu");
                    }
                    return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);

            }
        }
        else
        {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            if (browser == null) {
                ChromeDriverManager.getInstance().setup();
                return new ChromeDriver(options);
            }
            switch (browser) {
                case "IE":
                    InternetExplorerDriverManager.getInstance().setup();
                    return new InternetExplorerDriver();
                case "FIREFOX":
                    FirefoxDriverManager.getInstance().setup();
                    return new FirefoxDriver();
                case "CHROME":
                    ChromeDriverManager.getInstance().setup();
                    return new ChromeDriver(options);
                default:
                    ChromeDriverManager.getInstance().setup();
                    return new ChromeDriver();
            }
        }
    }
}
