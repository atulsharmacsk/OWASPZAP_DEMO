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
    private final String urlToTest="http://127.0.0.1:8080";
    //private final String urlToTest="https://ginandjuice.shop/";

    @BeforeMethod
    public void setUp(){
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.setProxy(proxy);
        chromeOptions.setAcceptInsecureCerts(true);

        WebDriverManager.chromedriver().setup();
        driver=new ChromeDriver(chromeOptions);
    }

//    @Test
//    public void testPassiveScan(){
//        driver.get(urlToTest);
//        waitTillPassiveScanCompleted();
//    }

    @Test
    public void testActiveScan() throws ClientApiException {
        addURLToScanTree(urlToTest);
        performActiveScan(urlToTest);
    }

    @AfterMethod
    public void tearDown(Method method){
        generateZapReport(urlToTest);
        driver.quit();
    }

}
