package Util;

import org.openqa.selenium.Proxy;
import org.zaproxy.clientapi.core.*;

public class ZapUtil {

    private static ClientApi clientApi;
    public static Proxy proxy;
    private static ApiResponse apiResponse;

    private static final String zapAddress = "127.0.0.1";
    private static final int zapPort = 8080;
    private static final String apiKey = Enter_API_KEY from ZAP; //Please add your own api key from ZAP


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
        String template = "traditional-html";
        String theme = null;
        String description = "Demo description";
        String contexts = null;
        String sites = site_to_test;
        String sections = null;
        String includedconfidences = null;
        String includedrisks = null;
        String reportfilename = "Demofilename";
        String reportfilenamepattern = null;
        String reportdir = System.getProperty("user.dir");
        String display = null;

        try {
            clientApi.reports.generate(title, template, theme, description, contexts, sites, sections,
                    includedconfidences, includedrisks, reportfilename, reportfilenamepattern, reportdir, display);
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }
}
