package utils;

import JsonData.LoginJson;
import JsonData.ResponseJson.NormalResponseJson;
import JsonData.ResponseType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Json {
    private static ObjectMapper mapper = new ObjectMapper();
    public Json(){}
    public static String getType(String data){
        String type ;
        try{
            Map<String,Object> map = mapper.readValue(data,new TypeReference<Map<String,Object>>(){});
            type = map.get("type").toString();
        }catch (IOException e){
            e.printStackTrace();
            type="ERROR";
        }
            return type;
    }
    // 获得登陆的object
    public static LoginJson getLoginObject(String data) throws Exception {
        return mapper.readValue(data,LoginJson.class);
    }
    // 获得常规的json
    private static String getNormalJson(String type,String value){
        String re;
        NormalResponseJson normalResponseJson = new NormalResponseJson();
        normalResponseJson.setType(type);
        normalResponseJson.setValue(value);
        try{
            re =mapper.writeValueAsString(normalResponseJson);
        }catch (JsonProcessingException e){
           System.out.println("object->json Error");
           re="ERROR";
            e.printStackTrace();
        }
        return re;
    }
    // 获得json的map
    public static Map getJsonMap(String value) throws IOException{
            return mapper.readValue(value, new TypeReference<Map<String, Object>>(){});
    }
    // 获得登入成功的字符串
    public static String getLoginSuccessResponse(String account){
        String type = ResponseType.LOGINSUCCESS;
        return "{\"type\":\""+type+"\"," +
                "\"value\":\""+account+"\"}";
    }

    // 获得登陆失败的字符串
    public static String getLoginFailureResponse(String status){
        String type = ResponseType.LOGINFAILURE;
        return "{\"type\":\""+type+"\"," +
                "\"value\":\""+status+"\"}";
    }

    // 获得登陆成功字符串
    public static String getMatchSuccessResponse(String opponentId){
        String type = ResponseType.MATCHSUCCESS;
        return getNormalJson(type,opponentId);
    }

    // 获得游戏结束字符串
    public static String getGameOverResponse(String gameOverType){
        String type = ResponseType.GAMEOVER;
        return getNormalJson(type,gameOverType);
    }

    // 校验格式是否符合规范
    public static boolean verifiedData(String data){
       boolean status = true;
       try {
           Map map = mapper.readValue(data,new TypeReference<Map<String,Object>>(){});
           String type = map.get("type").toString();
           if (type == null){
               status = false;
           }
       }catch (IOException e){
           status = false;
       }catch (NullPointerException e){
           status = false; // 空字符串的传值
       }
       return status; // 返回判断的
    }
}
