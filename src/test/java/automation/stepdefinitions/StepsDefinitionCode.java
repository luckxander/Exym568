package automation.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.HttpSessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Ordering;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Date;


@SpringBootTest
public class StepsDefinitionCode {
	
	public WebDriver dr = null;
	static String URL_login = "https://exym-vnext-teamtest2.azurewebsites.net/";
	static HttpSessionId stored = null;

	
	 //Browser SetUp and Login
	 ////////////////////////////////////////////////////////////////////////////////////////////////////
	 
    @Before
    public void setUp() throws Throwable { 
    	   System.out.println("Running setUp ...");
		   System.setProperty("webdriver.chrome.driver","C:/Users/luckx/OneDrive/Documents/workspace-spring-tool-suite-4-4.13.1.RELEASE/exym425/chromedriver.exe");
	       
		   dr = new ChromeDriver();
	       dr.manage().window().maximize();
	       dr.get(URL_login); 
		   
	       if(stored == null) {
			   HttpSessionId session = new HttpSessionId();
			   TimeUnit.SECONDS.sleep(6);
			   WebDriverWait wait = new WebDriverWait(dr, 10);
			   wait.until(ExpectedConditions.textToBePresentInElement(dr.findElement(By.xpath("//body[@id='kt_body']/app-root/ng-component/div/div/div/div/div/tenant-change/span")),"Current tenant"));

			   String currentText = dr.findElement(By.cssSelector("#kt_body > app-root > ng-component > div > div > div.d-flex.flex-center.flex-column.flex-column-fluid.p-10.pb-lg-20 > div > div > tenant-change > span")).getText();
			   //System.out.println("Current text => "+currentText);
			   if(currentText.intern() == "Current tenant: Not selected ( Change )") {
				  
				  //switch to default 
				  dr.findElement(By.linkText("Change")).click();
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.xpath("//*[@id=\"kt_body\"]/app-root/ng-component/div/div/div[1]/div/div/tenant-change/span/tenantchangemodal/div/div/div/form/div[2]/div[1]/div/label/input")).click();
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.cssSelector("#tenancyNameInput")).sendKeys("default");
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.xpath("//*[@id=\"kt_body\"]/app-root/ng-component/div/div/div[1]/div/div/tenant-change/span/tenantchangemodal/div/div/div/form/div[3]/button[2]")).click();
			   
				  //click openIDconnect
				  TimeUnit.SECONDS.sleep(6);
				  dr.findElement(By.linkText("OpenIdConnect")).click();
				  
				  //user credentials 
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.cssSelector("#signInName")).sendKeys("BettyWhite@mailinator.com");
				  dr.findElement(By.cssSelector("#password")).sendKeys("Password22!");
				  dr.findElement(By.cssSelector("#next")).click();
				  				  
			   }
			   
			   stored = session;
			   TimeUnit.SECONDS.sleep(3);
			}
    }
    
    /////////////////////////////////////////////////////////////////////////////
	//End Browser SetUp and Login

	
	@Given("I am a clinician user")
	public void i_am_a_clinician_user() throws Throwable {
		assertNotEquals(stored,null);
	}

	@When ("I go to the main page Exym vNext portal")
	public void i_go_to_the_main_page_Exym_vNext_portal() throws Throwable {
		TimeUnit.SECONDS.sleep(6);
		String expected_notes = dr.findElement(By.xpath("//*[@id=\"kt_wrapper\"]/div[2]/app-landing-dashboard/div/div/sub-header/div/div/div[1]/h5")).getText();
	    String current_notes ="Dashboard";
	    assertEquals(current_notes,expected_notes);

	}

	@And ("I click in the sort arrow located to the right of the ‘LAST SERVICE’ text in the ‘Client’s table")
	public void sort_last_service() throws Throwable {
		TimeUnit.SECONDS.sleep(3);
		dr.findElement(By.xpath("//*[@id=\"pr_id_5\"]/div/table/thead/tr/th[3]/p-sorticon")).click();
		
	}
	
	@Then ("I should see the services sorted by the most recent service date")
	public void recent_service_date() throws Throwable {
		List<WebElement> rows = dr.findElements(By.xpath("//*[@id=\"pr_id_5\"]/div/table/tbody/tr"));
		int rows_number = rows.size();
		
		String dateArray[] = new String[rows_number];
		for (int i=1; i<=rows_number; i++){
		    dateArray[i-1] = dr.findElement(By.xpath("//*[@id=\"pr_id_5\"]/div/table/tbody/tr["+i+"]/td[3]")).getText();
		}
		
		for (int i = 0; i < dateArray.length-1; i++) {
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");		
			java.util.Date cal1 = format.parse(dateArray[i]);
			java.util.Date cal2 = format.parse(dateArray[i+1]);
			
			//System.out.println(cal1.getClass().getSimpleName());
			int result = cal1.compareTo(cal2);
			if (result <= 0)  {
			   break;
			}				
		}
	}
	
	@And ("I click in the sort arrow located to the right of the ‘SCHEDULED’ text in the ‘My Notes’ table")
	public void sort_my_notes() throws Throwable {
		dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/thead/tr/th[4]/p-sorticon")).click();
	}
	
	@And ("I should see the notes sorted by scheduled date")
	public void sorted_notes() throws Throwable {
		
		List<WebElement> rows = dr.findElements(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr"));
		int rows_number = rows.size();
		
		String dateArray[] = new String[rows_number];
		for (int i=1; i<=dateArray.length; i++){
			dateArray[i-1] = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr["+i+"]/td[4]")).getText();	
		    //System.out.println("Scheduled => "+dateArray[i-1]);
		}
		
		for (int i = 0; i < dateArray.length-1; i++) {
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");		
			java.util.Date cal1 = format.parse(dateArray[i]);
			java.util.Date cal2 = format.parse(dateArray[i+1]);
			
			//System.out.println(cal1.getClass().getSimpleName());
			int result = cal1.compareTo(cal2);
			if (result <= 0)  {
			   break;
			}				
		}
	}
	
    @After //tearDown() (close browser)
    public void tearDown() throws Exception {
    	 TimeUnit.SECONDS.sleep(3);
        System.out.println("Running tearDown ...");
        dr.close();
    }
}
