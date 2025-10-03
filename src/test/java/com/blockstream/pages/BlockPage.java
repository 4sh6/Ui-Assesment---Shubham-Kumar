package com.blockstream.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.blockstream.utils.WaitHelper;
import com.blockstream.model.Transaction;
import com.blockstream.config.TestConfig;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BlockPage {
    private WebDriver driver;
    private WaitHelper waitHelper;
    private WebDriverWait wait;


    private By transactionContainer = By.xpath("//*[@id='explorer']/div/div/div[2]/div[2]/div[3]");
    private By transactionCards = By.xpath(".//div[@id='transaction-box']");
    private By inputElements = By.xpath(".//div[@class='vins']/div[@class='vin']");
    private By outputElements = By.xpath(".//div[@class='vouts']/div[@class='vout']");

    public BlockPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.WAIT_TIMEOUT));
    }

    public void goToBlockPage() {
        driver.get(TestConfig.BLOCK_URL);
        waitHelper.waitForPageToLoad();
    }

    public String getTransactionHeaderText() {
        WebElement container = waitHelper.waitForElementToBeVisible(transactionContainer);
        WebElement header = container.findElement(By.tagName("h3"));
        return header.getText().trim();
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        WebElement container = waitHelper.waitForElementToBePresent(transactionContainer);

        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(container, transactionCards));

        List<WebElement> transactionElements = container.findElements(transactionCards);

        System.out.println("Found " + transactionElements.size() + " transactions to process");

        for (WebElement transactionElement : transactionElements) {
            try {
                WebElement hashElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                        transactionElement, By.cssSelector(".header a")));
                String hash = hashElement.getText();

                int inputCount = transactionElement.findElements(inputElements).size();
                int outputCount = transactionElement.findElements(outputElements).size();

                Transaction transaction = new Transaction(hash, inputCount, outputCount);
                transactions.add(transaction);

            } catch (Exception e) {
                System.out.println("Skipped a transaction due to missing elements");
            }
        }

        return transactions;
    }

    public List<String> getTransactionsWithOneInputTwoOutputs() {
        List<Transaction> allTransactions = getAllTransactions();
        List<String> matchingHashes = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.hasOneInputAndTwoOutputs()) {
                matchingHashes.add(transaction.getHash());
            }
        }

        return matchingHashes;
    }
}