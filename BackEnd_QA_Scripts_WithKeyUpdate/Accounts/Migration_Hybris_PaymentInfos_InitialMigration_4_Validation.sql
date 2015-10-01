USE [DataMigration]
GO
/****** Object:  StoredProcedure [Migration].[Migration_Hybris_PaymentInfos_InitialMigration_4_Validation]    Script Date: 7/11/2015 10:11:42 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [Migration].[Migration_Hybris_PaymentInfos_InitialMigration_4_Validation] @LastRun DATETIME = '05/01/1901'
WITH RECOMPILE
AS 
BEGIN 

SET NOCOUNT ON
SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

IF OBJECT_ID('TEMPDB.dbo.#PayInfoMissing') IS NOT NULL 
DROP TABLE #PayInfoMissing 



IF OBJECT_ID('DataMigration.Migration.MissingPayInfos') IS NOT NULL 
DROP TABLE DataMigration.Migration.MissingPayInfos 

IF OBJECT_ID('TEMPDB.dbo.#PayInfoDups') IS NOT NULL 
DROP TABLE #PayInfoDups 

IF OBJECT_ID ('TEMPDB.dbo.#AccountIDs') IS NOT NULL 
DROP TABLE #AccountIDs

SET ANSI_WARNINGS OFF 
DECLARE @Country NVARCHAR(20)= 'US'
DECLARE @ServerMod DATETIME =@LastRun
DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOPerations.RFO_Reference.Countries (NOLOCK) WHERE Alpha2Code = @Country),  @RowCount BIGINT ,
		@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries (NOLOCK) WHERE isocode =  @Country );

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------

SELECT   DISTINCT a.AccountID INTO #AccountIDs --COUNT( DISTINCT a.AccountID)
FROM RFOPerations.RFO_Accounts.AccountRF (NOLOCK)a 
JOIN RFOPerations.RFO_Accounts.AccountBase (NOLOCK)  b ON a.AccountID =b.AccountID 
 JOIN RFOPerations.RFO_Accounts.AccountContacts  (NOLOCK) d ON b.AccountID =d.AccountID 
 JOIN RFOPerations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactID = D.AccountContactID 
 JOIN RFOPerations.RFO_Accounts.AccountContactAddresses  (NOLOCK) g ON g.AccountContactID = d.AccountContactID 
 JOIN RFOPerations.RFO_Accounts.AccountContactPhones  (NOLOCK)j ON j.AccountContactID = d.AccountContactID 
 LEFT JOIN RFOPerations.RFO_Accounts.Phones  (NOLOCK) p ON j.PhoneID =p.PhoneID AND p.PhoneTypeID = 1 
 LEFT JOIN RFOPerations.RFO_Accounts.Addresses  (NOLOCK) i ON i.AddressID =g.AddressID AND i.AddressTypeID =1 AND i.IsDefault= 1 
 LEFT JOIN RFOPerations.RFO_Accounts.EmailAddresses (NOLOCK)  f ON f.EmailAddressID =E.EmailAddressId AND EmailAddressTypeID =1 
 LEFT JOIN RFOPerations.Security.AccountSecurity  (NOLOCK) k ON k.AccountID =a.AccountID 
LEFT JOIN 

(SELECT DISTINCT AccountID
FROM RFOPerations.Hybris.Orders  (NOLOCK)  d 
JOIN RFOPerations.etl.OrderDate  (NOLOCK) od ON d.OrderID =od.OrderID 
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
--AND a.AccountID IN (SELECT p_rfAccountID FROM Hybris.dbo.Users)
--1,084,566 Rows 

--SELECT  DATEADD (Month, -18, GETDATE())
----------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#PayInfoMissing') IS NOT NULL 
DROP TABLE #PayInfoMissing



DECLARE  @RFOPayInfo BIGINT , @HYBPayInfo BIGINT


SELECT @RFOPayInfo =COUNT( PaymentProfileID) FROM RFOPerations.RFO_Accounts.PaymentProfiles (NOLOCK) a
 JOIN RFOPerations.RFO_Accounts.AccountBase (NOLOCK) b ON a.AccountID =b.AccountID
 --JOIN RodanFieldsLive.dbo.AccountPaymentMethods c ON c.AccountPaymentMethodID =a.PaymentProfileID 
WHERE CountryID =@RFOCountry AND a.AccountID IN (SELECT AccountID FROM #AccountIDs)

SELECT @HYBPayInfo=COUNT(a.PK) FROM Hybris.dbo.paymentinfos (NOLOCK)a JOIN Hybris.dbo.Users (NOLOCK) b ON b.PK = a.userpk
 WHERE b.p_country =@HybCountry 
AND a.OwnerPKString IS NOT NULL AND a.OwnerPkString = userpk


SELECT 'Payment Info', @RFOPayInfo AS RFO_Count, @HYBPayInfo AS Hybris_Count


;WITH CTE1 AS 

( SELECT a.AccountID, PaymentProfileID, CountryID 

  FROM RFOPerations.RFO_Accounts.PaymentProfiles (NOLOCK) a 
       JOIN RFOPerations.RFO_Accounts.AccountBase (NOLOCK) d 
	   ON a.AccountID =d.AccountID
	   WHERE d.CountryID = @RFOCountry 
	   AND d.AccountID IN (SELECT AccountID FROM #AccountIDs)
) ,
 CTE2 AS

 ( 
  SELECT b.PK, p_rfaccountpaymentmethodid, p_country
 FROM Hybris.dbo.paymentinfos (NOLOCK) b INNER JOIN Hybris.dbo.Users (NOLOCK) c ON b.userPk =c.Pk
 WHERE p_country = @HybCountry AND b.ownerpkstring IS NOT NULL AND b.OwnerPkString = userpk

 ) 

 
SELECT  e.PaymentProfileID AS RFO_PaymentProfileID,
 f.p_rfaccountpaymentmethodid AS Hybris_rfPaymentProfileID , CASE WHEN f.p_rfaccountpaymentmethodid IS NULL THEN 'Destination'
											                      WHEN e.PaymentProfileID IS NULL THEN 'Source'
																  END AS MissingFROM
INTO DataMigration.Migration.MissingPayInfos
FROM CTE1 e FULL OUTER JOIN  CTE2 f ON e.PaymentProfileID =f.p_rfaccountpaymentmethodid
WHERE (e.PaymentProfileID IS NULL OR f.p_rfaccountpaymentmethodid IS NULL)
/* SELECT @RowCount = COUNT(*) FROM #PayInfoMissing

IF @RowCount > 0
BEGIN 

SELECT 'Total ' + @Country +' PaymentProfiles Missing', @ROWCOUNT


SELECT MissingFrom, COUNT(*)
FROM #PayInfoMissing
GROUP BY MissingFROM 


SELECT * FROM #PayInfoMissing
ORDER BY MissingFrom

END  

*/
IF OBJECT_ID('TEMPDB.dbo.#PayInfoDups') IS NOT NULL 
DROP TABLE #PayInfoDups 


SELECT PaymentProfileID, COUNT(a.PK) AS PaymentInfo_Dups
INTO #PayInfoDups
FROM Hybris.dbo.PaymentInfos a JOIN RFOPerations.RFO_Accounts.PaymentProfiles b ON a.p_rfaccountpaymentmethodid = b.PaymentProfileID
JOIN Hybris.dbo.Users c ON a.userpk =c.PK
WHERE a.OwnerPkString = userpk 
AND c.p_Country= @HybCountry 
GROUP BY b.PaymentProfileID
HAVING COUNT(a.PK) > 1 


SELECT @RowCount = COUNT(*) FROM #PayInfoDups
IF @RowCount > 0
BEGIN 

SELECT  'Duplicate ' + @Country+' PaymentProfiles in Hybris' , @ROWCOUNT

SELECT * FROM #PayInfoDups

END 



---------------------------------------------------------------------------------------------

-- PaymentInfos Framework 


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


--DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOPerations.RFO_Reference.Countries WHERE Alpha2Code = 'US'),
--			@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries WHERE isocode = 'US' );

--DECLARE @LastRUN DATETIME ='05/01/1901'


----------------------------------------------------------------------------------------------------------------------

--- Load PaymentInfos Excepts 
---------------------------------------------------------------------------------------------------------------------


SELECT   
		CAST(PP.PaymentProfileID AS NVARCHAR (100)) AS PaymentProfileID_Code
		, CAST(PP.PaymentProfileID AS NVARCHAR (100)) AS PaymentProfileID
		, CAST (PK AS NVARCHAR) AS Hybris_Owner
		, CAST (PK AS NVARCHAR) AS Hybris_User
		,CAST(PP.AccountID AS NVARCHAR (100))		AS rfAccountID	--rfAccountId
		,CAST (ltrim(rtrim(ccp.NameOnCard ))AS NVARCHAR (100)) AS ProfileName
		--,CAST (RFOperations.[dbo].[DecryptTripleDES] (apm.AccountNumber) AS NVARCHAR (100))  AS CardNumber --, CAST (DisplayNumber AS NVARCHAR(100)) AS CardNumber--, 
		, CAST ( ExpMonth AS NVARCHAR (100))  AS ExpMonth
		, CAST ( ExpYear AS NVARCHAR (100))  AS ExpYear
		,CAST (CCP.BillingAddressID  AS NVARCHAR (100)) AS BillingAddressID
		, CASE WHEN v.Name = 'Invalid' THEN 'Unknown' ELSE CAST (V.Name  AS NVARCHAR (100)) END AS Vendor
INTO   #RFO_PayInfo
FROM    RFOPerations.RFO_Accounts.AccountBase (NOLOCK) AB 
		JOIN RFOPerations.RFO_Accounts.PaymentProfiles (NOLOCK) PP  ON AB.AccountID=PP.AccountID
		--JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountID = ab.AccountID and apm.AccountPaymentMethodID = pp.PaymentProfileID
		JOIN RFOPerations.RFO_Accounts.CreditCardProfiles (NOLOCK) CCP ON PP.PaymentProfileID =CCP.PaymentProfileID
		JOIN RFOPerations.RFO_Reference.CreditCardVendors (NOLOCK) V ON V.VendorID = CCP.VendorID 
		JOIN Hybris.dbo.Users hu ON pp.AccountID =hu.p_rfAccountID
WHERE   AB.CountryID = @RFOCountry 
		AND EXISTS ( 
					SELECT 1 
					FROM Hybris.dbo.paymentinfos (NOLOCK) PIN
					WHERE PIN.p_rfAccountPaymentMethodID = CAST (PP.PaymentProfileID AS NVARCHAR)
				   ) 
		AND CCP.ServerModifiedDate > @LastRun-- AND ccp.NameOnCard IS NOT NULL 


SELECT 
		 CAST(code AS NVARCHAR (100)) AS code
		 , CAST(p_rfAccountPaymentMethodID AS NVARCHAR (100)) AS p_rfAccountPaymentMethodID
		 		,CAST (pin.OwnerPKString AS NVARCHAR) AS OwnerPKString
		,CAST (pin.userpk AS NVARCHAR) AS UserPK	
			 ,CAST (HU.p_rfAccountId AS NVARCHAR (100)) AS p_rfAccountId
		, CAST (p_ccowner AS NVARCHAR (100)) AS ccowner
		--, CAST (p_number AS NVARCHAR (100)) AS number
		, CAST (p_validtomonth AS NVARCHAR (100)) AS p_validtomonth
		, CAST (p_validtoyear AS NVARCHAR (100)) AS p_validtoyear
		  ,CAST (p_rfaddressid AS NVARCHAR (100)) AS p_billingaddress
      , CASE WHEN PIN.p_type= 8796093055067 THEN CAST ('amex' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796093087835 THEN  CAST ('visa' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796093120603 THEN  CAST ('master' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796093153371 THEN  CAST ('diners' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796104818779 THEN  CAST ('maestro' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796104851547 THEN  CAST ('switch' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796104884315 THEN  CAST ('mastercard_eurocard' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796136177755 THEN  CAST ('discover' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796136210523 THEN  CAST ('mastercard' AS NVARCHAR (100))
			WHEN PIN.p_type= 8796136243291 THEN  CAST ('unknown' AS NVARCHAR (100))
		 END AS p_type 
INTO   #Hybris_PayInfo
FROM    Hybris.dbo.Users (NOLOCK) HU 
        JOIN Hybris.dbo.paymentinfos (NOLOCK) PIN ON PIN.UserPk= HU.PK
		LEFT JOIN Hybris.dbo.Addresses (NOLOCK) HA ON HA.PK = PIN.p_billingaddress

WHERE   HU.p_country =  @HybCountry 
		AND EXISTS ( 
					SELECT 1 
					 FROM RFOPerations.RFO_Accounts.PaymentProfiles PP
					 WHERE CAST (PP.PaymentProfileID AS NVARCHAR) = CAST (PIN.p_rfAccountPaymentMethodID AS NVARCHAR)
				   )
				   AND pin.Createdts > @LastRun



CREATE CLUSTERED INDEX MIX_PayProfID ON #RFO_PayInfo (PaymentProfileID)
CREATE CLUSTERED INDEX MIX_PayMethID ON #Hybris_PayInfo (p_RFAccountPaymentMethodID)





SELECT * INTO #PayInfo
FROM #RFO_PayInfo

EXCEPT
SELECT * FROM #Hybris_PayInfo

--INSERT INTO #ExceptReport
--VALUES ('PayInfo',@@ROWCOUNT)




CREATE CLUSTERED INDEX MIX_PayProfID1 ON #payInfo (PaymentProfileID)


SELECT 'RFOADDRESSES', COUNT(*) FROM #RFO_PayInfo  ---252,354

SELECT 'Hybris_ADDRESSES',  COUNT(*) FROM #Hybris_PayInfo  ---252,354

SELECT 'Excepts', COUNT(*) FROM #payInfo---252,354

--SELECT TOP 2 PaymentProfileID, CardNumber from #RFO_PayInfo
--WHERE CardNumber IS NOT NULL 




----------------------------------------------------------------------------------------------------------------------------
DECLARE @I INT = (SELECT MIN(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'PaymentInfos') , 
@C INT =  (SELECT MAX(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'PaymentInfos') 


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

DROP INDEX MIX_PayProfID ON #RFO_PayInfo 
DROP INDEX MIX_PayMethID ON #Hybris_PayInfo 
DROP INDEX MIX_PayProfID1 ON #PayInfo 


END 

