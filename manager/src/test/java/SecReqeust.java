import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 16-6-12.
 */
public class SecReqeust {
    private static final String BASE_URL = "http://localhost:8080/api/";
    private static Thread heartBeatThread = null;

    public static void main(String[] args) {
        if (heartBeatThread == null || !heartBeatThread.isAlive()) {
            heartBeatThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String response = register("jgt", "jgt123");
                            System.out.println(response);
                            register("wh", "jgt123");
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
//            heartBeatThread.start();
        }


//        authProcess("jgt", "zq", false);

        milinare();
    }

    private static String milinare() {
        String url = BASE_URL + "millionaire";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=lh&ID=0&Path=xsq-zq-wh&Stage=lh-xsq&Step=0\",\"receiver\":\"lh\",\"sender\":\"lh\"}";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=xsq&ID=15544c099cc&Path=xsq-zq-wh&Stage=lh-xsq&Step=1&kj1=689803b5961a71f4a11f23ccd9111176c55ef0170175306cc20438ff244ae739\",\"receiver\":\"xsq\",\"sender\":\"lh\"}";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=lh&ID=0&Path=zq-wh&Stage=lh-zq&Step=0\",\"receiver\":\"lh\",\"sender\":\"lh\"}";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=zq&ID=15544c0a9ef&Path=zq-wh&Stage=lh-zq&Step=1&kj1=229c5a7a24afac4e953b460b97d884bb03400d9ee6afc3069d282f41078cf5c\",\"receiver\":\"zq\",\"sender\":\"lh\"}";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=lh&ID=0&Path=wh&Stage=lh-wh&Step=0\",\"receiver\":\"lh\",\"sender\":\"lh\"}";
//        String msg = "{\"msg\":\"Protocol=Millionaire&Sender=lh&Receiver=wh&ID=15544c0c297&Path=wh&Stage=lh-wh&Step=1&kj1=39a2bd73bf651c175503392ec3eb68086a44d18f7ba54f5a178fdce23e1421cc\",\"receiver\":\"wh\",\"sender\":\"lh\"}";
        String msg = "{\"msg\":\"lh\",\"receiver\":\"all\",\"sender\":\"lh\"}";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", msg);

        String response = UrlRequestUtil.sendPost(url, paramMap);

        System.out.println(response);
        return response;
    }

    private static String register(String name, Object key) {
        String url = BASE_URL + "register";
        JSONObject param = new JSONObject();
        param.put("name", name);
        param.put("key", key);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", param.toJSONString());

        String response = UrlRequestUtil.sendPost(url, paramMap);

        return response;
    }

    private static void authProcess(String sender, String receiver, boolean end) {
        String rs2 = auth(sender, receiver);
        System.out.println(rs2);

        String rs3 = getAuthRequest(receiver);
        System.out.println(rs3);

        String s4 = authSucess(receiver, sender);
        System.out.println(s4);

        if (!end) {
            authProcess(receiver, sender, true);
        }

    }

    private static String auth(String sender, String receiver) {
        String url = BASE_URL + "auth";
        JSONObject param = new JSONObject();
        param.put("sender", sender);
        param.put("receiver", receiver);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", param.toJSONString());

        String response = UrlRequestUtil.sendPost(url, paramMap);

        return response;
    }

    private static String getAuthRequest(String name) {
        String url = BASE_URL + "getAuthRequest";
        JSONObject param = new JSONObject();
        param.put("name", name);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", param.toJSONString());

        String response = UrlRequestUtil.sendPost(url, paramMap);

        return response;
    }

    private static String authSucess(String source, String target) {

        String url = BASE_URL + "authSuccess";
        JSONObject param = new JSONObject();
        param.put("source", source);
        param.put("target", target);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", param.toJSONString());

        String response = UrlRequestUtil.sendPost(url, paramMap);

        return response;
    }
}
