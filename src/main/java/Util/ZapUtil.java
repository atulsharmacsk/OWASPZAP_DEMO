package Util;

import org.openqa.selenium.Proxy;
import org.zaproxy.clientapi.core.*;

public class ZapUtil {

    private static ClientApi clientApi;
    public static Proxy proxy;
    private static ApiResponse apiResponse;

    private static final String zapAddress = "127.0.0.1";
    private static final int zapPort = 8080;
    private static final String apiKey = System.getProperty("APIKEY");


    static {
        clientApi = new ClientApi(zapAddress, zapPort, apiKey);
        proxy = new Proxy().setSslProxy(zapAddress + ":" + zapPort).setHttpProxy(zapAddress + ":" + zapPort);
    }

    public static void waitTillPassiveScanCompleted() {
        try {
            apiResponse = clientApi.pscan.recordsToScan();
            String tempVal = ((ApiResponseElement) apiResponse).getValue();
            while (!tempVal.equals("0")) {
                System.out.println("passive scan is in progress");
                apiResponse = clientApi.pscan.recordsToScan();
                tempVal = ((ApiResponseElement) apiResponse).getValue();
            }
            System.out.println("passive scan is completed");
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }


    public static void generateZapReport(String site_to_test) {
        String title = "Demo Title";
        String sites = site_to_test;
        String description = "Demo description";

        String template = "traditional-html-plus";
        String sections = "chart|alertcount|passingrules|instancecount|statistics|alertdetails";
        String theme = "dark";

        String includedrisks = "High|Medium|Low";
        String includedconfidences = null;
        String reportfilename = null;
        String reportfilenamepattern = "{{yyyy-MM-dd}}-ZAP-Report-[[site]]";
        String reportdir = System.getProperty("user.dir")+"//reports";
        String display = "true";
        String contexts = "Atul";

        try {
            clientApi.reports.generate(title, template, theme, description, contexts, sites, sections,
                    includedconfidences, includedrisks, reportfilename, reportfilenamepattern, reportdir, display);
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }
}
