package net.nurigo.mavenspringdemo;

import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
//애가 메세지 출력이 되는 코드임!!
/**
 * @class ExampleSend
 * @brief This sample code demonstrate how to send sms through CoolSMS Rest API PHP
 * 출처 : https://developer.coolsms.co.kr/JAVA_SDK_EXAMPLE_Message
 */
public class ExampleSend {
  public static void main(String[] args) {
    String api_key = "NCSG8PMPOQOHDYSW";
    String api_secret = "KQ9PN9DA3GVYI2SWZGW4AM9HFP3C9AWT";
    Message coolsms = new Message(api_key, api_secret);

    // 4 params(to, from, type, text) are mandatory. must be filled
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("to", "01067226215");	// 수신전화번호
    params.put("from", "01067226215");	// 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
    params.put("type", "SMS");
    params.put("text", "첫번째 보내는 테스트 문자 메시지!");
    params.put("app_version", "test app 1.2"); // application name and version

    try {
      JSONObject obj = (JSONObject) coolsms.send(params);
      System.out.println(obj.toString());
    } catch (CoolsmsException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getCode());
    }
  }
}