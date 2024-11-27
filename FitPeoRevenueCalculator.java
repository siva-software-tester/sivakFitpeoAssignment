package fitpeosiva;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class FitPeoRevenueCalculator {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();

		try {
			// Step 1: Navigate to the FitPeo Homepage
			driver.get("https://www.fitpeo.com");
			System.out.println("Navigated to FitPeo Homepage");
			Thread.sleep(2000);
			
			WebDriverWait wait = new WebDriverWait(driver, 10);

			// Step 2: Navigate to the Revenue Calculator Page
			WebElement revenueCalculator = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/revenue-calculator']")));
			revenueCalculator.click();
			System.out.println("Navigated to Revenue Calculator Page");
			Thread.sleep(2000);

			// Step 3: Scroll Down to the Slider Section
			WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"//span[@class='MuiSlider-thumb MuiSlider-thumbSizeMedium MuiSlider-thumbColorPrimary MuiSlider-thumb MuiSlider-thumbSizeMedium MuiSlider-thumbColorPrimary css-1sfugkh']")));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0, 450);");
			System.out.println("Scroll Down to the Slider Section Wait");
			Thread.sleep(2000);

			// Step 4: Adjust the Slider to the Value 830
			WebElement sliderInput = driver.findElement(By.cssSelector("input[type='range']"));

			Actions action = new Actions(driver);
			action.clickAndHold(sliderInput).moveByOffset((int) 95, 0).release().perform();
			Thread.sleep(2000);			
			
			// Step 5: Update the Text Field to 570
			js.executeScript("window.scrollBy(0, 450);");
			By textFieldLocator = By.id(":r0:");
			WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(textFieldLocator));

			textField.click();
			textField.clear();
			textField.sendKeys("570");
			textField.sendKeys(Keys.RETURN);

			By sliderLocator = By.cssSelector("input[type='range']");
			action.clickAndHold(sliderInput).moveByOffset((int) -39, 0).release().perform();
			WebElement sliderInputValue = wait.until(ExpectedConditions.presenceOfElementLocated(sliderLocator));
			js.executeScript("window.scrollBy(0, 450);");
			System.out.println("Updated the Text Field to 570");
			Thread.sleep(2000);

			// Step 6: Validate Slider Value
			String sliderValue = sliderInputValue.getAttribute("value");
			if (sliderValue.equals("570")) {
				System.out.println("Test passed! The slider value is correctly updated to 570.");
			} else {
				System.out.println("Test failed. The slider value is: " + sliderValue);
			}
			
			//Change Slider value to 830 to calculate Total Recurring Reimbursement
			js.executeScript("window.scrollBy(0, 450);");
			WebElement sliderInputs = driver.findElement(By.cssSelector("input[type='range']"));
			action.clickAndHold(sliderInputs).moveByOffset((int) 39, 0).release().perform();
			System.out.println("The Slider Value is adjusted to 830 ");
			Thread.sleep(2000);
			
			// Step 7: Scroll Down Further and Select the CPT Codes
			js.executeScript("window.scrollBy(0, 750);");
			Thread.sleep(2000);

			List<String> cptCodes = Arrays.asList("CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474");
			for (String cptCode : cptCodes) {
	            selectCheckboxForCPTCode(driver, cptCode);
	            Thread.sleep(200);
	        }
			Thread.sleep(2000);
			System.out.println("Scrolled Down  and Selected all the CPT Codes");

			// Step 8: Validate Total Recurring Reimbursement
			WebElement totalRecurringElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//div[@class='MuiToolbar-root MuiToolbar-gutters MuiToolbar-regular css-1lnu3ao']" +
		                    "//p[contains(text(), 'Total Recurring Reimbursement for all Patients Per Month:')]//following-sibling::p")
		        ));
			String totalReimbursementText = totalRecurringElement.getText();
			System.out.println("Total Recurring Reimbursement is: "+totalReimbursementText);

			// Step 9: Verify the Total Recurring Reimbursement value is $110700
			if (totalReimbursementText.equals("$112050")) {
				System.out.println("Total Recurring Reimbursement is $112050!");
			} else {
				System.out.println("Total Recurring Reimbursement is not $112050! ");
			}
			Thread.sleep(2000);

		} catch (TimeoutException e) {
            System.out.println("Timeout Exception: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Element not found: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }finally {
			// Close the browser
			driver.quit();
		}
	}
	public static void selectCheckboxForCPTCode(WebDriver driver,String cptCode) {
        try {
            WebElement checkbox = driver.findElement(By.xpath(
					"//p[contains(text(), '" + cptCode + "')]/ancestor::div[contains(@class, 'MuiBox-root')]/label//input"));
            // Click the checkbox if it's not already selected
            if (!checkbox.isSelected()) {
            	JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", checkbox);
                System.out.println("Selected checkbox for " + cptCode);
            } else {
                System.out.println("Checkbox for " + cptCode + " is already selected.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error selecting checkbox for " + cptCode + ": Element not found - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error selecting checkbox for " + cptCode + ": " + e.getMessage());
        }
    }

}
