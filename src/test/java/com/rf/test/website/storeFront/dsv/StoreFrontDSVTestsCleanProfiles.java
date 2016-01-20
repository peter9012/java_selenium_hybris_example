package com.rf.test.website.storeFront.dsv;

import org.testng.annotations.Test;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.DSVStoreFrontBillingInfoPage;
import com.rf.pages.website.DSVStoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class StoreFrontDSVTestsCleanProfiles extends RFWebsiteBaseTest{
	private DSVStoreFrontHomePage dsvStoreFrontHomePage;
	private DSVStoreFrontBillingInfoPage dsvStoreFrontBillingInfoPage;
	
	
	@Test
	public void cleanAllBillingProfilesConsultant() throws Exception{
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_CONSULTANT_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_CONSULTANT_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontBillingInfoPage = dsvStoreFrontHomePage.clickBillingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontBillingInfoPage.cleanAllBillingProfiles();
	}
	
	@Test
	public void cleanAllBillingProfilesPC() throws Exception{
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_PC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_PC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontBillingInfoPage = dsvStoreFrontHomePage.clickBillingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontBillingInfoPage.cleanAllBillingProfiles();
	}
	
	@Test
	public void cleanAllBillingProfilesRC() throws Exception{
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_RC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_RC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontBillingInfoPage = dsvStoreFrontHomePage.clickBillingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontBillingInfoPage.cleanAllBillingProfiles();
	}
}