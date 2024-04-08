import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TigerCenterClass {
    private static final Browser BROWSER
            = Browser.CHROME; // Can be changed to Browser.CHROME

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
    void testClassSearchButton() throws Exception {
        driver.get("https://tigercenter.rit.edu/");
        WebElement classButton = driver.findElement(By.xpath("//*[@id=\"angularApp\"]/app-root/div[2]/mat-sidenav-container[2]/mat-sidenav-content/div[2]/landing-page/div/div/div/div/div[4]/a[1]"));
        assertEquals("Class Search", classButton.getText());
        classButton.click();
        Thread.sleep(1000);
    }

    @Test
    void testSoftwareTesting() throws Exception {
        classSearchButtonHelper("Software Testing");
    }

    @Test
    void testPersonalSE() throws Exception {
        classSearchButtonHelper("Personal Software Engineering");
    }

    void classSearchButtonHelper(String className) throws Exception {
        driver.get("https://tigercenter.rit.edu/");
        WebElement classButton = driver.findElement(By.xpath("//*[@id=\"angularApp\"]/app-root/div[2]/mat-sidenav-container[2]/mat-sidenav-content/div[2]/landing-page/div/div/div/div/div[4]/a[1]"));
        assertEquals("Class Search", classButton.getText());
        classButton.click();
        Select termSelector = new Select(driver.findElement(By.name("termSelector")));
        termSelector.selectByValue("1: 0");
        WebElement classInput = driver.findElement(By.cssSelector(".completer-input"));
        classInput.clear();
        classInput.sendKeys(className);
        driver.findElement(By.cssSelector((".classSearchSearchButton"))).click();
        WebElement resultsRowsParent = driver.findElement(By.cssSelector(".classSearchBasicResultsMargin"));
        List<WebElement> resultRows = resultsRowsParent.findElements(By.className("row"));
        resultRows.remove(0);
        for (WebElement specificResult : resultRows) {

            List<WebElement> resultsInfo = specificResult.findElements(By.className("classSearchBasicResultsText"));
            System.out.println("Name: " + resultsInfo.get(0).getText());
            System.out.println("Days: " + resultsInfo.get(6).getText());
            System.out.println("Times: " + resultsInfo.get(7).getText());
            System.out.println("Location: " + resultsInfo.get(8).getText());
            System.out.println("Instructor: " + resultsInfo.get(resultsInfo.size() - 1).getText());
            System.out.println(" ");
        }
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
