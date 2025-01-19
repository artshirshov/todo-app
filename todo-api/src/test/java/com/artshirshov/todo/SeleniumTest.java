package com.artshirshov.todo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {

    static WebDriver driver;
    static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        driver.get("http://localhost:5173/");
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    @Test
    @Order(1)
    void testCreationTask() {
        String expectedTitle = "Test Title Selenium";
        String expectedDescription = "Test Description Selenium";
        String expectedTag = "Test Tag Selenium";

        fillTaskForm(expectedTitle, expectedDescription, expectedTag);

        WebElement button = driver.findElement(
                By.xpath("//*[@id=\"root\"]/div/form/button")
        );

        button.click();

        String actualTitle = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/span")
                )
        ).getText();
        String actualDescription = driver.findElement(
                By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/p")
        ).getText().split("\n")[0];
        String actualTag = driver.findElement(
                By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/p/div/div")
        ).getText();

        Assertions.assertEquals(expectedTitle, actualTitle);
        Assertions.assertEquals(expectedDescription, actualDescription);
        Assertions.assertEquals(expectedTag, actualTag);
    }

    @Test
    @Order(2)
    void testEditTask() throws InterruptedException {
        String expectedTitle = "Edit Test Title Selenium";
        String expectedDescription = "Edit Test Description Selenium";

        WebElement editButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id=\"root\"]/div/ul/li/div[2]/button[1]")
                ));
        editButton.click();

        WebElement title = driver.findElement(By.xpath("//*[@id=\":r7:\"]"));
        WebElement description = driver.findElement(By.xpath("//*[@id=\":r9:\"]"));
        WebElement saveButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[5]/button[2]"));

        clearFieldSlowly(title);
        title.sendKeys(expectedTitle);
        clearFieldSlowly(description);
        description.sendKeys(expectedDescription);

        saveButton.click();

        Thread.sleep(100);
        String actualTitle = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/span")
                )).getText();
        String actualDescription = driver.findElement(
                By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/p")
        ).getText().split("\n")[0];

        Assertions.assertEquals(expectedTitle, actualTitle);
        Assertions.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @Order(3)
    void testEditTagTask() {
        String expectedTag = "Edit Test Tag Selenium";

        WebElement editButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id=\"root\"]/div/ul/li/div[2]/button[1]")
                ));
        editButton.click();

        WebElement tagTitle = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("/html/body/div[2]/div[3]/div[3]/div/div/input")
                ));
        WebElement tagButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/button"));
        WebElement saveButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[5]/button[2]"));

        tagTitle.sendKeys(expectedTag);
        tagButton.click();
        saveButton.click();

        WebElement tagContainer = wait.until(
                ExpectedConditions.elementToBeClickable(
                        (By.cssSelector(".MuiBox-root.css-164r41r"))
                ));
        List<WebElement> tagElements = tagContainer.findElements(By.cssSelector(".MuiChip-label"));

        List<String> tags = tagElements.stream()
                .map(WebElement::getText)
                .toList();

        Assertions.assertEquals(expectedTag, tags.getLast());
    }

    @Test
    @Order(4)
    void testDeleteTask() throws InterruptedException {
        WebElement deleteButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id=\"root\"]/div/ul/li/div[2]/button[2]")
                )
        );

        List<WebElement> taskListBefore = driver.findElements(By.xpath("//*[@id=\"root\"]/div/ul/li"));
        String deletedTaskTitle = taskListBefore.getFirst()
                .findElement(By.xpath("//*[@id=\"root\"]/div/ul/li/div[1]/span"))
                .getText();

        deleteButton.click();

        Thread.sleep(200);
        List<WebElement> taskListAfter = driver.findElements(By.xpath("//*[@id=\"root\"]/div/ul/li"));

        boolean isTaskStillPresent = taskListAfter.stream()
                .anyMatch(task -> task.findElement(By.xpath("./div[1]/span"))
                        .getText()
                        .equals(deletedTaskTitle)
                );

        Assertions.assertFalse(isTaskStillPresent);
    }

    private void fillTaskForm(String title, String description, String tag) {
        WebElement titleField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\":r1:\"]")));
        titleField.clear();
        titleField.sendKeys(title);

        WebElement descriptionField = driver.findElement(By.xpath("//*[@id=\":r3:\"]"));
        descriptionField.clear();
        descriptionField.sendKeys(description);

        WebElement tagField = driver.findElement(By.xpath("//*[@id=\":r5:\"]"));
        tagField.clear();
        tagField.sendKeys(tag);

        WebElement addTagButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[3]/button"));
        addTagButton.click();
    }

    private void clearFieldSlowly(WebElement element) {
        element.click();
        String value = element.getAttribute("value");
        for (int i = 0; i < value.length(); i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input'))", element);
    }
}

