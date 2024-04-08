import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeptInfo {
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
    void testDepartmentChairOfSE() throws Exception {
        Map<String, String> expectedMap = new HashMap<>() {{
            put("Name", "Naveen Sharma");
            put("Email", "nxsvse@rit.edu");
            put("Title", "Department Chair");
            put("Department", "Department of Software Engineering");
            put("College", "Golisano College of Computing and Information Sciences");
        }};
        String cardHeaderCSSSelector = "#card-header-4291";
        String cardCollapseCSSSelector = "#card-collapse-4291";
        Map<String, String> resultMap = departmentChairSearchHelper(cardHeaderCSSSelector, cardCollapseCSSSelector);
        assertEquals(expectedMap, resultMap);
    }

    @Test
    void testDepartmentChairOfCS() throws Exception {
        Map<String, String> expectedMap = new HashMap<>() {{
            put("Name", "Zachary Butler");
            put("Email", "zxbvcs@rit.edu");
            put("Title", "Interim Department Chair");
            put("Department", "Department of Computer Science");
            put("College", "Golisano College of Computing and Information Sciences");
        }};
        String cardHeaderCSSSelector = "#card-header-4303";
        String cardCollapseCSSSelector = "#card-collapse-4303";
        Map<String, String> resultMap = departmentChairSearchHelper(cardHeaderCSSSelector, cardCollapseCSSSelector);
        assertEquals(expectedMap, resultMap);
    }

    @Test
    void testDepartmentChairOfSI() throws Exception {
        Map<String, String> expectedMap = new HashMap<>() {{
            put("Name", "Eva Navarro");
            put("Email", "eva.navarro@rit.edu");
            put("Title", "Director School of Information");
            put("Department", "School of Information");
            put("College", "Golisano College of Computing and Information Sciences");
        }};
        String cardHeaderCSSSelector = "#card-header-4315";
        String cardCollapseCSSSelector = "#card-collapse-4315";
        Map<String, String> resultMap = departmentChairSearchHelper(cardHeaderCSSSelector, cardCollapseCSSSelector);
        assertEquals(expectedMap, resultMap);
    }

    Map<String, String> departmentChairSearchHelper(String cardHeaderCSSSelector, String cardCollapseCSSSelector) throws Exception {
        String expandDeptByCSSSelector = cardHeaderCSSSelector + " > p > a";
        String personalInfoByCSSSelector = cardCollapseCSSSelector + " > div > div > article > div > div.col-xs-12.col-sm-5.person--info";
        String personExtraTextByCSSSelector = cardCollapseCSSSelector + " > div > div > article > div > div.col-xs-12.col-sm-4.person--extra-text";
        driver.get("https://rit.edu/");
        driver.findElement(By.cssSelector("#main-nav--link--academics")).click();
        driver.findElement(By.cssSelector("#block-rit-bootstrap-subtheme-rit-main-menu > ul > li.nav-item.expanded.dropdown.mouse-focus.show-subnav > div > div > ul:nth-child(2) > li:nth-child(1) > a")).click();
        driver.findElement(By.cssSelector("#block-rit-bootstrap-subtheme-content > div.field.field--name-field-content.field--type-entity-reference-revisions.field--label-hidden.field__items > div:nth-child(3) > div > div > div > div > div > div > div > div > ul > li:nth-child(3) > a")).click();
        driver.findElement(By.cssSelector("#block-rit-bootstrap-subtheme-rit-main-menu > ul > li:nth-child(7) > a")).click();
        driver.findElement(By.cssSelector(expandDeptByCSSSelector)).click();
        WebElement focusElement = driver.findElement(By.cssSelector(personalInfoByCSSSelector));
        Actions actions = new Actions(driver);
        actions.moveToElement(focusElement).perform();
        WebElement resultParent = driver.findElement(By.cssSelector(personalInfoByCSSSelector));
        List<WebElement> results = resultParent.findElements(By.className("pb-2"));
        WebElement resultParent1 = driver.findElement(By.cssSelector(personExtraTextByCSSSelector));
        List<WebElement> results1 = resultParent1.findElements(By.className("pb-2"));
        System.out.println("Name: " + results.get(0).getText());
        System.out.println("Email: " + results1.getFirst().getText());
        System.out.println("Title: " + results.get(1).getText());
        System.out.println("Department: " + results.get(2).getText());
        System.out.println("College: " + results.get(3).getText());
        System.out.println(" ");
        Thread.sleep(5000);
        return Map.of("Name", results.get(0).getText(), "Email", results1.getFirst().getText(), "Title",
                results.get(1).getText(),"Department", results.get(2).getText(), "College",
                results.get(3).getText());
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