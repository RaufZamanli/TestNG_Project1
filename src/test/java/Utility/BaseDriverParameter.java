package Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseDriverParameter {

    public  WebDriver driver;
    public static WebDriverWait wait;



    @BeforeClass
    @Parameters("browserType")
   public void bashlangicIshlemleri(String browserType) {

        Logger logger= Logger.getLogger("");
        logger.setLevel(Level.SEVERE);


        if (browserType.equalsIgnoreCase("firefox")) {
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            driver = new FirefoxDriver();
            System.out.println("firefox started");
        }
        else if (browserType.equalsIgnoreCase("Edge")){


            driver=new EdgeDriver();
            System.out.println("Edge started");
        }
        else {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        }

        wait=new WebDriverWait(driver,Duration.ofSeconds(15));
        // driver=new ChromeDriver();
        driver.manage().window().maximize();
        Duration dr = Duration.ofSeconds(30);
        driver.manage().timeouts().pageLoadTimeout(dr);
        driver.manage().timeouts().implicitlyWait(dr);


    }

    @AfterClass
   public void bitishIshlemleri() {

        Tools.Wait(4);
        driver.quit();

    }
}

