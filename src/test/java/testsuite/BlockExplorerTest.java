package testsuite;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class BlockExplorerTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL = "https://blockstream.info/block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test(priority = 1)
    public void validateTransactionSectionHeader() {
        driver.get(URL);

        // Wait for the transaction list to load
        WebElement txnHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"explorer\"]/div/div/div[2]/div[2]/div[3]/h3"))
        );

        String headerText = txnHeader.getText().trim();
        System.out.println("Transaction section header: " + headerText);

        // Verify header contains "25 of 2875 Transactions"
        Assert.assertTrue(headerText.contains("25 of 2875 Transactions"),
                "Header text mismatch: " + headerText);
    }

    @Test(priority = 2)
    public void parseTransactionsAndPrintMatchingOnes() {
        driver.get(URL);

        // Locate container holding all transactions
        WebElement transactionContainer = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id='explorer']/div/div/div[2]/div[2]/div[3]"))
        );

        // Get all transaction boxes
        List<WebElement> transactionCards = transactionContainer.findElements(
                By.xpath(".//div[@id='transaction-box']")
        );

        System.out.println("Total transactions found: " + transactionCards.size());

        for (WebElement txn : transactionCards) {
            try {
                // Transaction hash
                WebElement hashElem = txn.findElement(By.cssSelector(".header a"));
                String txnHash = hashElem.getText();

                // Count inputs (vin)
                int inputCount = txn.findElements(
                                By.xpath(".//div[@class='vins']/div[@class='vin']"))
                        .size();

                // Count outputs (vout)
                int outputCount = txn.findElements(
                                By.xpath(".//div[@class='vouts']/div[@class='vout']"))
                        .size();

                // Print if it matches the required condition
                if (inputCount == 1 && outputCount == 2) {
                    System.out.println("Transaction with 1 input & 2 outputs: " + txnHash);
                }

            } catch (NoSuchElementException e) {
                System.err.println("Skipping a transaction due to missing elements.");
            }
        }
    }
}
