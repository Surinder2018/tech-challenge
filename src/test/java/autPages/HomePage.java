package autPages;

import com.thoughtworks.gauge.Gauge;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomePage extends AbstractPage {
    String pageTitle  = "eBay";

    public void go(){
        String app_url = System.getenv("APP_URL");
        driver.get(app_url + "/");
        // verify page title and wait for page load
        isPageLoaded(300* envFactor,pageTitle);

    }

    public void search(String keyWord){
        input(driver, By.id(System.getenv("HOME_SEARCH_INPUT_ID")),"Search input",keyWord);
        clickAndWait(driver,By.id(System.getenv("HOME_SEARCH_BTN_ID")), "Search",envFactor *200);
    }

    public void verifySearch(String keyWord){
        waitForPageToBeReady(2000*envFactor);
        WebElement resultsUl =  findElement(driver, By.id(System.getenv("HOME_SEARCH_RESULT_UL_ID")), envFactor *1000).findElement(By.tagName("ul"));
        Gauge.writeMessage("No. of results found "+ resultsUl.findElements(By.tagName("li")).size());
        // select the first given option
        WebElement firstResultLink = findElement(driver,By.id(System.getenv("HOME_SEARCH_RESULT_LI_ID")+"1"), envFactor *200).findElement(By.tagName("a"));
        // verify this item is relevant to the searched keyword
        String linkTxt = firstResultLink.getAttribute("href").toLowerCase();
        LOG.info(linkTxt);
        Gauge.writeMessage("Item link: " +linkTxt + "does contain keyword "+ keyWord +" : " +linkTxt.contains(keyWord));
        Assert.assertTrue(linkTxt.contains(keyWord));
    }

    public String addOptionToCart(String optionNo){
        // click link for required option
        WebElement linkEle =findElement(driver,By.cssSelector(System.getenv("HOME_SEARCH_RESULT_LI_CSS")), envFactor *200).findElement(By.tagName("a"));
        String productDescription = linkEle.getText();
        clickAndWait(linkEle,"Option",envFactor*300);
        //add to cart
        clickAndWait(driver,By.id("atcRedesignId_btn"),"add to cart",800*envFactor);
        Gauge.writeMessage(productDescription + " is added to cart");
        gotoCart();
        return productDescription;
    }

    public void countItemInCart( int expNumber) {
        waitForPageToBeReady(500*envFactor);
        String count = findElement(driver,By.id("gh-cart-n"),envFactor*800).getText();
        Gauge.writeMessage("Number of items in cart "+ count);
        Assert.assertEquals(expNumber,Integer.parseInt(count));
    }

    public void gotoCart() {
        clickAndWait(driver,By.linkText("Go to cart"), "Go to cart",envFactor *1000);
        if( isPageLoaded(2000*envFactor,"cart.ebay"))
            Assert.assertTrue(driver.getCurrentUrl().contains("cart.ebay"));
    }

    public void checkItemInCart(String itemDetails){
    }
}
