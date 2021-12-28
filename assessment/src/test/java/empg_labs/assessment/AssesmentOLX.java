package empg_labs.assessment;

import org.testng.annotations.Test;


import org.testng.annotations.BeforeTest;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

public class AssesmentOLX {
	WebDriver driver;
	String url = "https://www.olx.com.pk/mall/";

	@BeforeTest
	public void goToUrl() {

		System.setProperty("webdriver.chrome.driver", "E:\\eclipse-workspace\\assessment\\chromedriver96.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
	}

	@Test(priority = 0)
	public void verifyURL() {
		Assert.assertEquals(driver.getCurrentUrl(), url);
	}

	@Test(priority = 1)
	public void navigateToMobileCategory() throws InterruptedException {
		Actions action = new Actions(driver);
		WebElement allCategories = driver.findElement(By.xpath("//button[@class='button_btn__r8N5j button_link__xvDOA']"));
		allCategories.click();
		Thread.sleep(2000);
		WebElement MobAndTab = driver.findElement(By.xpath("//li//span[text()='Mobiles & Tablets']"));
		action.moveToElement(MobAndTab).perform();

		WebElement mobile = driver.findElement(By.xpath("//li//span[text()='Mobiles']"));
		mobile.click();
		Thread.sleep(8000);	  
	}

	@Test(priority = 2)
	public void filterListingsByPrice() throws InterruptedException {

		WebElement minPriceFilter= driver.findElement(By.id("minPrice"));
		minPriceFilter.clear();
		minPriceFilter.sendKeys("40000");
		WebElement maxPriceFilter = driver.findElement(By.id("maxPrice"));
		maxPriceFilter.clear();
		maxPriceFilter.sendKeys("120000");
		Thread.sleep(3000);
	}

	@Test(priority = 3)
	public void validateResultsInPriceRange() {
		//This will store top 10 results in Web Element List
		List<WebElement> mobileResults = driver.findElements(By.xpath("(//div[@class='card_prices__1EWoa']/span)[position()<=10]"));
		ArrayList<String> mobilePriceStr = new ArrayList<String>();
		//Text of the List of Web Elements will store in String array.
		for(WebElement result : mobileResults){
			mobilePriceStr.add(result.getText().substring(2));
		}

		//Min price range from range filter will convert in integer and will store in int variable
		int minPriceRange = Integer.parseInt(driver.findElement(By.id("minPrice")).getAttribute("value").replace(",",""));

		//Max price range from range filter will convert in integer and will store in int variable
		int maxPriceRange = Integer.parseInt(driver.findElement(By.id("maxPrice")).getAttribute("value").replace(",",""));

		for(int i=0 ; i<mobilePriceStr.size(); i++) {
			//String Mob prices will covert in integer type and will store in integer variable one by one
			int mobPrice = Integer.parseInt(mobilePriceStr.get(i).replace(",",""));
			if(mobPrice >= minPriceRange && mobPrice <= maxPriceRange) {
				Assert.assertTrue(true);
			}
			else {
				System.out.println("Wrong prices");
				Assert.assertTrue(false);
			}
		}
	}

	@Test(priority = 4)
	public void validateResultsAreFromMobileCategory() throws InterruptedException {
		driver.findElement(By.className("card_productCard__3fd_M")).click();
		Thread.sleep(5000);
		ArrayList<String> breadCrumbList = new ArrayList<String>();
		List<WebElement> breadCrumbs = driver.findElements(By.xpath("(//a[@class='breadcrumb-item'])[position()<=3]"));
		for(WebElement breadCrumb : breadCrumbs ) {
			breadCrumbList.add(breadCrumb.getText());  
		}

		ArrayList<String> actual_list = new ArrayList<String>();
		actual_list.add("Home");
		actual_list.add("Mobiles & Tablets");
		actual_list.add("Mobiles");

		//Asserting the original Arraylist matches to the Arraylist with retrieved Link Texts 
		System.out.println(breadCrumbList +" "+actual_list);
		if(breadCrumbList.equals(actual_list)) {
			Assert.assertTrue(true);
		}
		else {
			System.out.println("Not in a correct Category.");
			Assert.assertTrue(false);
		}

	}

	@AfterTest
	public void afterTest() {
		driver.quit();
	}

}
