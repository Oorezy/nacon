package com.app.nacon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class ScrapingService {

    public void scrape() throws IOException {

        String url = "https://www.maersk.com/tracking/APU131179"; // Fake URL

        Document doc = Jsoup.connect(url).get();

        Element etaElement = doc.selectFirst(""); // Use actual class/id/tag

        if (etaElement != null) {
            String eta = etaElement.text();
            System.out.println("ETA: " + eta);
        } else {
            System.out.println("ETA not found.");
        }
    }

    public void scrape2() throws IOException {
        System.out.println("\n\nscrape started.\n");
        System.setProperty("webdriver.chrome.driver", "C:\\projects\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);

        // Simulate user interaction
        WebElement input = driver.findElement(By.id("mc-input-track-input")); // Replace with actual input field
        input.sendKeys("APU131179");

        WebElement submit = driver.findElement(By.id("button")); // Replace with actual button
        submit.click();

        /*try {
            driver.get("https://www.maersk.com/tracking/252257366");
//            Thread.sleep(Duration.ofSeconds(20));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement shadowHost = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("mc-text-and-icon"))
            );

//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement slotElement = wait.until(
//                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("slot.sublabel[name='sublabel']"))
//            );
            // Access shadow DOM
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement shadowRoot = (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);

            // Now find the <slot> inside the shadow DOM
            WebElement slotElement = shadowRoot.findElement(By.cssSelector("span.labels slot.sublabel[name='sublabel']"));



//            WebElement slotElement = driver.findElement(By.cssSelector("span.labels slot.sublabel[name='sublabel']"));
            String sublabelText = slotElement.getText();
            System.out.println("Sublabel: " + sublabelText);
        } finally {
            driver.quit();
        }*/
        try {
            driver.get("https://www.maersk.com/tracking"); // Replace with actual URL

            // Wait for the page to fully load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement sublabelElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("mc-text-and-icon")
                    )
            );

            // Scroll to the element if needed
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", sublabelElement);

            // Wait again if needed for the element to be fully visible
            wait.until(ExpectedConditions.visibilityOf(sublabelElement));

            // Extract the date from the <slot> element
            String sublabelText = sublabelElement.getText();
            System.out.println("Estimated Arrival Date: " + sublabelText);

        } finally {
            driver.quit();
        }
        System.out.println("\n\nscrape ended.\n");

    }
    /*
    *
    *  try {
            driver.get("https://example.com/track?bl=ABC123456"); // Replace with actual URL

            // Wait for the shadow root to be ready
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement shadowHost = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("your-parent-element"))
            );

            // Access shadow DOM
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement shadowRoot = (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);

            // Now find the <slot> inside the shadow DOM
            WebElement sublabelElement = shadowRoot.findElement(By.cssSelector("span.labels slot.sublabel[name='sublabel']"));

            // Get the date text from the second <slot> element
            String sublabelText = sublabelElement.getText();
            System.out.println("Estimated Arrival Date: " + sublabelText);
        } finally {
            driver.quit();
        }*/

}
