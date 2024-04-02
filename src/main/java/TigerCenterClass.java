import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.function.Supplier;

class TigerCenterClass {
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
    void testClassSearchButton() throws Exception {
        driver.get("https://tigercenter.rit.edu/");
        WebElement classButton = driver.findElement(By.xpath("//*[@id=\"angularApp\"]/app-root/div[2]/mat-sidenav-container[2]/mat-sidenav-content/div[2]/landing-page/div/div/div/div/div[4]/a[1]"));
        assertEquals("Class Search", classButton.getText());
        classButton.click();
        Thread.sleep(1000);
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
