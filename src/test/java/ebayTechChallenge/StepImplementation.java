package ebayTechChallenge;

import autPages.HomePage;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation {

    WebDriver driver = Driver.webDriver;
    HomePage home = new HomePage();
    Map<String, String> itemsInCart = new HashMap<String, String>();;

    @Step("Go to ebay home page")
    public void gotoHomePageEbay() {
        String app_url = System.getenv("APP_URL");
        driver.get(app_url + "/");
        assertThat(driver.getTitle()).contains("eBay");
    }

    @Step("Enter the keyword <>")
    public void searchKeyword(String keyWord) {
        home.search(keyWord);
    }

    @Step("Verify the search results contains the keyword <>")
    public void verifyResults(String keyword) {
        home.verifySearch(keyword);

    }

    @Step("Verify number of items in the cart to be <>")
    public void checkCart(int countOfItems) {
        home.countItemInCart(countOfItems);

    }

    @Step("Add option number <1> to the shopping cart")
    public void addToCart(String itemRow) {
        String item = home.addOptionToCart(itemRow);
        itemsInCart.put("Item1",item);
    }


    @Step("Verify cart has item with details <>")
    public void itemDetailsInCart(String details) {

    }


}
