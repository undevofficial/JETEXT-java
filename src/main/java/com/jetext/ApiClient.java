package com.jetext;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.jetext.exceptions.InvalidRequestException;
import com.jetext.exceptions.jetextException;

public class ApiClient {

  private String authkey;
  private static final String API_BASE_PATH = "http://control.jetext.com";
  private MessageFactory messageFactory;

  public String getAuthkey() {
    return authkey;
  }

  public ApiClient(String authkey) {
    this.authkey = authkey;
    this.messageFactory = MessageFactory.getInstance(this);
  }

  public Long getRouteBalance(MessageRoute route) throws jetextException {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("type", route.getValue());
    ApiResponse response = this.request("balance.php", parameters);

    if (response.isRaw()) {
      return new Long(response.getRaw());
    }

    throw new InvalidRequestException(response.getJson().getObject().getString("msg"));
  }

  public Boolean isValid() throws jetextException {
    ApiResponse response = this.request("validate.php");

    if (response.isJson()) {
      JSONObject responseObj = response.getJson().getObject();
      return "Valid".equals(responseObj.getString("message"));
    }

    throw new InvalidRequestException(response.getRaw());
  }

  public ApiResponse request(String endpoint) throws jetextException {
    Map<String, Object> parameters = new HashMap<String, Object>();
    return this.request(endpoint, parameters);
  }

  public ApiResponse request(String endpoint, Map<String, Object> parameters) throws jetextException {
    String url = API_BASE_PATH + "/" + endpoint;

    try {
      HttpResponse<String> response = Unirest.get(url).queryString("response", "json")
          .queryString("authkey", this.getAuthkey()).queryString(parameters).asString();

      return new ApiResponse(response.getBody());
    } catch (UnirestException e) {
      throw new jetextException(e.getMessage());
    }
  }

  public MessageFactory messages() {
    return this.messageFactory;
  }

  public static String version() {
    return "0.0.1-SNAPSHOT";
  }

}
