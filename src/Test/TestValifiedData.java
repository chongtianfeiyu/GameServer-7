package Test;

import utils.Json;

// 该类用于测试数据格式校验正确性
public class TestValifiedData {
    static void checkData(String data){
        if (Json.verifiedData(data)){
            System.out.println("格式校验正确");
        }else{
            System.out.println("格式校验错误");
        }
    }
    public static void main(String[] args){
        /* 测试数据集
        *  1、正确的数据集 {"type":"typeValue"}
        *  2.1 错误的数据集 不是json 数据格式 "test failure data"
        *  2.2 错误的数据集 是json 数据，空内容 "{}"
        *  2.3 错误的数据集 是json 没有type标签 "{"notType":"data"}"
        *  2.4 错误的数据集 是json 有type标签 没有数据内容
        *
        * */
        // test1
        String test1 = Json.getLoginSuccessResponse("123456");
        checkData(test1);

        // test2.1
        String test2_1 = "test failure data";
        checkData(test2_1);

        // test2.2
        String test2_2 = "{}";
        checkData(test2_2);

        // test2_3
        String test2_3 = "{\"notType\":\"data\"}";
        checkData(test2_3);

        // test2.4
        String test2_4 = "\"type\":\"\"";
        checkData(test2_4);

        /* 测试通过
        *格式校验正确
        *格式校验错误
        *格式校验错误
        *格式校验错误
        *格式校验错误
        * */
    }
}
