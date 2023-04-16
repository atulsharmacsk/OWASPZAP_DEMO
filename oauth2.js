var HttpRequestHeader = Java.type("org.parosproxy.paros.network.HttpRequestHeader");
var HttpHeader = Java.type("org.parosproxy.paros.network.HttpHeader");
var URI = Java.type("org.apache.commons.httpclient.URI");
var ScriptVars = Java.type('org.zaproxy.zap.extension.script.ScriptVars');


function authenticate(helper, paramsValues, credentials) {

  var token_endpoint = paramsValues.get("apiUrl");
  var client_id = paramsValues.get("clientId");
  var client_secret = paramsValues.getParam("clientSecret");
  var tenant_id = paramsValues.get("tenantId");
  var grant_type = paramsValues.get("grantType");
  var emailId= credentials.get("emailid");
  var password= credentials.get("password");
  
  
  // Build body
  var requestbody = "client_id=" + client_id;
   requestbody+= "&client_secret=" + client_secret;
   requestbody+= "&tenant_id=" + tenant_id;
   requestbody+= "&grant_type=" + grant_type;
   requestbody+= "&emailId=" + emailId;
   requestbody+= "&password=" + password;

  // Build header
  var tokenRequestURI = new URI(token_endpoint, false);
  var tokenRequestMethod = HttpRequestHeader.POST;
  var tokenRequestMainHeader = new HttpRequestHeader(tokenRequestMethod, tokenRequestURI, HttpHeader.HTTP11);

  // Build message
  var tokenMsg = helper.prepareMessage();
  tokenMsg.setRequestBody(refreshTokenBody);
  tokenMsg.setRequestHeader(tokenRequestMainHeader);
  tokenMsg.getRequestHeader().setContentLength(tokenMsg.getRequestBody().length());

  // Make the request and receive the response
  helper.sendAndReceive(tokenMsg, false);

  // Parse the JSON response and save the new access_token in a global var
  // we will replace the Authentication header in AddBearerTokenHeader.js
  var json = JSON.parse(tokenMsg.getResponseBody().toString());
  var access_token = json['access_token'];

  if (access_token){
    ScriptVars.setGlobalVar("access_token", access_token);
  }else{
    print("Error getting access token")
  }

  return tokenMsg;
}


function getRequiredParamsNames(){
  return ["apiUrl","clientId","clientSecret","tenantId","grantType"];
}


function getOptionalParamsNames(){
  return [];
}


function getCredentialsParamsNames(){
  return ["emailid","password"];
}
