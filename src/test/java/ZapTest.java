import Util.ZapUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ClientApiException;

import java.lang.reflect.Method;

import static Util.ZapUtil.*;

public class ZapTest {

    WebDriver driver;
    private final String urlToTest="http://localhost:8090/bodgeit/";
    private final String contextName="Form_Based_Auth";
    private final String user="Test1";
    //private final String urlToTest="https://ginandjuice.shop/";

    @BeforeMethod
    public void setUp(){
//        ChromeOptions chromeOptions=new ChromeOptions();
//        chromeOptions.setProxy(proxy);
//        chromeOptions.setAcceptInsecureCerts(true);
//
//        WebDriverManager.chromedriver().setup();
//        driver=new ChromeDriver(chromeOptions);
    }

//    @Test
//    public void testPassiveScan(){
//        driver.get(urlToTest);
//        waitTillPassiveScanCompleted();
//    }
//
//    @Test
//    public void testActiveScan() throws ClientApiException {
//        addURLToScanTree(urlToTest);
//        performActiveScan(urlToTest, contextName);
//    }

//    @Test
//    public void testSpider() throws ClientApiException {
//        performSpidering(urlToTest,contextName);
//    }

    @Test
    public void testSpiderAsFormBasedUser() throws ClientApiException {
        performSpideringAsUser(urlToTest,contextName,user);
    }

    @AfterMethod
    public void tearDown(Method method) throws ClientApiException {
        generateZapReport(urlToTest,method.getName().replace("test",""));
        cleanTheScanTree();
        driver.quit();
    }

}
