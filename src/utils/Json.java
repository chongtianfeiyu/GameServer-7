package utils;

import JsonData.LoginJson;
import JsonData.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Json {
    private static ObjectMapper mapper = new ObjectMapper();
    public Json(){}
    public static String getType(String data){
        String type = "ERROR";
        try{
            Map<String,Object> map = mapper.readValue(data,new TypeReference<Map<String,Object>>(){});
            type = map.get("type").toString();
        }catch (IOException e){e.printStackTrace();}
        return type;
    }
    // 获得登陆的object
    public static LoginJson getLoginObject(String data) throws Exception {
        return mapper.readValue(data,LoginJson.class);
    }
    // 获得登入成功的字符串
    public static String getLoginSuccessResponse(String account){
        String type = ResponseType.LOGINSUCCESS;
        return "{\"type\":\""+type+"\"," +
                "\"value\":\""+account+"\"}";
    }

    // 获得登陆失败的字符串
    public static String getLoginFailureResponse(String status){
        String type = ResponseType.LOGINFAILUE;
        return "{\"type\":\""+type+"\"," +
                "\"value\":\""+status+"\"}";
    }
}
