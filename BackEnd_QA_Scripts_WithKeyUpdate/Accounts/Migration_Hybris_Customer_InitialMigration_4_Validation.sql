USE [DataMigration]
GO
/****** Object:  Sto[Migration].[Migration_Hybris_Customer_InitialMigration_4_Validation]redProcedure [Migration].[Migration_Hybris_Customer_InitialMigration_4_Validation]    Script Date: 7/11/2015 3:17:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [Migration].[Migration_Hybris_Customer_InitialMigration_4_Validation] @LastRun DATETIME = '05/01/1901'
WITH RECOMPILE
AS 
BEGIN 

SET NOCOUNT ON
SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

IF OBJECT_ID('TEMPDB.dbo.#Accounts_Missing') IS NOT NULL 
DROP TABLE #Accounts_Missing 


IF OBJECT_ID('DataMigration.Migration.AccountsMissing') IS NOT NULL 
DROP TABLE DataMigration.Migration.AccountsMissing


IF OBJECT_ID('TEMPDB.dbo.#Accounts_Dups') IS NOT NULL 
DROP TABLE #Accounts_Dups 

IF OBJECT_ID ('TEMPDB.dbo.#AccountIDs') IS NOT NULL 
DROP TABLE #AccountIDs

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRun

DECLARE @Country NVARCHAR(20)= 'US'
DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries (NOLOCK) WHERE Alpha2Code = @Country),  @RowCount BIGINT ,
		@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries (NOLOCK) WHERE isocode =  @Country );

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------

SELECT   DISTINCT a.AccountID INTO #AccountIDs --COUNT( DISTINCT a.AccountID)
FROM RFOperations.RFO_Accounts.AccountRF (NOLOCK)a 
JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)  b ON a.AccountID =b.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountContacts  (NOLOCK) d ON b.AccountID =d.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactID = D.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses  (NOLOCK) g ON g.AccountContactID = d.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK)j ON j.AccountContactID = d.AccountContactID 
 LEFT JOIN RFOperations.RFO_Accounts.Phones  (NOLOCK) p ON j.PhoneID =p.PhoneID AND p.PhoneTypeID = 1 
 LEFT JOIN RFOperations.RFO_Accounts.Addresses  (NOLOCK) i ON i.AddressID =g.AddressID AND i.AddressTypeID =1 AND i.IsDefault= 1 
 LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK)  f ON f.EmailAddressID =E.EmailAddressId AND EmailAddressTypeID =1 
 LEFT JOIN RFOperations.Security.AccountSecurity  (NOLOCK) k ON k.AccountID =a.AccountID 
LEFT JOIN 

(SELECT DISTINCT AccountID
FROM RFOperations.Hybris.Orders  (NOLOCK)  d 
JOIN RFOperations.etl.OrderDate  (NOLOCK) od ON d.OrderID =od.OrderID 
WHERE od.StartDate >=  '2014-05-01'
)  c ON b.AccountID =c.AccountID 

WHERE 1=1 
AND b.CountryID =@RFOCountry 
AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
AND (b.AccountStatusID <>3 OR  c.AccountID IS NOT NULL ) 
AND f.EmailAddressID IS NOT NULL 
AND i.AddressID IS NOT NULL 
AND p.PhoneID IS NOT NULL 
AND k.AccountID IS NOT NULL 
AND b.ServerModifiedDate > @LastRun
--AND a.AccountID IN (SELECT p_rfAccountID FROM Hybris.dbo.Users)
--1,084,566 Rows 

--SELECT  DATEADD (Month, -18, GETDATE())



DECLARE @RFOAccount BIGINT, @HYBAccount BIGINT
SELECT @RFOAccount =COUNT( DISTINCT AccountID) FROM RFOPerations.RFO_Accounts.AccountBase (NOLOCK)
WHERE CountryID =@RFOCountry AND AccountID IN (SELECT AccountID FROM #AccountIDs)


SELECT @HYBAccount=COUNT(PK) FROM Hybris.dbo.Users (NOLOCK) WHERE p_country =@HybCountry 


SELECT 'Accounts', @RFOAccount AS RFO_Accounts, @HYBAccount AS Hybris_Accounts, ABS(@RFOAccount - @HYBAccount) AS Difference 




SELECT  AccountID AS RFO_AccountID,
 b.p_rfaccountid AS Hybris_rfAccountID , CASE WHEN b.p_rfaccountid IS NULL THEN 'Destination'
											  WHEN a.AccountID IS NULL THEN 'Source' END AS MissingFROM
INTO DataMigration.Migration.AccountsMissing
FROM 


( SELECT AccountID FROM #AccountIDs) a
  
    FULL OUTER JOIN 
   
   ( SELECT p_rfAccountID FROM  Hybris.dbo.Users  (NOLOCK)
    WHERE p_country = @HybCountry) b 
	
	ON a.AccountID =b.p_rfaccountid
WHERE (a.AccountID IS NULL OR b.p_rfaccountid IS NULL) 


--SELECT  TOP 20 * FROM #Accounts_Missing
--ORDER BY NEWID()
/*
SELECT @RowCOunt = COUNT(*) FROM #Accounts_Missing

IF @RowCount > 0
BEGIN 

SELECT ' Total Missing ' + @Country + ' Accounts', @ROWCOUNT

SELECT MissingFROM, COUNT(*)
FROM #Accounts_Missing 
GROUP BY MissingFROM 

SELECT * FROM #Accounts_Missing
ORDER BY MissingFrom

END  
*/
--SELECT * FROM Hybris.dbo.Users 
--WHERE p_rfAccountID = 591704


--SELECT * FROM RFOPerations.RFO_Accounts.AccountBase
--WHERE AccountID =591704

--SELECT * FROM RFOPerations.RFO_Accounts.AccountRF 
--WHERE AccountID =591704

--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

SELECT AccountID, COUNT (PK) AS CountofDups
INTO #Accounts_Dups
FROM Hybris.dbo.Users a JOIN RFOPerations.RFO_Accounts.AccountBase b ON a.p_rfaccountid = b.AccountID
WHERE CountryID = @RFOCountry
GROUP BY  AccountID
HAVING COUNT (PK)> 1 


SELECT @RowCount = COUNT(*) FROM #Accounts_Dups

IF @RowCount > 0
BEGIN 

SELECT  'Duplicate ' + @Country+' Accounts in Hybris' , @ROWCOUNT

SELECT * FROM #Accounts_Dups

END 

ELSE 

SELECT 'No Duplicates'

---------------------------------------------------------------------------------------------

-- Accounts Framework 


---------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#Accounts') IS NOT NULL DROP TABLE #Accounts 
IF OBJECT_ID('TEMPDB.dbo.#ExceptReport') IS NOT NULL DROP TABLE #ExceptReport 
IF OBJECT_ID('TEMPDB.dbo.#Column_Excepts') IS NOT NULL DROP TABLE #Column_Excepts 
IF OBJECT_ID('TEMPDB.dbo.#Addresses') IS NOT NULL DROP TABLE #Addresses
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Accounts') IS NOT NULL DROP TABLE #Hybris_Accounts
IF OBJECT_ID('TEMPDB.dbo.#RFO_Accounts') IS NOT NULL DROP TABLE #RFO_Accounts
IF OBJECT_ID('TEMPDB.dbo.#RFO_Addresses') IS NOT NULL DROP TABLE #RFO_Addresses
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Addresses') IS NOT NULL DROP TABLE #Hybris_Addresses
IF OBJECT_ID('TEMPDB.dbo.#RFO_PayInfo') IS NOT NULL DROP TABLE #RFO_PayInfo
IF OBJECT_ID('TEMPDB.dbo.#Hybris_PayInfo') IS NOT NULL DROP TABLE #Hybris_PayInfo
IF OBJECT_ID('TEMPDB.dbo.#PayInfo') IS NOT NULL DROP TABLE #PayInfo

TRUNCATE TABLE  DataMigration.Migration.ErrorLog_Accounts

-----------------------------------------------------------------------------------------------


--DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries WHERE Alpha2Code = 'US'),
--			@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries WHERE isocode = 'US' );

--DECLARE @LastRUN DATETIME ='05/01/1901'


----------------------------------------------------------------------------------------------------------------------

--- Load Accounts Excepts 
---------------------------------------------------------------------------------------------------------------------

;WITH PayInfo AS
(
SELECT AccountID,  PaymentProfileID
FROM RFOperations.RFO_Accounts.PaymentProfiles 
WHERE IsDefault=1 
),

Shipping AS 
(
SELECT AccountID, a.AddressID 
FROM RFOperations.RFO_Accounts.Addresses A 
JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
WHERE AddressTypeID = 2 AND IsDefault=1 AND A.CountryID =@RFOCountry

)
, Billing AS 
(
SELECT AccountID, A.AddressID 
FROM RFOperations.RFO_Accounts.Addresses A 
JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
WHERE AddressTypeID = 3 AND IsDefault=1 AND A.CountryID = @RFOCountry 
) 
, 
Main AS 
(
SELECT AccountID, A.AddressID, A.Locale, A.Region
FROM RFOperations.RFO_Accounts.Addresses A 
JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
WHERE AddressTypeID = 1 AND IsDefault=1 AND A.CountryID = @RFOCountry
) 
, 
EnrolledPC AS
( SELECT AccountID, 1 AS EnrolledASPC
FROM RFOperations.Hybris.Autoship
WHERE Active =1 AND AutoShipTypeID =1 
)
, EnrolledCRP AS
(SELECT AccountID, 1 AS EnrolledASCRP
FROM RFOperations.Hybris.Autoship
WHERE Active =1 AND AutoShipTypeID =2)

, 
EnrolledPulse AS 
(SELECT AccountID, 1 AS EnrolledASPulse
FROM RFOperations.Hybris.Autoship
WHERE Active =1 AND AutoShipTypeID =3)




SELECT DISTINCT CAST (AB.AccountID AS NVARCHAR (100)) AS AccountID				--p_rfaccountid
        , CASE WHEN Birthday = 'Jan  1 1900 12:00AM' OR Birthday = '' THEN NULL 
		ELSE CAST (AC.Birthday AS NVARCHAR (100)) END AS Birthday					--,p_dateofbirth
        ,CAST (AR.HardTerminationDate AS NVARCHAR(100)) AS HardTerminationDate		--,p_hardterminationdate
        ,CAST (AR.IsTaxExempt AS NVARCHAR(100)) AS IsTaxExempt					--	,p_excemptfromtax
        ,CAST (LTRIM(RTRIM(EA.EmailAddress)) AS NVARCHAR(255)) as EmailAddress			--,p_customeremail
        ,AB.AccountNumber 				--,p_rfaccountnumber
        ,CAST (AR.SponsorId	AS NVARCHAR(100)) AS SponsorID					--,p_sponsorid
        ,CAST (LTRIM(RTRIM(AR.CoApplicant)) AS NVARCHAR(100)) AS CoApplicant					--	,p_enrollspousename
        ,CAST(LTRIM(RTRIM(PH.PhoneNumberRaw)) AS NVARCHAR(100)) AS PhoneNumberRaw				--,p_mainphone
        ,CAST (AR.NextRenewalDate	AS NVARCHAR(100)) AS NextRenewalDate			--	,p_expirationdate
        ,CAST (AR.LastRenewalDate	AS NVARCHAR(100)) AS LastRenewalDate			--	,p_renewlatertime

        ,CASE WHEN AccountTypeID =1 THEN CAST (EnrollmentDate AS NVARCHAR(100)) 	--	,p_consultantsince
             ELSE NULL
        END AS ConsEnrollmentDate ,

		CASE WHEN AccountTypeID =2 THEN CAST (EnrollmentDate AS NVARCHAR(100))	--	,p_consultantsince
             ELSE NULL
        END AS PCEnrollmentDate 
		
		,CAST (0 AS NVARCHAR(100)) AS HasOrder	--CASE WHEN O.OrderID IS NOT NULL THEN CAST (1 AS NVARCHAR(100))					--p_hasorder
								--	 ELSE CAST (0 AS NVARCHAR(100))	
								--	 END AS HasOrder 
        --
        ,CASE WHEN LTRIM(RTRIM(AR.CoApplicant)) IS NOT NULL						--p_enrollallowspouse
                  AND AR.CoApplicant <> '' THEN CAST (1 AS NVARCHAR(100))	
             ELSE CAST (0 AS NVARCHAR(100))	
        END AS EnrollSpouseAllow 

        ,CASE WHEN AR.Active = 0 THEN CAST (1 AS NVARCHAR(100))	                            --p_logindisabled
             ELSE CAST (0 AS NVARCHAR(100))	
        END AS p_logindisabled 

        ,
		CASE WHEN AC.GenderID =1  THEN CAST ('Female'AS NVARCHAR(100))						--p_Gender
		     WHEN AC.GenderID =2  THEN CAST ('Male'AS NVARCHAR(100))		
			 ELSE NULL END  AS Gender	
			 							
        ,CASE WHEN AB.CountryID = 236 THEN CAST('US'AS NVARCHAR(100))	
			  WHEN AB.CountryID = 40 THEN CAST('CA'	AS NVARCHAR(100))	
			  ELSE NULL END AS Country	
			  						-- p_country
		, CASE WHEN AB.CurrencyID = 38 THEN CAST('CAD'AS NVARCHAR(100))	
		       WHEN AB.CurrencyID = 4  THEN CAST ('USD'AS NVARCHAR(100))	
		      ELSE NULL END AS Currency 

		,CASE WHEN ast.Name = 'Begun Enrollment' THEN CAST ('Pending' AS NVARCHAR(100))	     --p_AccountStatus
		 ELSE CAST (ast.Name AS NVARCHAR (100)) END AS AccountStatus 

		, CASE WHEN LanguageID = 4 THEN CAST ('en' AS NVARCHAR(100)) ELSE NULL END AS PreferredLanguage  -- p_preferredlanguage
		, CASE WHEN LanguageID = 4 THEN CAST ('en' AS NVARCHAR(100))  ELSE NULL END AS SessionLanguage    -- p_sessionlanguage
		, CASE WHEN Active =1 THEN CAST (ASE.username AS NVARCHAR (255))
			   WHEN Active= 0 THEN CAST (CONCAT (AccountNumber,'_',UserName) AS NVARCHAR (255)) END AS UniqueID
		--,CAST (ASE.Password AS NVARCHAR(100)) AS Passwd -- P_Passwd
		--, CAST (S.AddressID AS NVARCHAR(100)) AS DefaultShippingAddress-- Default Shipping Address 
		--, CAST (B.AddressID AS NVARCHAR(100)) AS DefaultBillingAddress 
		--, CAST (P.PaymentProfileID AS NVARCHAR(100)) AS DefaultPaymentInfo -- DefaultPaymentInfo 
		, CAST (LTRIM(RTRIM(CONCAT(AC.FirstName ,' ',AC.LastName))) AS NVARCHAR(255)) AS Name
		, CASE WHEN LEN (m.Region)> 2 THEN  CAST (r.Region AS NVARCHAR(100)) 
		       ELSE CAST (m.Region AS NVARCHAR(100)) END AS ConsultantState
		, CAST (M.Locale AS NVARCHAR(100)) AS ConsultantTown
		, CAST (ISNULL(EPC.EnrolledASPC,0) AS NVARCHAR(100)) AS EnrolledASPC
		,CAST (ISNULL (EC.EnrolledASCRP,0) AS NVARCHAR(100)) AS EnrolledASCRP
		,CAST (ISNULL(EPU.EnrolledASPulse,0)  AS NVARCHAR(100)) AS EnrolledASPulse
		, CASE WHEN AB.AccountTypeID =1 THEN CAST ('Consultant' AS NVARCHAR(100))
			   WHEN AB.AccountTypeID =2 THEN CAST ('PC'AS NVARCHAR(100))
			   WHEN AB.AccountTypeID =3 THEN CAST ('RC' AS NVARCHAR(100))
		  ELSE NULL END AS CustomerGroup

		  
		  
	INTO #RFO_Accounts


		FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		--JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID 
		JOIN RFOperations.RFO_Reference.AccountStatus (NOLOCK) AST ON AST.AccountStatusID = AB.AccountStatusID
		JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
		JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK) ACPH ON ACPH.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) PH ON PH.PhoneID = ACPH.PhoneId
                                                    AND PH.PhoneTypeID = 1
                                                    AND PH.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountEmails  (NOLOCK) AE ON AE.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) EA ON EA.EmailAddressID = AE.EmailAddressId
                                                            AND EmailAddressTypeID = 1
                                                            AND EA.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) AR ON AB.AccountID = AR.AccountID
		JOIN RFOperations.Security.AccountSecurity (NOLOCK) ASE ON ASE.AccountID = ab.AccountID

		LEFT JOIN Shipping S ON S.AccountId=AB.AccountID 
		LEFT JOIN Billing B ON B.AccountID =AB.AccountID
		LEFT JOIN PayInfo P ON P.AccountID = AB.AccountID
	    LEFT JOIN Main M ON M.AccountId = AB.AccountID
		--LEFT JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AccountContactId = AC.AccountContactId
		--LEFT JOIN RFOperations.RFO_Accounts.Addresses A ON A.AddressID = ACA.AddressID
		LEFT JOIN EnrolledCRP EC ON EC.AccountID = AB.AccountID
		LEFT JOIN EnrolledPC EPC ON EPC.AccountID = AB.AccountID
		LEFT JOIN EnrolledPulse EPU ON EPU.AccountID = AB.AccountID
		LEFT JOIN DataMigration.Migration.RegionMapping R ON R.region= m.region 

WHERE   AB.CountryID =@RFOCountry
        AND EXISTS ( SELECT 1
                     FROM   Hybris.dbo.users HU
                     WHERE  HU.p_rfaccountid = AB.AccountID)
		AND ab.AccountID IN (SELECT AccountID FROM #AccountIDs)
        AND AB.ServerModifiedDate >@LastRUN--@ServerMod
		
		 
UPDATE a
SET HasOrder = CAST ('1' AS NVARCHAR (100))
FROM RFOperations.Hybris.Orders b  JOIN RFOperations.etl.OrderDate c ON c.OrderID =b.OrderID 
JOIN #RFO_Accounts a ON  a.AccountID =b.AccountID
WHERE b.AccountID IS NOT NULL AND StartDate > '05/01/2014'






SELECT  
		CAST (p_rfaccountid AS NVARCHAR (100)) AS p_rfAccountID,
        CAST (HU.p_dateofbirth AS NVARCHAR (100)) AS p_dateofbirth,
        CAST (p_hardterminationdate AS NVARCHAR (100)) AS p_hardterminationdate,
        CAST (p_excemptfromtax AS NVARCHAR (100)) AS p_excemptfromtax,
        CAST (LTRIM(RTRIM(p_customeremail)) AS NVARCHAR (255)) AS p_customeremail,
        CAST (p_rfaccountnumber AS NVARCHAR (100)) AS p_rfaccountnumber,
        CAST (p_sponsorid AS NVARCHAR (100)) AS p_sponsorid,
        CAST (LTRIM(RTRIM(HU.p_enrollspousename)) AS NVARCHAR (100)) AS p_enrollspousename,
        CAST (LTRIM(RTRIM(p_mainphone)) AS NVARCHAR (100)) AS p_mainphone  ,
        CAST (p_expirationdate AS NVARCHAR (100)) AS p_expirationdate ,
        CAST (p_renewlatertime AS NVARCHAR (100)) AS p_renewlatertime,
        CAST (p_consultantsince AS NVARCHAR (100)) AS p_consultantsince,
		CAST (HU.p_preferredcustomersince AS NVARCHAR (100)) AS p_preferredcustomersince
       ,CAST (p_hasorder AS NVARCHAR (100)) AS p_hasorder
       ,CAST (p_enrollallowspouse AS NVARCHAR (100)) AS p_enrollallowspouse,
		CAST (p_logindisabled AS NVARCHAR (100)) p_logindisabled ,

		CASE WHEN  HU.p_gender = 8796093874267	THEN CAST ('Female' AS NVARCHAR (100))
			 WHEN  HU.p_gender = 8796093841499 THEN CAST ('Male' AS NVARCHAR (100))
			 ELSE NULL END AS p_gender, 

	  	CASE WHEN HU.p_Country = 8796100624418 THEN CAST ('US' AS NVARCHAR (100))
		     WHEN HU.p_Country = 8796094300194 THEN CAST ('CA' AS NVARCHAR (100))
			  ELSE NULL END AS p_country ,

		CASE WHEN HU.p_sessioncurrency = 8796125855777 THEN CAST ('USD' AS NVARCHAR (100))
		     WHEN HU.p_sessioncurrency = 8796125888545 THEN CAST ('CAD' AS NVARCHAR (100))
			 ELSE NULL END AS p_SessionCurrency ,
		CASE WHEN p_AccountStatus =  8796135915611 THEN CAST ('PENDING' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796135948379 THEN CAST ('ACTIVE' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796135981147 THEN CAST ('ONHOLD' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796136013915 THEN CAST ('SUSPENDED' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796136046683 THEN CAST ('HIATUS' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796136079451 THEN CAST ('SOFT TERMINATED VOLUNTARY' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796136112219 THEN CAST ('SOFT TERMINATED INVOLUNTARY' AS NVARCHAR (100))
			 WHEN p_AccountStatus =  8796136144987 THEN CAST ('HARD TERMINATED' AS NVARCHAR (100))
			 END AS p_AccountStatus,
		CASE WHEN p_preferredLanguage = 8796093055008 THEN 'en' ELSE CAST (p_preferredLanguage AS NVARCHAR (100)) END AS p_PreferredLanguage, 
		CASE WHEN p_sessionlanguage = 8796093055008 THEN 'en' ELSE CAST (p_SessionLanguage AS NVARCHAR (100)) END AS p_SessionLanguage,
        CAST (UniqueID AS NVARCHAR (100)) AS UniqueID,
		--CAST (Passwd AS NVARCHAR (100)) AS Passwd,
		--CAST (HAS.p_rfaddressid AS NVARCHAR (100))AS p_DefaultShippingAddress,
		--CAST (HAB.p_rfaddressid AS NVARCHAR (100)) AS p_DefaultPaymentAddress,
		--CAST (HP.p_rfaccountpaymentmethodid AS NVARCHAR (100)) AS p_DefaultPaymentInfo,
		CAST (HU.name AS NVARCHAR (100)) AS Name,
		CAST (r.isocode  AS NVARCHAR (100)) AS p_consultantstate,
		CAST (HU.p_consultanttown AS NVARCHAR (100)) AS p_consultanttown,
		CAST (HU.p_enrolledaspc AS NVARCHAR (100)) AS p_enrolledaspc,
		CAST (HU.p_enrolledascrp AS NVARCHAR (100)) AS p_enrolledascrp,
		CAST (p_enrolledaspulse AS NVARCHAR (100)) AS p_enrolledaspulse,
		CASE WHEN TargetPK = 8796126117893 THEN CAST ('PC' AS NVARCHAR (100))
		WHEN TargetPK = 8796126150661 THEN CAST ('Consultant' AS NVARCHAR (100))
		WHEN TargetPK =8796126085125 THEN CAST ('RC' AS NVARCHAR (100))
		ELSE NULL END AS CustomerGroup
INTO #Hybris_Accounts 


FROM    Hybris.dbo.users (NOLOCK) HU 
		JOIN Hybris.dbo.pgrels (NOLOCK) PG ON HU.PK= PG.SourcePK
		JOIN Hybris.dbo.countries  (NOLOCK) C ON C.PK = HU.p_country
		LEFT JOIN Hybris.dbo.PaymentInfos (NOLOCK) HP ON HP.PK= HU.p_defaultpaymentinfo 
		LEFT JOIN Hybris.dbo.Addresses (NOLOCK) HAS ON HAS.PK = HU.defaultshippingaddress
		LEFT JOIN Hybris.dbo.Addresses  (NOLOCK) HAB ON HAB.PK = HU.defaultpaymentaddress
		LEFT JOIN Hybris.dbo.Regions (NOLOCK) R ON r.PK =HU.p_consultantstate 
WHERE   HU.p_country = @HybCountry --AND TargetPK <> 8796126150661
        AND EXISTS ( SELECT 1
                     FROM   RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
                     WHERE  AB.AccountID = HU.p_rfaccountid)
		AND (Hu.Createdts >@ServerMod --AND  hu.p_sourcename = 'Hybris-DM'
		)--OR HU.ModifiedTS >@ServerMod

CREATE CLUSTERED INDEX MIX_AccountID ON #RFO_Accounts (AccountID)
CREATE CLUSTERED INDEX MIX_rfAccountID ON #Hybris_Accounts (p_rfAccountID)



SELECT * INTO  #Accounts FROM #RFO_Accounts

EXCEPT 

SELECT * FROM #Hybris_Accounts



CREATE CLUSTERED INDEX MIX_AccountID1 ON #Accounts (AccountID)


SELECT 'RFOAccounts', COUNT(*) FROM #RFO_Accounts  ---252,354

SELECT 'Hybris_Accounts',  COUNT(*) FROM #Hybris_Accounts  ---252,354

SELECT 'Excepts', COUNT(*) FROM #Accounts ---252,354



SELECT MissingFROM, COUNT(*)
FROM DataMigration.Migration.AccountsMissing 
GROUP BY MissingFROM 



DECLARE @I INT = (SELECT MIN(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Users') , 
@C INT =  (SELECT MAX(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Users') 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     DataMigration.Migration.Metadata_Accounts
                               WHERE    MapID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

DECLARE @DesTemp NVARCHAR (50) =(SELECT CASE WHEN HybrisObject = 'Users' THEN '#Hybris_Accounts'
										     WHEN HybrisObject = 'Addresses' THEN '#Hybris_Addresses'
										     WHEN HybrisObject = 'PaymentInfos' THEN '#Hybris_PayInfo'
											 END
			FROM  DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								) 

DECLARE @DesCol NVARCHAR (50) =(SELECT Hybris_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								)

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject= 'Users'
                                             THEN 'p_rfAccountID'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN 'p_rfAddressID'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN 'p_rfaccountPaymentMethodID'
                                        END
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol +
' FROM DataMigration.Migration.ErrorLog_Accounts a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO DataMigration.Migration.ErrorLog_Accounts (Identifier,MapID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= @LastRun

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 


SELECT  b.RFO_column, COUNT(*) AS Counts
FROM RFOPerations.dbo.ErrorLog_Accounts A JOIN RFOPerations.dbo.Metadata_Accounts B ON a.MapID =b.MapID
GROUP BY b.MapID, RFO_Column


drop index MIX_AccountID ON #RFO_Accounts 
drop index MIX_rfAccountID ON #Hybris_Accounts
drop index MIX_AccountID1 ON #Accounts

END 

