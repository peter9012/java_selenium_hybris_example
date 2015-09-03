package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;


public class RFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(RFWebsiteBasePage.class.getName());

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	private final By WELCOME_DD_EDIT_CRP_LINK_LOC = By.xpath("//div[@id='account-info']//a[contains(text(),'Edit')]");
	protected RFWebsiteDriver driver;
	public RFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	//contains the common methods useful for all the pages inherited

	public static String convertDBDateFormatToUIFormat(String DBDate){
		String UIMonth=null;
		String[] splittedDate = DBDate.split(" ");
		String date = (splittedDate[0].split("-")[2].charAt(0))=='0'?splittedDate[0].split("-")[2].split("0")[1]:splittedDate[0].split("-")[2];
		String month = (splittedDate[0].split("-")[1].charAt(0))=='0'?splittedDate[0].split("-")[1].split("0")[1]:splittedDate[0].split("-")[1];		
		String year = splittedDate[0].split("-")[0];		
		switch (Integer.parseInt(month)) {		
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;		
		}

		return UIMonth+" "+date+", "+year;
	}

	public void clickOnShopLink(){
		driver.waitForElementPresent(By.id("our-products"));
		driver.click(By.id("our-products"));
		logger.info("Shop link clicked ");
	}

	public void clickOnAllProductsLink(){
		try{
			//driver.waitForElementPresent(By.xpath("//a[@title='All Products']"));
			driver.click(By.xpath("//a[@title='All Products']"));
		}catch(NoSuchElementException e){
			logger.info("All products link was not present");
			driver.click(By.xpath("//div[@id='dropdown-menu']//a[@href='/us/quick-shop/quickShop']"));
		}
		logger.info("All products link clicked "+"//a[@title='All Products']");	
		driver.waitForPageLoad();
	}

	public StoreFrontUpdateCartPage clickOnQuickShopImage(){
		driver.waitForElementToBeVisible(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"), 30);
		driver.waitForElementPresent(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		driver.click(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		logger.info("Quick shop Img link clicked "+"//a[@href='/us/quick-shop/quickShop' and @title='']");
		driver.waitForPageLoad();
		return new StoreFrontUpdateCartPage(driver);
	}

	public boolean areProductsDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	}

	public void selectProductAndProceedToBuy() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		logger.info("Buy Now button clicked");
		driver.waitForPageLoad();
	}

	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		logger.info("Add To CRP button clicked for first product");
		driver.waitForLoadingImageToDisappear();
	}

	public void addQuantityOfProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.id("quantity0"));
		driver.findElement(By.id("quantity0")).clear();
		driver.findElement(By.id("quantity0")).sendKeys(qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//a[@class='updateLink']"));
		logger.info("Update button clicked after adding quantity");
		driver.pauseExecutionFor(3000);
	}

	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitForm"));
		driver.click(By.id("submitForm"));
		logger.info("Next button after adding quantity clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isCartPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	}

	public void addAnotherProduct() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//p[@class='floated-right']//a[contains(text(),'Continue shopping')]"));
		driver.click(By.xpath("//p[@class='floated-right']//a[contains(text(),'Continue shopping')]"));
		logger.info("Continue shopping link clicked");
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		logger.info("Buy Now button clicked and another product selected");
		driver.waitForPageLoad();
	}

	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='checkout']"));
		driver.click(By.xpath("//input[@value='checkout']"));
		logger.info("checkout button clicked");
		driver.waitForPageLoad();
	}

	public boolean isLoginOrCreateAccountPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='content-full-page']/h1[text()='Log in or create an account']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='content-full-page']/h1[text()='Log in or create an account']")));
	}

	public void enterNewRCDetails() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="RCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.id("the-password-again")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("confirm password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.click(By.id("next-button"));		
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void enterNewPCDetails() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="PCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.id("the-password-again")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("confirm password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));		
		logger.info("Create New Account button clicked");		
	}

	public boolean isPopUpForPCThresholdPresent() throws InterruptedException{
		boolean isPopUpForPCThresholdPresent=false;
		driver.waitForElementPresent(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products in your PC cart greater than the threshold CAD $90')]"));
		isPopUpForPCThresholdPresent = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products in your PC cart greater than the threshold CAD $90')]")));
		if(isPopUpForPCThresholdPresent==true){
			driver.click(By.xpath("//div[@id='popup-content']//input"));
			return true;
		}
		return false;
	}

	public boolean isCheckoutPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Checkout')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Checkout')]")));
	}

	public void enterMainAccountInfo(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.ADDRESS_LINE_1_CA);
			logger.info("Address Line 1 entered is "+TestConstants.ADDRESS_LINE_1_CA);
			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.CITY_CA);
			logger.info("City entered is "+TestConstants.CITY_CA);
			driver.click(By.id("state"));
			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
			driver.click(By.xpath("//select[@id='state']/option[2]"));
			logger.info("state selected");
			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.POSTAL_CODE_CA);
			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_CA);
			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.PHONE_NUMBER);
			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER);
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.NEW_ADDRESS_LINE1_US);
			logger.info("Address line 1 entered is "+TestConstants.NEW_ADDRESS_LINE1_US);
			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.NEW_ADDRESS_CITY_US);
			driver.click(By.id("state"));
			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
			driver.click(By.xpath("//select[@id='state']/option[2]"));
			logger.info("state selected");
			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
			logger.info("postal code entered is "+TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
			logger.info("phone number entered is "+TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		}

	}

	public void enterMainAccountInfo(String address1,String city,String province,String postalCode,String phoneNumber){
		driver.findElement(By.id("address.line1")).sendKeys(address1);
		logger.info("Address Line 1 entered is "+address1);
		driver.findElement(By.id("address.townCity")).sendKeys(city);
		logger.info("City entered is "+city);
		driver.click(By.id("state"));
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
		driver.findElement(By.id("address.postcode")).sendKeys(postalCode);
		logger.info("postal code entered is "+postalCode);
		driver.findElement(By.id("address.phonenumber")).sendKeys(phoneNumber);
		logger.info("phone number entered is "+phoneNumber);
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		driver.waitForElementPresent(By.id("continue-no-sponsor"));
		driver.click(By.id("continue-no-sponsor"));	
		logger.info("continue without sponsor link clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveAccountAddress"));
		driver.click(By.id("saveAccountAddress"));
		logger.info("Next button after selecting sponsor is clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}		
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("saveShippingInfo"));
		action.moveToElement(driver.findElement(By.id("saveShippingInfo"))).click(driver.findElement(By.id("saveShippingInfo"))).build().perform();
		logger.info("Next button on shipping address clicked");		
		driver.waitForLoadingImageToDisappear();
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.type(By.id("card-nr"), cardNumber);
		logger.info("Billing card number entered is "+cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.id("card-name"));
		driver.findElement(By.id("card-name")).clear();
		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
		logger.info("card name entered is "+nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
		logger.info("expiration date is selected");
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		logger.info("New Billing card address selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save billing profile button clicked");
	}



	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		logger.info("Next button on billing profile clicked");	
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@class='positive right pad_right ']"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[3]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[4]/div"));
		driver.click(By.xpath("//input[@class='positive right pad_right ']"));
		logger.info("Next button on billing profile clicked");

	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.click(By.id("placeOrderButton"));
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();		
	}

	public void clickOnRodanAndFieldsLogo(){
		driver.waitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		logger.info("Rodan and Fields logo clicked");	
		driver.waitForPageLoad();
	}

	public boolean isOrderPlacedSuccessfully(){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']/h1[text()='Thank you for your order']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='confirm-left-shopping']/h1[text()='Thank you for your order']")));
	}

	public boolean verifyWelcomeDropdownToCheckUserRegistered(){		
		driver.waitForElementPresent(By.xpath("//li[@id='account-info-button']/a"));
		return driver.findElement(By.xpath("//li[@id='account-info-button']/a")).getText().contains("Welcome");
	}

	public void applyPriceFilterLowToHigh() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]"));
		driver.click(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]"));
		driver.click(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]/following::li[1]/form/div"));
		logger.info("filter done for low to high price");
	}

	public boolean verifyPCPerksTermsAndConditionsPopup() throws InterruptedException{
		boolean isPCPerksTermsAndConditionsPopup = false;
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		isPCPerksTermsAndConditionsPopup = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")));
		if(isPCPerksTermsAndConditionsPopup==true){
			driver.click(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
			return true;
		}
		return false;
	}

	public void clickOnPCPerksTermsAndConditionsCheckBoxes(){
		driver.waitForElementToBeClickable(By.xpath("//input[@id='Terms2']/.."), 15);
		driver.pauseExecutionFor(1000);
		driver.click(By.xpath("//input[@id='Terms2']/.."));
		driver.click(By.xpath("//input[@id='Terms3']/.."));
	}

	public boolean selectNewBillingCardExpirationDateAsExpiredDate()	 {
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='01']"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[@value='01']"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[1]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[1]"));
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='01']"));
		return driver.findElement(By.xpath("//select[@id='expiryMonth']/option[@value='01']")).isEnabled();
	}


	public boolean validatePasswordFieldMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter 6')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}
	public void clearPasswordField(){
		driver.findElement(By.id("new-password-account")).clear();
		logger.info("password field cleared");
	}

	public boolean recurringMonthlyChargesSection() {
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]"));
		return driver.findElement(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]")).isDisplayed();
	}

	public boolean pulseSubscriptionTextbox() {
		driver.waitForElementPresent(By.id("webSitePrefix"));
		return driver.findElement(By.id("webSitePrefix")).isEnabled();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckbox() {
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		boolean status=driver.findElement(By.xpath("//input[@id='spouse-check']/..")).isSelected();
		if(status==false){
			driver.click(By.xpath("//input[@id='spouse-check']/.."));
		}
	}

	public void enterSpouseFirstName(String firstName){
		driver.waitForElementPresent(By.id("spouse-first"));
		driver.findElement(By.id("spouse-first")).sendKeys(firstName);
		logger.info("Spouse first name entered as "+firstName);
	}

	public void enterSpouseLastName(String firstName){
		driver.waitForElementPresent(By.id("spouse-last"));
		driver.findElement(By.id("spouse-last")).sendKeys(firstName);
		logger.info("Spouse last name entered as "+firstName);
	}

	public void clickOnRequestASponsorBtn(){
		driver.waitForElementPresent(By.xpath("//input[@value='Request a sponsor']"));
		driver.click(By.xpath("//input[@value='Request a sponsor']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOKOnSponsorInformationPopup(){
		//   driver.waitForElementToBeVisible(By.xpath("//div[@id='sponsorMessage']//div[@id='popup-sponsorMessage']//input[contains(@value,'OK')]"), 15);
		driver.waitForElementPresent(By.xpath("//div[@id='sponsorMessage']//div[@id='popup-sponsorMessage']//input[contains(@value,'OK')]"));
		driver.click(By.xpath("//div[@id='sponsorMessage']//div[@id='popup-sponsorMessage']//input[contains(@value,'OK')]"));
	}

	public void clickYesIWantToJoinPCPerksCB(){
		driver.waitForElementPresent(By.id("pc-customer2-div-order-summary"));
		driver.click(By.id("pc-customer2-div-order-summary"));
	}

	public void checkIAcknowledgePCAccountCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='Terms2']/.."));
		driver.click(By.xpath("//input[@id='Terms2']/.."));  
		logger.info("I Acknowledge PC Account Checkbox selected");
	}

	public void checkPCPerksTermsAndConditionsCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='Terms3']/.."));
		driver.click(By.xpath("//input[@id='Terms3']/.."));  
		logger.info("PC Perks terms and condition checkbox selected");
	}

	public boolean isWelcomePCPerksMessageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]")));
	}

	public String createNewPC() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="PCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.id("the-password-again")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("confirm password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));  
		logger.info("Create New Account button clicked"); 
		return emailAddress;
	}

	public String createNewRC(){	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="RCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.id("the-password-again")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		logger.info("confirm password entered as "+TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.click(By.id("next-button"));  
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return emailAddress;
	}

	public StoreFrontCartAutoShipPage clickEditCrpLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
		driver.click(WELCOME_DD_EDIT_CRP_LINK_LOC);
		logger.info("User has clicked on edit Crp link from welcome drop down");
		driver.waitForPageLoad();
		return new StoreFrontCartAutoShipPage(driver);
	}

	public boolean validateExistingUserPopUp(String userid){
		String firstName="RCUser";
		String lastName = "Test";
		String pcmailid="autopc@xyz.com";
		String rcmailid="Retail29@mailinator.com";
		String consultantmailid="con0708@yopmail.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		if(userid.equalsIgnoreCase("pc")){
			driver.findElement(By.id("email-account")).sendKeys(pcmailid);
			logger.info("email entered as "+pcmailid);
		}else if(userid.equalsIgnoreCase("rc")){
			driver.findElement(By.id("email-account")).sendKeys(rcmailid);
			logger.info("email entered as "+rcmailid);
		}else{
			driver.findElement(By.id("email-account")).sendKeys(consultantmailid);
			logger.info("email entered as "+consultantmailid);
		}
		driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		boolean flag=false;
		if(driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='fancybox-inner']//h2[contains(text(),'E')]")))){
			return !flag;
		}else{
			driver.navigate().refresh();
			driver.waitForPageLoad();
			if(userid.equalsIgnoreCase("pc")){
				driver.findElement(By.id("email-account")).sendKeys(pcmailid);
				logger.info("email entered as "+pcmailid);
			}else if(userid.equalsIgnoreCase("rc")){
				driver.findElement(By.id("email-account")).sendKeys(rcmailid);
				logger.info("email entered as "+rcmailid);
			}else{
				driver.findElement(By.id("email-account")).sendKeys(consultantmailid);
				logger.info("email entered as "+consultantmailid);
			}
			driver.findElement(By.id("password")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
			driver.waitForLoadingImageToDisappear();
			driver.pauseExecutionFor(2000);
			return driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='fancybox-inner']//h2[contains(text(),'E')]")));
		}
	}

	public boolean validateSendMailToResetMyPasswordFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[@class='resetPasswordEmail']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='activePCPopup']//input[@class='resetPasswordEmail']")));
	}
	public boolean validateCancelEnrollmentFunctionalityPC(){
		String baseurl="http://www.corprfo.stg2.rodanandfields.com/ca/";
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//div[@id='activePCPopup']//input[@class='cancelEnrollment']"));
		driver.waitForPageLoad();
		return driver.getCurrentUrl().equalsIgnoreCase(baseurl);
	}

	public boolean validateSendMailToResetMyPasswordFunctionalityRC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activeRetailPopup']//input[@class='resetPasswordEmail']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='activeRetailPopup']//input[@class='resetPasswordEmail']")));
	}

	public boolean validateCancelEnrollmentFunctionalityRC(){
		String baseurl="http://www.corprfo.stg2.rodanandfields.com/ca/";
		driver.waitForElementPresent(By.xpath("//div[@id='activeRetailPopup']//input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//div[@id='activeRetailPopup']//input[@class='cancelEnrollment']"));
		driver.waitForPageLoad();
		return driver.getCurrentUrl().equalsIgnoreCase(baseurl);
	}

	public boolean validateSendMailToResetMyPasswordFunctionalityConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")));
	}

	public boolean validateCancelEnrollmentFunctionalityConsultant(){
		String baseurl="http://www.corprfo.stg2.rodanandfields.com/ca/";
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//div[@id='notavailablePopup']//input[@class='cancelEnrollment']"));
		driver.waitForPageLoad();
		return driver.getCurrentUrl().equalsIgnoreCase(baseurl);
	}
}