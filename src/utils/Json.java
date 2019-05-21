package utils;

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
}
