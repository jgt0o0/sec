import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ji on 16-6-12.
 */
public class UrlRequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlRequestUtil.class);
    private static HttpClient httpClient = HttpClientBuilder.create().build();

    public static String sendPost(String url, Map<String, String> param) {
        String body = null;
        try {
            //设置客户端编码
            if (httpClient == null) {
                httpClient = new DefaultHttpClient();
            }
            // Post请求
            HttpPost httppost = new HttpPost(url);

            httppost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

            // 设置参数
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            //封装参数
            Set<String> keys = param.keySet();

            for (String key : keys) {
                String value = param.get(key);
                NameValuePair item = new BasicNameValuePair(key, value);
                data.add(item);
            }

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data, "UTF-8");

            httppost.setEntity(formEntity);

            // 发送请求
            HttpResponse httpresponse = httpClient.execute(httppost);

            if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                LOGGER.debug("请求成功！");
                HttpEntity entity = httpresponse.getEntity();
                body = EntityUtils.toString(entity, "utf-8");
                if (entity != null) {
                    entity.consumeContent();
                }
            } else {
                LOGGER.error("请求错误代码：" + httpresponse.getStatusLine().getStatusCode());
                LOGGER.info("请求错误信息：" + EntityUtils.toString(httpresponse.getEntity(), "utf-8"));
            }
        } catch (Exception e) {
            LOGGER.error("发送请求失败!", e);
        }
        return body;
    }
}
