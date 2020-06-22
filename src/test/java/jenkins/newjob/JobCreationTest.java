package jenkins.newjob;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class JobCreationTest {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    static final String JENKINS_URL = "http://127.0.0.1:8080/jenkins";
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin";
    static final String JOB_NAME = "myGenericJob";
    static final String JOB_TYPE = "hudson_model_FreeStyleProject";
    static final String PROJECT_URL = "https://github.com/SquashTF-workshop/skfCompareXML";
    static final String PROJECT_RUNNER = "Squash Keyword Framework";

    @BeforeEach
    public void setUp(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get(JENKINS_URL);
        wait = new WebDriverWait(driver, 30);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testNewJob(){

        //Login

        driver.findElement(By.xpath("//a[contains(@href,'login')][parent::div[@class='login']]")).click();

        driver.findElement(By.xpath("//input[@id='j_username']")).sendKeys(USERNAME);

        driver.findElement(By.xpath("//input[@name='j_password']")).sendKeys(PASSWORD);

        driver.findElement(By.xpath("//input[@name='Submit']")).click();

        //New job

        driver.findElement(By.xpath("//a[@href='/jenkins/view/all/newJob']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='name']")));

        driver.findElement(By.xpath("//input[@id='name']")).sendKeys(JOB_NAME);

        driver.findElement(By.xpath("//li[@class='" + JOB_TYPE + "']")).click();

        driver.findElement(By.xpath("//button[@id='ok-button']")).click();

        //Job configuration

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/jenkins/job/" + JOB_NAME + "/']")));

        //wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

        //wait.until(ExpectedConditions.jsReturnsValue("return jQuery.active == 0"));

        WebElement scmPanel = driver.findElement(By.xpath("//tr[contains(@title,'Gestion de code source') or contains(@title,'Source Code Management')]"));

        wait.until(ExpectedConditions.elementToBeClickable(scmPanel));

        js.executeScript("arguments[0].scrollIntoView();", scmPanel);

        driver.findElement(By.xpath("//input[@type='radio'][parent::label[contains(text(), 'Git')]]")).click();

        driver.findElement(By.xpath("//input[@checkurl='/jenkins/job/" + JOB_NAME + "/descriptorByName/hudson.plugins.git.UserRemoteConfig/checkUrl']")).sendKeys(PROJECT_URL);

        WebElement buildDDL = driver.findElement(By.xpath("//button[@suffix='builder']"));

        js.executeScript("arguments[0].scrollIntoView();", buildDDL);

        buildDDL.click();

        driver.findElement(By.xpath("//a[text()='Squash Build']")).click();

        driver.findElement(By.xpath("//select[@name='runner']")).click();

        driver.findElement(By.xpath("//option[@value='" + PROJECT_RUNNER + "']")).click();

        driver.findElement(By.xpath("//span[@name='Submit']")).click();
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }


}
