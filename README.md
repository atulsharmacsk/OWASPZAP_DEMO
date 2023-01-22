--------------------------------------Demo part 1---------------------------------------------------

# OWASPZAP_DEMO
OWASP ZAP automation using java, selenium, zapClient, TestNG and Maven



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
		


--------------------------------------Demo part 2---------------------------------------------------
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
