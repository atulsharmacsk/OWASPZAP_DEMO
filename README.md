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