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
