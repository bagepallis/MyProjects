import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.asserts.SoftAssert;

public class LinkChecker {

	
	public String verifyTitle(String currentTitle) {
		String status = null;
		String matchTitle = "QA Automation Tools Trainings and Tutorials | QA Test Hub";
		if (currentTitle.equals(matchTitle)) {
			status = "PASS";
		} else {
			status = "FAIL";
		}
		
		return status;
		
	}
	
	public int randomNumGenerator(int listSize) {
		int randomNumber = (int) (Math.random() * listSize); 
		return randomNumber;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "/Users/user1/Desktop/Neetha-work/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.get("http://bnpassion.wordpress.com");
		

		// extract all the links on all the pages of the website
		boolean next_page = true;
		
		while (next_page) {
		    
			List<WebElement> links = driver.findElements(By.xpath("//article[contains(@id, 'post')]/header/h1/a"));
		    SoftAssert softAsrt = new SoftAssert();
		    
			for (WebElement link:links)
			{
				link.getAttribute("href");
				//checking the link if it is broken or not without clicking it
				
				HttpURLConnection conn = (HttpURLConnection)new URL(link.getAttribute("href")).openConnection();
				conn.setRequestMethod("HEAD");
				conn.connect();
				int responseCode = conn.getResponseCode();
				System.out.println(link.getText() + " - "+ responseCode + " - " + link.getAttribute("href") + "\n");
				softAsrt.assertTrue(responseCode < 400, "BROKEN LINK" + " " + link.getText() + " - "+ responseCode + " - " + link.getAttribute("href") + "\n");
				
			}
		
			softAsrt.assertAll(); // Need this -> to report all the failures caught
			    
			// generate a random number between 1 and 50, click on that title in the links
			String openLinkInNewTab = Keys.chord(Keys.COMMAND, Keys.ENTER);
			LinkChecker newAssign = new LinkChecker();
			
			if (links.size()>0) {
				int randomLinkNum = newAssign.randomNumGenerator(links.size()); 
				String linkToClick = links.get(randomLinkNum).getText();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			    driver.findElement(By.linkText(linkToClick)).sendKeys(openLinkInNewTab);
			    System.out.println("**random number: " + randomLinkNum + " - " + linkToClick +" link opened in new tab");
			}
			
			// load the next page link
			try { 
            	next_page = true;
				String next_page_url = driver.findElement(By.xpath("//div[@class='nav-previous']/a")).getAttribute("href");
				driver.get(next_page_url);
				
			} catch (NoSuchElementException e) {
				next_page = false;
			}
					
		}	
			
		
		driver.quit();
	}

}
