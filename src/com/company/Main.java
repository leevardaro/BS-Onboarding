package com.company;

    import java.net.MalformedURLException;
    import java.net.URL;
    import java.time.Duration;
    import java.util.List;
    import java.util.Hashtable;
    import java.util.Iterator;
    import java.util.Set;
    import org.openqa.selenium.By;
    import org.openqa.selenium.JavascriptExecutor;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.remote.DesiredCapabilities;
    import org.openqa.selenium.remote.RemoteWebDriver;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

class Chrome implements Runnable {
    public void run() {
        Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
        capsHashtable.put("os", "Windows");
        capsHashtable.put("os_version", "10");
        capsHashtable.put("browser", "Chrome");
        capsHashtable.put("browser_version", "latest");
        capsHashtable.put("build", "BStack-[Java] Sample Build");
        capsHashtable.put("name", "Chrome");
        Main r1 = new Main();
        r1.executeTest(capsHashtable);
    }
}
class Firefox implements Runnable {
    public void run() {
        Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
        capsHashtable.put("os", "Windows");
        capsHashtable.put("os_version", "10");
        capsHashtable.put("browser", "Firefox");
        capsHashtable.put("browser_version", "latest");
        capsHashtable.put("build", "BStack-[Java] Sample Build");
        capsHashtable.put("name", "Firefox");
        Main r1 = new Main();
        r1.executeTest(capsHashtable);
    }
}
class Safari implements Runnable {
    public void run() {
        Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
        capsHashtable.put("browser", "Safari");
        capsHashtable.put("browser_version", "14");
        capsHashtable.put("os", "OS X");
        capsHashtable.put("os_version", "Big Sur");
        capsHashtable.put("build", "BStack-[Java] Sample Build");
        capsHashtable.put("name", "Safari");
        Main r1 = new Main();
        r1.executeTest(capsHashtable);
    }
}
public class Main {
    public static final String USERNAME = "";
    public static final String AUTOMATE_KEY = "";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static void main(String[] args) throws Exception {
        Thread object1 = new Thread(new Chrome());
        object1.start();
        Thread object2 = new Thread(new Firefox());
        object2.start();
        Thread object3 = new Thread(new Safari());
        object3.start();
    }


    public void executeTest(Hashtable<String, String> capsHashtable) {
        String key;
        DesiredCapabilities caps = new DesiredCapabilities();
        // Iterate over the hashtable and set the capabilities
        Set<String> keys = capsHashtable.keySet();
        Iterator<String> itr = keys.iterator();
        while (itr.hasNext()) {
            key = itr.next();
            caps.setCapability(key, capsHashtable.get(key));
        }
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, 10);


            String baseUrl = "https://www.amazon.com";

            //go to amazon search iPhone
            driver.get(baseUrl);
            WebElement searchInput = driver.findElement(By.id("twotabsearchtextbox"));
            searchInput.sendKeys("iphone x");
            searchInput.submit();

            //filter
            WebElement iosFilter = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[.='iOS']/..//i")));
            iosFilter.click();
            WebElement clickSort = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("a-autoid-0-announce")));
            clickSort.click();
            WebElement sort = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[.='Price: High to Low']")));
            sort.click();

            //Wait to make sure filters are applied
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            //get row count of results
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']")));
            List<WebElement> links = driver.findElements(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']"));
            int rowCount = links.size();
            System.out.println(capsHashtable.get("browser") + " " + "Results row count is " + rowCount);

            //arrays for search results data;
            String name;
            String wholePrice;
            String decimalPrice;
            String totalPrice;
            String link;

            //for loop for pulling in values
            for (int i = 1; i <= rowCount; i++) {

                name = driver.findElement(By.xpath("(//span[@class='a-size-medium a-color-base a-text-normal'])[" + i + "]")).getText();
                Boolean hasPrice = driver.findElements(By.xpath("(//*[@data-component-type='s-search-result'])[" + i + "]//span[@class='a-price']//span[@class='a-price-whole']")).size() > 0;
                if (hasPrice == true) {
                    wholePrice = driver.findElement(By.xpath("(//*[@data-component-type='s-search-result'])[" + i + "]//span[@class='a-price']//span[@class='a-price-whole']")).getText();
                    decimalPrice = driver.findElement(By.xpath("(//*[@data-component-type='s-search-result'])[" + i + "]//span[@class='a-price']//span[@class='a-price-fraction']")).getText();
                    totalPrice = "$" + wholePrice + "." + decimalPrice;
                } else
                    totalPrice = "varies by color";

                link = driver.findElement(By.xpath("(//*[@data-component-type='s-search-result'])[1]//a[@class='a-link-normal a-text-normal']")).getAttribute("href");

                System.out.println(capsHashtable.get("browser") + " " + "Result " + i + " name is " + name);
                System.out.println(capsHashtable.get("browser") + " " + "Result " + i + " price = " + totalPrice);
                System.out.println(capsHashtable.get("browser") + " " + "Result " + i + " link = " + link);

            }


            driver.quit();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
    }
}