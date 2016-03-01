package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;


public class CSCockpitCustomerTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCustomerTabPage.class.getName());

	private static String orderTypeLoc = "//div[contains(text(),'Order Number')]/following::a[text()='%s']/following::span[2]";
	private static String orderSectionLoc ="//div[text()='%s']";
	private static String orderTypeCustomerTabLoc = "//div[@class='z-listbox-body']//a[contains(text(),'%s')]//following::td[2]//span";
	private static String orderDetailsLoc = "//span[contains(text(),'%s')]";
	private static String customerTypeLoc = "//span[contains(text(),'Customer Type')]/following::span[text()='%s']";
	private static String autoshipTemplateDetailsLoc = "//span[contains(text(),'Autoship Templates')]/following::div[contains(text(),'%s')]";
	private static String orderNumberLoc = "//div[@class='csSearchResults']/descendant::div[@class='z-listbox-body']//tbody[2]/tr[2]/td[1]//a[contains(text(),'%s')]";
	private static String autoshipIdStatusLoc = "//span[text()='Autoship Templates']/following::div[1]//div/a[text()='%s']/following::span[contains(text(),'pcAutoship')]/following::span[1]";
	 private static String autoshipNumberWhoseAutoshipIsCancelledLoc = "//span[text()='Autoship Templates']/following::span[contains(text(),'Cancelled')]/preceding::a[text()='%s'][1]";
	
	private static final By PLACE_ORDER_BUTTON = By.xpath("//td[contains(text(),'PLACE AN ORDER')]");	
	private static final By ORDER_NUMBER_IN_CUSTOMER_ORDER = By.xpath("//span[contains(text(),'Customer Orders')]/following::div[contains(text(),'Order Number')][1]/following::a[1]");
	private static final By FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION = By.xpath("//div[@class='csSearchResults']/descendant::div[@class='z-listbox-body']//tbody[2]/tr[2]/td[1]//a");
	private static final By ORDER_NUMBER_CUSTOMER_TAB_LOC = By.xpath("//div[@class='customerOrderHistoryWidget']//tr[2]//a");
	private static final By ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'Account Status:')]/following::span[1]");
	private static final By ADD_CARD_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::td[text()='ADD CARD']");
	private static final By CREDIT_CARD_EDIT_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/tr[1]//td[text()='EDIT']");
	private static final By ADD_NEW_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'ADD NEW PAYMENT PROFILE')]");
	private static final By EDIT_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]");
	private static final By SHIPPING_ADDRESS_EDIT_BUTTON = By.xpath("//span[text()='Customer Addresses']/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/tr[1]//td[text()='Edit']");
	private static final By EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]");
	private static final By CLOSE_POPUP_OF_EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]/div[contains(@id,'close')]");
	private static final By ADD_NEW_SHIPPING_ADDRESS = By.xpath("//span[contains(text(),'Customer Address')]/following::td[text()='Add']");
	private static final By CREATE_NEW_ADDRESS = By.xpath("//div[contains(text(),'Create New Address')]");
	private static final By AUTOSHIP_ID_FIRST = By.xpath("//span[text()='Autoship Templates']/following::div[contains(@class,'listbox-body')][1]//tr[2]//a");
	private static final By AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'crpAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'pcAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Autoship Templates']");
	private static final By CUSTOMER_ORDER_SECTION = By.xpath("//span[text()='Customer Orders']");
	private static final By CUSTOMER_BILLING_INFO = By.xpath("//span[text()='Billing Information']");
	private static final By CUSTOMER_ADDRESS = By.xpath("//span[text()='Customer Addresses']");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/../../preceding-sibling::td//a");
	private static final By CREATE_PULSE_TEMPLATE_BTN = By.xpath("//td[contains(text(),'Create Pulse Template')]");
	private static final By CREATE_PULSE_TEMPLATE_BTN_ON_POPUP = By.xpath("//div[contains(text(),'Add PWS Prefix')]/following::td[contains(text(),'Create Pulse Template')]");
	private static final By NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC = By.xpath("//span[contains(text(),'Autoship Templates')]/following::div[@class='csWidgetContent'][1]//div[@class='z-listbox-body']//tbody[2]//tr[2]/td[7]//span");
	private static final By PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate'][1]/../../preceding-sibling::td//a");
	private static final By SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT = By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]/ancestor::td[contains(@style,'display:none;')]");
	private static final By SHIPPING_PROFILE_ERROR_POPUP_OK_BTN = By.xpath("//div[@class='z-window-modal']//td[text()='OK']");
	private static final By USE_THIS_ADDRESS = By.xpath("//td[contains(text(),'Use this Address')]");
	private static final By SHIPPING_ADDRESS_PROFILE_FIRST_NAME = By.xpath("//span[text()='Customer Addresses']/following::div[@class='z-listbox-body']//tbody[2]//tr[1]//td[1]/div");
	private static final By SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX= By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]/preceding::span[@class='z-checkbox'][1]/input");
	private static final By YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP = By.xpath("//td[text()='Yes']");
	private static final By CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP = By.xpath("//td[contains(text(),'Create new address')]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='crpAutoship']/../../..//span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP = By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='pcAutoship']/following::span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By CLOSE_POPUP_OF_CREATE_NEW_ADDRESS = By.xpath("//div[contains(text(),'Create New Address')]/div[contains(@id,'close')]");
	private static final By ADDRESS_CAN_NOT_BE_ADDED_POPUP = By.xpath("//span[contains(text(),'Address Cannot be added for Inactive user')]");
	private static final By OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP = By.xpath("//td[text()='OK']");
	private static final By ATTENDENT_NAME_TEXT_BOX = By.xpath("//span[text()='Attention']/following::input[1]");
	private static final By CITY_TOWN_TEXT_BOX = By.xpath("//span[text()='City/Town']/following::input[1]");
	private static final By POSTAL_TEXT_BOX = By.xpath("//span[text()='Postal Code']/following::input[1]");
	private static final By COUNTRY_TEXT_BOX = By.xpath("//span[text()='Country']/following::input[1]");
	private static final By PROVINCE_TEXT_BOX = By.xpath("//span[text()='State/Province']/following::input[1]");
	private static final By PHONE_TEXT_BOX = By.xpath("//span[text()='Phone1']/following::input[1]");
	private static final By ADDRESS_LINE_TEXT_BOX = By.xpath("//span[text()='Line 1']/following::input[1]");
	private static final By NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/following::span[contains(text(),'PENDING')]/../../following::td[4]//span");
	private static final By PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate']/../../..//span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate']/../../..//span[contains(text(),'PENDING')]/../../following::td[4]//span");
	private static final By RELOAD_PAGE_BTN_IN_LEFT_PANEL = By.xpath("//td[text()='Reload Page']");
	private static final By DEFAULT_SELECTED_SHIPPING_ADDRESS = By.xpath("//span[contains(text(),'Default shipping address')]/following::option[@selected='selected'][1]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pcAutoship']/../../..//span[contains(text(),'Cancelled')]/../../preceding-sibling::td//a");
	private static final By AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT = By.xpath("//span[text()='Autoship Templates']/following::span[1][text()='No Entries']");

	protected RFWebsiteDriver driver;

	public CSCockpitCustomerTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void clickFirstOrderInCustomerTab(){
		driver.waitForElementPresent(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		driver.click(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickPlaceOrderButtonInCustomerTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON);
		driver.click(PLACE_ORDER_BUTTON);
	}

	public String getOrderTypeInCustomerTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeLoc, orderNumber)));
		return driver.findElement(By.xpath(String.format(orderTypeLoc, orderNumber))).getText();
	}

	public String clickAndGetOrderNumberInCustomerTab(){
		driver.waitForElementPresent(ORDER_NUMBER_IN_CUSTOMER_ORDER);
		String orderNumber=driver.findElement(ORDER_NUMBER_IN_CUSTOMER_ORDER).getText();
		logger.info("Order number fetched is "+orderNumber);
		driver.click(ORDER_NUMBER_IN_CUSTOMER_ORDER);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return orderNumber;
	}

	public String getOrderNumberInCustomerTab() {
		driver.waitForElementPresent(ORDER_NUMBER_CUSTOMER_TAB_LOC);
		return driver.findElement(ORDER_NUMBER_CUSTOMER_TAB_LOC).getText();

	}

	public String validateAccountStatusOnCustomerTab() {
		driver.waitForElementPresent(ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC);
		return driver.findElement(ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC).getText();
	}
	public String getOrderTypeOnCustomerTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeCustomerTabLoc,orderNumber)));
		return driver.findElement(By.xpath(String.format(orderTypeCustomerTabLoc, orderNumber))).getText();
	}

	public String getOrderDetailsInCustomerTab(String CID){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, CID)));
		String orderNumber = driver.findElement(By.xpath(String.format(orderDetailsLoc, CID))).getText();
		logger.info("Selected Cutomer order number is = "+orderNumber);
		return orderNumber;
	}

	public boolean getAccountDetailsInCustomerTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
	}

	public boolean verifyCustomerTypeIsPresentInCustomerTab(String customerType){
		driver.waitForElementPresent(By.xpath(String.format(customerTypeLoc, customerType)));
		return driver.isElementPresent(By.xpath(String.format(customerTypeLoc, customerType)));
	}

	public boolean verifyAutoshipTemplateDetailsInCustomerTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(autoshipTemplateDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(autoshipTemplateDetailsLoc, details)));
	}

	public String getAndClickFirstAutoshipIDInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_ID_FIRST);
		String autoshipID = driver.findElement(AUTOSHIP_ID_FIRST).getText();
		driver.click(AUTOSHIP_ID_FIRST);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean verifySectionsIsPresentInCustomerTab(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(orderSectionLoc, sectionName)));
		return driver.isElementPresent(By.xpath(String.format(orderSectionLoc, sectionName)));
	}

	public boolean isAddCardButtonPresentInCustomerTab(){
		driver.isElementPresent(ADD_CARD_BTN);
		return driver.isElementPresent(ADD_CARD_BTN);  
	}

	public void clickAddCardButtonInCustomerTab(){
		driver.isElementPresent(ADD_CARD_BTN);
		driver.click(ADD_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditButtonForCreditCardPresentInCustomerTab(){
		driver.isElementPresent(CREDIT_CARD_EDIT_BTN);
		return driver.isElementPresent(CREDIT_CARD_EDIT_BTN);  
	}

	public boolean isAddNewPaymentProfilePopupPresentInCustomerTab(){
		driver.isElementPresent(ADD_NEW_PAYMENT_PROFILE);
		return driver.isElementPresent(ADD_NEW_PAYMENT_PROFILE);  
	}

	public void clickEditButtonForCreditCardInCustomerTab(){
		driver.isElementPresent(CREDIT_CARD_EDIT_BTN);
		driver.click(CREDIT_CARD_EDIT_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditPaymentProfilePopupPresentInCustomerTab(){
		driver.isElementPresent(EDIT_PAYMENT_PROFILE);
		return driver.isElementPresent(EDIT_PAYMENT_PROFILE);  
	}

	public void clickEditButtonOfShippingAddressInCustomerTab(){
		driver.isElementPresent(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.click(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditAddressPopupPresentInCustomerTab(){
		driver.isElementPresent(EDIT_ADDRESS);
		return driver.isElementPresent(EDIT_ADDRESS);  
	}

	public void clickCloseOfEditAddressPopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.click(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddButtonOfCustomerAddressInCustomerTab(){
		driver.isElementPresent(ADD_NEW_SHIPPING_ADDRESS);
		driver.click(ADD_NEW_SHIPPING_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isCreateNewAddressPopupPresentInCustomerTab(){
		driver.isElementPresent(CREATE_NEW_ADDRESS);
		return driver.isElementPresent(CREATE_NEW_ADDRESS);  
	}

	public void clickAutoshipIdOnCustomerTab() {
		try{
			driver.quickWaitForElementPresent(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			driver.waitForElementPresent(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.waitForLoadingImageToDisappear();
		}
	}

	public boolean verifyAutoshipTemplateSectionInCustomerTab(){
		driver.isElementPresent(AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE);  
	}

	public boolean verifyCustomerOrderSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_ORDER_SECTION);
		return driver.isElementPresent(CUSTOMER_ORDER_SECTION);  
	}

	public boolean verifyCustomerBillingInfoSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_BILLING_INFO);
		return driver.isElementPresent(CUSTOMER_BILLING_INFO);  
	}

	public boolean verifyCustomerAddressSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_ADDRESS);
		return driver.isElementPresent(CUSTOMER_ADDRESS);  
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public void clickCreatePulseTemplateBtn(){
		driver.waitForElementPresent(CREATE_PULSE_TEMPLATE_BTN);
		driver.click(CREATE_PULSE_TEMPLATE_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCreatePulseTemplateBtnOnPopup(){
		driver.waitForElementPresent(CREATE_PULSE_TEMPLATE_BTN_ON_POPUP);
		driver.click(CREATE_PULSE_TEMPLATE_BTN_ON_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNextDueDateOfAutoshipTemplate(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC);
		return driver.findElement(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC).getText();
	}

	public String convertPulseTemplateDate(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		String month = splittedDate[0];
		String year = splittedDate[2];  
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
		System.out.println("Date is "+UIMonth+" "+date+", "+"20"+year);
		return date+" "+UIMonth+", "+year;
	}

	public String getAndClickPulseAutoshipIDHavingTypeAsPulseAutoshipTemplate(){
		driver.waitForElementPresent(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP);
		String autoshipID = driver.findElement(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP).getText();
		logger.info("Pulse Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopup(){
		driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT);
		return driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT);  
	}

	public void clickCreateNewAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.click(CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public boolean verifyAndClickShiipngAddressErrorPopupAndClickOkBtn(){
		driver.waitForElementPresent(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		boolean isPopupPresent = driver.isElementPresent(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		driver.click(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return isPopupPresent;
	}

	public void clickUseThisAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(USE_THIS_ADDRESS);
		driver.click(USE_THIS_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getFirstShippingAddressProfileName(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_PROFILE_FIRST_NAME);
		return driver.findElement(SHIPPING_ADDRESS_PROFILE_FIRST_NAME).getText();
	}

	public void clickSetAsAutoshipChkBoxInCreateNewAddressPopup(){
		driver.waitForElementPresent(SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX);
		driver.click(SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX);
	}

	public void clickOnYesOnUpdateAutoshipAddressPopup(){
		driver.waitForElementPresent(YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP);
		driver.click(YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopupForPendingAutoship(){
		driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP);
		return driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP);  
	}

	public String getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public void clickCloseOfCreateNewAddressPopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_CREATE_NEW_ADDRESS);
		driver.click(CLOSE_POPUP_OF_CREATE_NEW_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean addressCanNotBeAddedForInactiveUserInCustomerTab(){
		driver.isElementPresent(ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		return driver.isElementPresent(ADDRESS_CAN_NOT_BE_ADDED_POPUP);  
	}

	public void clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab(){
		driver.waitForElementPresent(OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		driver.click(OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(String attendentFirstName,String attendeeLastName,String addressLine,String city,String postalCode,String Country,String province,String phoneNumber){
		driver.waitForElementPresent(ATTENDENT_NAME_TEXT_BOX);
		driver.clear(ATTENDENT_NAME_TEXT_BOX);
		driver.type(ATTENDENT_NAME_TEXT_BOX,attendentFirstName+" "+attendeeLastName);
		logger.info("Attendee name entered is "+attendentFirstName+" "+attendeeLastName);
		driver.waitForElementPresent(ADDRESS_LINE_TEXT_BOX);
		driver.type(ADDRESS_LINE_TEXT_BOX,addressLine);
		logger.info("Address line 1 entered is "+addressLine);
		driver.waitForElementPresent(CITY_TOWN_TEXT_BOX);
		driver.type(CITY_TOWN_TEXT_BOX, city);
		logger.info("City entered is "+city);
		driver.waitForElementPresent(POSTAL_TEXT_BOX);
		driver.type(POSTAL_TEXT_BOX, postalCode);
		logger.info("Postal code entered is "+postalCode);
		driver.waitForElementPresent(COUNTRY_TEXT_BOX);
		driver.type(COUNTRY_TEXT_BOX, Country);
		logger.info("Country entered is "+Country);
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(PROVINCE_TEXT_BOX);
		driver.type(PROVINCE_TEXT_BOX, province);
		logger.info("Province entered is "+province);
		driver.waitForElementPresent(PHONE_TEXT_BOX);
		driver.type(PHONE_TEXT_BOX, phoneNumber);
		logger.info("Phone number entered is "+phoneNumber);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNextDueDateOfCRPAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE);
		return driver.findElement(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE).getText();
	}

	public String getAndClickPulseTemplateAutoshipIDHavingStatusIsPending(){
		driver.waitForElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public String getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending(){
		driver.waitForElementPresent(PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING);
		return driver.findElement(PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING).getText();
	}

	public void clickOrderNumberInCustomerOrders(String orderNumber){
		driver.pauseExecutionFor(20000);
		if(driver.isElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)))==true){
			logger.info("Order found");
		}else{
			for(int i=0; i<=10; i++){
				driver.click(RELOAD_PAGE_BTN_IN_LEFT_PANEL);
				driver.pauseExecutionFor(5000);
				if(driver.isElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)))==true){
					break;
				}else{
					continue;
				}

			}
		}
		driver.waitForElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)));
		driver.findElement(By.xpath(String.format(orderNumberLoc, orderNumber))).click();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getDefaultSelectedShippingAddressFromDropDown(){
		driver.waitForElementPresent(DEFAULT_SELECTED_SHIPPING_ADDRESS);
		logger.info("Default selected shipping address in customer tab is "+driver.findElement(DEFAULT_SELECTED_SHIPPING_ADDRESS).getText());
		return driver.findElement(DEFAULT_SELECTED_SHIPPING_ADDRESS).getText().trim();
	}

	public String getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsCancelled(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isAutoshipTemplateHavingNoEntries(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT);
	}

	public String getStatusOfAutoShipIdFromAutoshipTemplate(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(autoshipIdStatusLoc, autoshipId)));
		String autoshipIDStatus = driver.findElement(By.xpath(String.format(autoshipIdStatusLoc, autoshipId))).getText();
		logger.info("Autoship id status from CS cockpit UI Is"+autoshipIDStatus);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipIDStatus;
	}

	public void clickAutoshipIDHavingTypeAsPCAutoshipAndStatusAsCancelled(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(autoshipNumberWhoseAutoshipIsCancelledLoc, autoshipId)));
		driver.click(By.xpath(String.format(autoshipNumberWhoseAutoshipIsCancelledLoc, autoshipId)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

}