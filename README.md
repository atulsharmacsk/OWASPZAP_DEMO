
-------------------------------DEMO 1-------------------------------------

The OWASP Zed Attack Proxy is a free security tool 
which acts as a proxy between browser and network, find security issues in web applications 
& report them to the end user.

ZAP by default passively scans all HTTP messages (requests and responses) sent to the web application being tested. 
Passive scanning does not change the HTTP messages.
Active scanning attempts to find potential issues by using known attacks against the selected targets. 
Active scanning is an attack on those targets.

Penetration testing can never be done without taking permission from the owner of the web apps. 

Tools used:- IntelliJ, Java, Maven, TestNG
 
 dependencies:-
* TestNG-https://mvnrepository.com/artifact/org.testng/testng/6.11
* Selenium-https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java/4.7.2
* zapClient- https://mvnrepository.com/artifact/org.zaproxy/zap-clientapi/1.11.0
* WebDriverManager - https://mvnrepository.com/artifact/io.github.bonigarcia/webdrivermanager/5.3.1

Demo Steps:  
 Step 1:- download the owasp zap zip file from-https://github.com/zaproxy/zaproxy/releases/download/v2.11.1/ZAP_2.11.1_Crossplatform.zip
 & extract the content.
 
 Step 2:-Run the bat file from extracted folder, 
 once tool gets started-select first radio button and click on start. 
 This is a prerequisite that this tool shall be kept open for scan to work.
 by default it will open on port-8080, http://127.0.0.1:8080
 
 Step 3:-Naviagte to tools-Options-API, note the api key.
 
 Step 4:-Create a new maven java project in IDE.
 
 Step 5:- Add following dependencies in pom.xml-testng, zap-clientapi, selenium, webdrivermanager
 
 Step 6:- Create a project where we will do following things:-
			- Define owasp zap as a proxy,driver setup, using proxy in driver & initializing ClientAPI reference.
			- Open url on which scan needs to be performed, wait for passive scan to completed
			- Generate scan report and close browser window. 
			
		(https://www.zaproxy.org/docs/desktop/addons/report-generation/#template)
		String title="Demo Title";
        String template="traditional-html";
        String theme = null;
        String description="Demo description";
        String contexts=null;
        String sites=site_to_test;
        String sections=null;
        String includedconfidences=null;
        String includedrisks=null;
        String reportfilename="Demofilename";
        String reportfilenamepattern=null;
        String reportdir=System.getProperty("user.dir");
        String display=null;
		
		getClientAPI().reports.generate(title,template,theme,description,contexts,sites,sections,
                    includedconfidences,includedrisks,reportfilename,reportfilenamepattern,reportdir,display);
		
scan url-http://127.0.0.1:8080/
Or go through https://owasp.org/www-project-vulnerable-web-applications-directory/, please read the usage policies before using it for passive scan. Active scan though these sites might allow(please see in the policy) but on a safer side I will recommend to perfom on loclahost only.		
		

ZAP API Reference with end points-https://www.zaproxy.org/docs/api/
ZAP API on local-http://127.0.0.1:8080/UI		
		
Glimpse of upcoming topics that we will cover through automation-
- Overview of the reporting capabilities. 
- Site tree-Adding a url, verifying the added urls, removing the urls from site tree.
- Running an active scan and waiting till the scan is completed.
- Running both passive and Active scan & creating separate reports for both.
- Setting up postman for zap client end points.
- Using Restassured as another approach for interacting with zapClient api.
		
-------------------------------DEMO 2-------------------------------------

Agenda- 
- Enabling test case execution from maven/cmd line and passing api key as a parameter
- Exploring various reporting attributes from genrateReport method of zapUtil. (https://www.zaproxy.org/docs/desktop/addons/report-generation/api/)
- Some basic terminologies like Strength, Threshold, Passive scan rule, Policies, Confidence
 

Steps:-
1. Create testng.xml and include sureflre plugin in pom.xml. provide testng.xml location to surefire plugin.
Surefire with testng-https://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html

testng.xml->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Practice Suite">
    <test name="Test Basics 1">
        <classes>
            <class name="ZapTest"/>
        </classes>
    </test> <!-- Test -->
</suite> <!-- Suite -->


Addition to pom.xml,  after closure of dependenices tag
    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>3.0.0-M8</version>
                    <configuration>
                        <suiteXmlFiles>
                            <suiteXmlFile>./src/test/testng.xml</suiteXmlFile>
                        </suiteXmlFiles>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
	
2.	To run the test cases use command->
mvn -DAPIKEY=api_key_value -DforkCount=0 test
where 
-DAPIKEY is to pass apikey in form of parameter.
-DforkCount to debug your code. 

3. Understand parameters of report method:-
https://www.zaproxy.org/docs/desktop/addons/report-generation/api/
https://www.zaproxy.org/docs/desktop/addons/report-generation/templates/

Basic Terms:-
strength & threshold;-
Strength. (Intensity of attack, only for active scan)
This controls the number of attacks that ZAP will perform. 
If you select Low then fewer attacks will be used which will be quicker but may miss some issues. 
If you select High then more attacks will be used which may find more issues but will take longer.

threshold. (Once a Threshould is crossed only then alert are raised)
This controls how likely ZAP is to report potential threat.
If you select Off then the scan rule won’t run.
If you select Low then more potential issues will be raised which mean there can be lot unsignificant threats reported.
If you select High then fewer potential issues will be raised which may mean that some real issues are missed.

Policy - A way to provide different strengthe and threshold levels to various type of threat.
Can be found in Policy Manager.

Passive Scan- Passive Scan rules
Active Scan- we use policies.

Confidence:-"confidence" of or in the finding. In other word how sure ZAP is in the finding/alert.

-------------------------------DEMO 3-------------------------------------

Agenda-
1. Setting up Postman with the zap end points. 
2. Executing Active Scan using zapClientAPI.

Steps:-
1. Created postman collection for the end points from https://www.zaproxy.org/docs/api/.
2. Try to check active scan from postman and understand the prerequisite for the same.
3. Adding url to Site tree using zapClientAPI. 
4. Verifying the url got added to scan tree.
4. How to Run Active Scan using zapClientAPI.
5. Waiting dynamically for active scan to get completed.
6. Excluding certian urls from getting into scan tree.


Adding url to site trr-
		clientApi.core.accessUrl(site_to_test, "false");

Verifying the urls added-
        apiResponse = clientApi.core.urls();
        System.out.println("test");
        List<ApiResponse> list = ((ApiResponseList) apiResponse).getItems();

Active scan parameters-
        String url = site_to_test;
        String recurse = null;
        String inscopeonly = null;
        String scanpolicyname = null;
        String method = null;
        String postdata = null;
		Integer contextId=0;
        apiResponse = clientApi.ascan.scan(url, recurse, inscopeonly, scanpolicyname, method, postdata, contextId);
        String scanId = ((ApiResponseElement) apiResponse).getValue();

WaitillActiveScan-
        apiResponse = clientApi.ascan.status(scanId);
        String scanProgress = ((ApiResponseElement) apiResponse).getValue();
			
Things to come-
- Creating contexts.
- Active scan parameters.
- Running both active/passive scan together.
	- generation separate reports
	- clean scan tree after each run.
- Replacing zapclientapi with restassured.	



-------------------------------DEMO 4-------------------------------------

Agenda-
- Creating contexts, importing contexts, removing contexts.
- Few of the mostly used Active scan parameters.
- Running both active/passive scan together.
	- generation separate reports
	- clean scan tree after each run.
	
Runs the active scanner against the given URL and/or Context
the 'recurse' parameter can be used to scan URLs under the given URL
the parameter 'inScopeOnly' can be used to constrain the scan to URLs that are in scope (ignored if a Context is specified)
the parameter 'scanPolicyName' allows to specify the scan policy (if none is given it uses the default scan policy)
the parameter 'contextId' allows to specify the context against which scan should be triggered.
the parameters 'method' and 'postData' allow to select a given request in conjunction with the given URL.

Things yet to cover
- Spidering for active scan
- Replacing zapclientapi with restassured.	


-------------------------------DEMO 5-------------------------------------

Agenda-
 -- Executing the Spider
 -- Waiting till Spider runs completely 
 -- Passive scan with spider (triggered automatically with spidering)
 -- Active scan with spider
 
 Spidering is the way to automatically discover new resources on any given site and adding those to scan tree.
 
 -- Traditional Spider (Crawler): Use this approach to crawl the HTML resources (hyperlinks etc) in the web application.
 -- Ajax Spider: Use this feature if the application heavily relies with Ajax calls.  


-------------------------------DEMO 6-------------------------------------

Agenda-
1. Understanding the authentication concept in zap
2. Performing a form based authentication and doing an authenticated scan
3. Understanding how to extract any response value from complex apiresponse of zapClient

	Target application might have some sections of application which is only available for logged in users, therefore it is very important to know how to perform authenticated scans in zap.

Types of authentications:-
 > Form-based authentication
 > Script-based authentication
 > JSON-based authentication

general steps when configuring the application authentication with ZAP.
Define a context which
> include the target application inside the context. 
> exclude logout page, password change functionality etc.
> Set the authentication mechanism
> Define auth parameters
> Set up login/logout indicators
> Add valid user and passowrd for login
> Make sure that session management is set as "Cookie based session management"

Running a spider using above context, user, site
Install- docker-https://docs.docker.com/desktop/install/windows-install/
Application for form based login- 
docker run --rm -p 8090:8080 -i -t psiinon/bodgeit


-------------------------------DEMO 7-------------------------------------

Json based authentication-

Site setup->docker run -d -p 3000:3000 bkimminich/juice-shop
Site to visit- http://localhost:3000/

Steps:-
Context creation
- Create & save Session script (to set up bearer token and cookie for further requests)
- Set up Authentication-> Json based authentication
- Set up Session managment- >Script based session managment
- Configure Users


Export context & Use it in automation just like we did it for form based authentication

-------------------------------DEMO 8-------------------------------------

Script based authentication/ OAuth 2 Authorization in zap:-

For OAuth2, To get the Access Token a POST request as querystring
(E.g. clientId=id, clientSecret=secret, tenantId=tenant, grant_type=client_credentials,
email_id=email,password=pwd) to one exposed API which shall generate the token:-  http://BaseURL/TokenEndPoint
The response JSON looks similar to
{
"access_token": "token value",
"token_type": "bearer",
"expires_in": time,
".issued": "date time",
".expires": "expiry date time"
}

Autentication Script based upon -https://github.com/zaproxy/community-scripts/blob/main/authentication/OfflineTokenRefresh.js (Need to modify it as per requirement)
HTTP sender Script-https://github.com/zaproxy/community-scripts/blob/main/httpsender/AddBearerTokenHeader.js

authentication script will automatically fetch the new access token for every unauthorized request determined by the "Logged Out" or "Logged In" indicator set in Context -> Authentication.

httpsender script will add the new access token to all requests in scope made by ZAP (except the authentication ones) as an "Authorization: Bearer [access_token]" HTTP Header.


Steps:-
1) Create an Autentication Script to get the token and store it in global variable.
2) Write an "HTTP sender" Script (and enable it by rigth click)
3) Create a Context with following consitions:-
   Include Urls in Context
   Authentication : set as Script-based Authentication & load your Script (step 1 ), provide the required params, set login and logut indicator.
   Users : create a User & define credentials.
   Session Management : Http Authentication
4) Export context for automation purpose.
5) Enable the HTTP Sender script- http://127.0.0.1:8081/JSON/script/action/enable/
6) Automated scan using the context.

