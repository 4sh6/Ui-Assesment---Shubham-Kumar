package com.blockstream.testsuite;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.blockstream.pages.BlockPage;
import com.blockstream.utils.DriverManager;
import java.util.List;

public class BlockExplorerTest {
    private WebDriver driver;
    private BlockPage blockPage;

    @BeforeClass
    public void setup() {
        driver = DriverManager.getDriver();
        blockPage = new BlockPage(driver);
    }

    @Test(priority = 1)
    public void validateTransactionSectionHeader() {
        // Navigate to block page
        blockPage.goToBlockPage();


        String headerText = blockPage.getTransactionHeaderText();
        System.out.println("Transaction section header: " + headerText);


        Assert.assertTrue(headerText.contains("25 of 2875 Transactions"),
                "Header should contain '25 of 2875 Transactions' but got: " + headerText);
    }

    @Test(priority = 2)
    public void parseTransactionsAndPrintMatchingOnes() {

        blockPage.goToBlockPage();


        List<String> matchingTransactions = blockPage.getTransactionsWithOneInputTwoOutputs();


        System.out.println("TRANSACTIONS WITH 1 INPUT AND 2 OUTPUTS =");

        if (matchingTransactions.isEmpty()) {
            System.out.println("No transactions found with exactly 1 input and 2 outputs.");
        } else {
            System.out.println("Found " + matchingTransactions.size() + " matching transactions:");
            for (int i = 0; i < matchingTransactions.size(); i++) {
                System.out.println((i + 1) + ". " + matchingTransactions.get(i));
            }
        }
    }


    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}