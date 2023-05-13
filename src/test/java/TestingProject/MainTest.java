package TestingProject;

import Utility.BaseDriverParameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class MainTest extends BaseDriverParameter {



    @Test(priority = 1)
    @Parameters({"Firstname","LastName","Email","Password"})
    void  RegistrationsTest(String fName,String lName,String Email, String Password){

        driver.get("https://demo.nopcommerce.com/");

        WebElement register = driver.findElement(By.linkText("Register"));
        register.click();

        WebElement gender = driver.findElement(By.id("gender-male"));
        gender.click();

        WebElement firstname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FirstName")));
        firstname.sendKeys(fName);

        WebElement lastname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("LastName")));
        lastname.sendKeys(lName);

        Select drMenu = new Select(driver.findElement(By.name("DateOfBirthDay")));
        drMenu.selectByValue("10");

        Select drMenu2 = new Select(driver.findElement(By.name("DateOfBirthMonth")));
        drMenu2.selectByVisibleText("May");

        Select drMenu3 = new Select(driver.findElement(By.name("DateOfBirthYear")));
        drMenu3.selectByVisibleText("1998");

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys(Email);

        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys(Password);

        WebElement confirmPassword = driver.findElement(By.id("ConfirmPassword"));
        confirmPassword.sendKeys(Password);

        WebElement registerButton = driver.findElement(By.id("register-button"));
        registerButton.click();

        WebElement text = driver.findElement(By.xpath("//div[text()='Your registration completed']"));

        Assert.assertEquals(text.getText(),"Your registration completed", "Test failed");

    }

    @Test(priority = 2, dependsOnMethods = "RegistrationsTest")
    @Parameters({"Email", "Password"})

    void LoginTest(String Email, String Password){
        driver.get("https://demo.nopcommerce.com/");

                WebElement login= driver.findElement(By.linkText("Log in"));
        login.click();

        WebElement email= driver.findElement(By.id("Email"));
        email.sendKeys(Email);

        WebElement password= driver.findElement(By.id("Password"));
        password.sendKeys(Password);

        WebElement loginBtn= driver.findElement(By.className("login-button"));
        loginBtn.click();

        WebElement logConfirm= wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log out")));
        Assert.assertEquals(logConfirm.getText(), "Log out" , "Test failed");

        WebElement logOut = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out")));
        logOut.click();

    }

    @Test(dataProvider = "emails",priority = 3)
    void DataProviderLogin(String Email, String Password) {
        driver.get("https://demo.nopcommerce.com/");

        WebElement login = driver.findElement(By.className("ico-login"));
        login.click();

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys(Email);

        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys(Password);

        WebElement loginBtn = driver.findElement(By.className("login-button"));
        loginBtn.click();

        List<WebElement> classes = driver.findElements(By.cssSelector("div[class]"));

        for (WebElement e  :classes) {

            if (e.getText().equalsIgnoreCase("Login was unsuccessful. Please correct the errors and try again."))
            {
                WebElement failedMsg = driver.findElement(By.className("message-error validation-summary-errors"));
                Assert.assertTrue(failedMsg.getText().contains("Login was unsuccessful"), "Test Failed");
                break;
            }
        }

        List<WebElement> hrefs=driver.findElements(By.cssSelector("a[href]"));

        for (WebElement e  :hrefs) {

            if (e.getText().contains("Log out")){
                Assert.assertEquals(e.getText(),"Log out", "Test Failed");
                break;
            }
        }

    }

    @DataProvider
     Object[][] emails(){
        Object[][] data={
                {"Memmed125@gmail.com", "51516565"},
                {"rauf376@gmail.com", "123456"},
        };
        return data;
    }




    @Test(priority = 4, dependsOnMethods = "LoginTest")
    void TabMenuTest()
    {
        driver.get("https://demo.nopcommerce.com/");

        List<String> listIsimler = new ArrayList<>();
        listIsimler.add("Computers");
        listIsimler.add("Electronics");
        listIsimler.add("Apparel");
        listIsimler.add("Digital downloads");
        listIsimler.add("Books");
        listIsimler.add("Jewelry");
        listIsimler.add("Gift Cards");

        List<WebElement> tabMenu = driver.findElements(By.cssSelector("[class='top-menu notmobile']>li>a"));

        for (WebElement e  :tabMenu) {
            System.out.println(e.getText().trim());
            Assert.assertTrue(listIsimler.contains(e.getText().trim()), "Test failed");
        }

    }



   @Test(priority = 5,dependsOnMethods = "TabMenuTest")
   void OrderGiftsTest()
   {

       driver.get("https://demo.nopcommerce.com/");
       WebElement giftCard = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Gift Cards")));
       giftCard.click();

       List<WebElement> gifts = driver.findElements(By.xpath("//a[contains(text(),'Physical')]"));

       gifts.get((int)(Math.random()*2)).click();

       WebElement receiptName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='recipient-name']")));
       receiptName.sendKeys("Ahmet");

       WebElement SenderName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='sender-name']")));
       SenderName.sendKeys("Ahmet");

       WebElement message = driver.findElement(By.xpath("//textarea[@class='message']"));
       message.sendKeys("Hello");

       WebElement addToCard = driver.findElement(By.xpath("//button[contains(@id,'add-to-cart-button')]"));
       addToCard.click();

       WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//p[text()='The product has been added to your ']"))));

       Assert.assertTrue(success.getText().contains("The product has been added to your"),"Test Failed");

   }

    @Test(priority = 6,dependsOnMethods = "OrderGiftsTest")
    void orderComputerTest(){
        driver.get("https://demo.nopcommerce.com/");

                Actions actions=new Actions(driver);

        WebElement computer= driver.findElement(By.xpath("//a[text()='Computers ']"));
        actions.moveToElement(computer).build().perform();

        WebElement desktop= driver.findElement(By.xpath("//a[text()='Desktops ']"));
        actions.moveToElement(desktop).click().build().perform();

        WebElement build= driver.findElement(By.linkText("Build your own computer"));
        build.click();

        WebElement ram=driver.findElement(By.id("product_attribute_2"));
        Select selectRam=new Select( ram);

        while (true) {
            int random = (int) (Math.random() * 3);
            if (random!=0) {
                selectRam.selectByIndex(random);
                break;
            }

        }

        List<WebElement> hdd=driver.findElements(By.xpath("//input[@type='radio' and contains(@id, 'product_attribute_3')]"));
        hdd.get((int)(Math.random()*2)).click();

        WebElement addCart= driver.findElement(By.id("add-to-cart-button-1"));
        addCart.click();

        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//p[text()='The product has been added to your ']"))));
        Assert.assertTrue(success.getText().contains("The product has been added to your"),"Test Failed");
    }

    @Test(priority = 7,dependsOnMethods = "orderComputerTest")
    @Parameters("itemName")
    void ParametreliSeacrhText(String name)
    {
        driver.get("https://demo.nopcommerce.com/");

        WebElement searchBox = driver.findElement(By.cssSelector("[class='search-box-text ui-autocomplete-input']"));
        searchBox.sendKeys(name);

        WebElement searchButton = driver.findElement(By.cssSelector("[class='button-1 search-box-button']"));
        searchButton.click();

        WebElement urun = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Adobe Photoshop CS4")));

        Assert.assertTrue(urun.getText().contains(name),"test failed");

    }
}
