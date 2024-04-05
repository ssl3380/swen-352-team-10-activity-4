import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

class DeptInfo {
    private static final Browser BROWSER
            = Browser.FIREFOX; // Can be changed to Browser.CHROME

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.err.println("Will try WebDriver at path:");
        System.err.println(Browser.getWebDriverPathFor(BROWSER));
    }

    @BeforeEach
    void setUp() {
        driver = BROWSER.setUpWebDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void testDepartmentChairOfSEJcs8198() throws Exception {
        driver.get("https://rit.edu/");
        driver.findElement(By.cssSelector("a.black-on-white:nth-child(2)")).click();
        driver.findElement(By.cssSelector("div.form-group:nth-child(4) > label:nth-child(2)")).click();
        WebElement searchField = driver.findElement(By.cssSelector("#edit-keys--2"));
        searchField.clear();
        searchField.sendKeys("Department Chair Department of Software Engineering");
        driver.findElement(By.cssSelector("#edit-submit-directory--2")).click();
        WebElement resultsParent = driver.findElement(By.cssSelector(".views-infinite-scroll-content-wrapper"));
        List<WebElement> results = resultsParent.findElements(By.className("views-row"));
        List<WebElement> resultInfo = results.getFirst().findElements(By.className("pb-2"));
        System.out.println("Name: " + resultInfo.getFirst().getText());
        System.out.println("Title: " + resultInfo.get(1).getText());
        System.out.println("Email: " + resultInfo.getLast().getText());
        System.out.println("Department: " + resultInfo.get(2).getText());
        System.out.println("College: " + resultInfo.get(resultInfo.size()-2).getText());
        System.out.println(" ");
        Thread.sleep(3000);
    }

    @Test
    void testDepartmentChairOfCSJcs8198() throws Exception {
        driver.get("https://rit.edu/");
        driver.findElement(By.cssSelector("a.black-on-white:nth-child(2)")).click();
        driver.findElement(By.cssSelector("div.form-group:nth-child(4) > label:nth-child(2)")).click();
        WebElement searchField = driver.findElement(By.cssSelector("#edit-keys--2"));
        searchField.clear();
        searchField.sendKeys("Department Chair Department of Computer Science");
        driver.findElement(By.cssSelector("#edit-submit-directory--2")).click();
        WebElement resultsParent = driver.findElement(By.cssSelector(".views-infinite-scroll-content-wrapper"));
        List<WebElement> results = resultsParent.findElements(By.className("views-row"));
        List<WebElement> resultInfo = results.get(1).findElements(By.className("pb-2"));
        System.out.println("Name: " + resultInfo.getFirst().getText());
        System.out.println("Title: " + resultInfo.get(1).getText());
        System.out.println("Email: " + resultInfo.getLast().getText());
        System.out.println("Department: " + resultInfo.get(2).getText());
        System.out.println("College: " + resultInfo.get(resultInfo.size()-2).getText());
        System.out.println(" ");
        Thread.sleep(3000);
    }

    @Test
    void testDepartmentChairOfSIJcs8198() throws Exception {
        driver.get("https://rit.edu/");
        driver.findElement(By.cssSelector("a.black-on-white:nth-child(2)")).click();
        driver.findElement(By.cssSelector("div.form-group:nth-child(4) > label:nth-child(2)")).click();
        WebElement searchField = driver.findElement(By.cssSelector("#edit-keys--2"));
        searchField.clear();
        searchField.sendKeys("Director School of Information");
        driver.findElement(By.cssSelector("#edit-submit-directory--2")).click();
        WebElement resultsParent = driver.findElement(By.cssSelector(".views-infinite-scroll-content-wrapper"));
        List<WebElement> results = resultsParent.findElements(By.className("views-row"));
        List<WebElement> resultInfo = results.get(6).findElements(By.className("pb-2"));
        System.out.println("Name: " + resultInfo.getFirst().getText());
        System.out.println("Title: " + resultInfo.get(1).getText());
        System.out.println("Email: " + resultInfo.getLast().getText());
        System.out.println("Department: " + resultInfo.get(2).getText());
        System.out.println("College: " + resultInfo.get(resultInfo.size()-2).getText());
        System.out.println(" ");
        Thread.sleep(3000);
    }

    private enum Browser {
        CHROME("webdriver.chrome.driver",
                "chromedriver",
                ChromeDriver::new),
        FIREFOX("webdriver.gecko.driver",
                "geckodriver",
                FirefoxDriver::new);

        private final String driverPropertyKey;
        private final String driverBaseName;
        private final Supplier<WebDriver> webDriverSupplier;

        Browser(String driverPropertyKey,
                String driverBaseName,
                Supplier<WebDriver> webDriverSupplier) {
            this.driverPropertyKey = driverPropertyKey;
            this.driverBaseName = driverBaseName;
            this.webDriverSupplier = webDriverSupplier;
        }

        private static Path getWebDriverPathFor(Browser browser) {
            String driverFileName = browser.driverBaseName;
            if (System.getProperty("os.name").startsWith("Windows")) {
                driverFileName += ".exe";
            }
            return Paths.get(System.getProperty("user.dir"), driverFileName);
        }

        private WebDriver setUpWebDriver() {
            Path driverPath = getWebDriverPathFor(this);
            System.setProperty(this.driverPropertyKey, driverPath.toString());
            return this.webDriverSupplier.get();
        }
    }
}