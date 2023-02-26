package Util;

import org.openqa.selenium.Proxy;
import org.zaproxy.clientapi.core.*;

import java.util.List;
import java.util.stream.Collectors;

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

    public static void addURLToScanTree(String site_to_test) throws ClientApiException {
        clientApi.core.accessUrl(site_to_test, "false");
        if(getUrlsFromScanTree().contains(site_to_test))
            System.out.println(site_to_test+ " has been added to scan tree");
        else
            throw new RuntimeException(site_to_test +" not added to scan tree, active scan will not be possible");
    }

    public static List<String> getUrlsFromScanTree() throws ClientApiException {
        apiResponse=clientApi.core.urls();
        List<ApiResponse> responses=((ApiResponseList)apiResponse).getItems();
        return responses.stream().map(r->((ApiResponseElement)r).getValue()).collect(Collectors.toList());
    }

    public static void performSpidering(String site_to_test, String contextName) throws ClientApiException {
        apiResponse=clientApi.spider.scan(site_to_test,null,null,null,null);
        String spiderScanId=((ApiResponseElement)apiResponse).getValue();

        apiResponse=clientApi.spider.status(spiderScanId);
        String spiderScanStatus=((ApiResponseElement)apiResponse).getValue();

        while (!spiderScanStatus.equals("100")){
            apiResponse=clientApi.spider.status(spiderScanId);
            spiderScanStatus=((ApiResponseElement)apiResponse).getValue();
            System.out.println("Spidering is in progress, current status="+spiderScanStatus);
        }

        waitTillPassiveScanCompleted();

        System.out.println("starting active scan--");
        performActiveScan(site_to_test, contextName);
    }

    public static void performActiveScan(String site_to_test, String contextName) throws ClientApiException {
        String url = site_to_test;
        String recurse = null;
        String inscopeonly = null;
        String scanpolicyname = null;
        String method = null;
        String postdata = null;
        Integer contextId= getContextAfterImporting(contextName);
        System.out.println("context id imported "+ contextId);
        apiResponse = clientApi.ascan.scan(url, recurse, inscopeonly, scanpolicyname, method, postdata, contextId);
        String scanId = ((ApiResponseElement) apiResponse).getValue();

        waitTillActiveScanIsCompleted(scanId);

        apiResponse=clientApi.context.removeContext(contextName);
        if(((ApiResponseElement)apiResponse).getValue().equals("OK"))
            System.out.println("context has been removed");
        else
            throw new RuntimeException("context was not removed after active scan");


    }

    private static Integer getContextAfterImporting(String contextName) throws ClientApiException {
        apiResponse=clientApi.context.importContext(contextName);
        return Integer.parseInt(((ApiResponseElement)apiResponse).getValue());
    }

    private static void waitTillActiveScanIsCompleted(String scanId) throws ClientApiException {
        apiResponse=clientApi.ascan.status(scanId);
        String status=((ApiResponseElement)apiResponse).getValue();

        while (!status.equals("100")){
            apiResponse=clientApi.ascan.status(scanId);
            status=((ApiResponseElement)apiResponse).getValue();
            System.out.println("Active scan is in progress");
        }

        System.out.println("Active scan has completed");
    }


    public static void generateZapReport(String urlToTest, String reportName) {
        String title = "Demo Title";
        String sites = urlToTest;
        String description = "Demo description";

        String template = "traditional-html-plus";
        String sections = "chart|alertcount|passingrules|instancecount|statistics|alertdetails";
        String theme = "dark";

        String includedrisks = "High|Medium|Low";
        String includedconfidences = null;
        String reportfilename = reportName;
        //String reportfilenamepattern = "{{yyyy-MM-dd}}-ZAP-Report-[[site]]";
        String reportdir = System.getProperty("user.dir")+"//reports";
        String display = "true";
        String contexts = null;

        try {
            clientApi.reports.generate(title, template, theme, description, contexts, sites, sections,
                    includedconfidences, includedrisks, reportfilename, "", reportdir, display);
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }

    public static void cleanTheScanTree() throws ClientApiException {
        List<String> urls=getUrlsFromScanTree();
        for (String url:urls){
            if(getUrlsFromScanTree().stream().anyMatch(s->s.contains(url))){
                clientApi.core.deleteSiteNode(url,"","");
            }
        }
        if(getUrlsFromScanTree().size()==0)
            System.out.println("scan tree has been cleared successfully");
        else
            throw new RuntimeException("scan tree was not cleared");

    }
}
