package ddfj0.few;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
 
public class HttpClientUtils {

	public static String doGet(String url) {
		return doGet(url, null, null);
	}
 
	public static String doGet(String url, Map<String, String> param, Map<String,String> headers) {
		PoolingHttpClientConnectionManager httpCliConnMgr = getHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(httpCliConnMgr).build();
		URI uri = null;
		try {

            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            builder.setCharset(Charset.forName("utf-8"));
            uri = builder.build();            
		} catch (URISyntaxException e) {
			System.out.println("doGet1: " + e.getMessage());
			//e.printStackTrace();
			return null;
		}
        HttpGet httpGet = new HttpGet(uri);
        if(headers!= null && !headers.isEmpty()){
            headers.forEach((name,value)->httpGet.setHeader(name,value));
        }
    
		RequestConfig config = getRequestConfig();
		// 设置请求配置信息
		httpGet.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				return result;
			}
		} catch (ClientProtocolException e) {
			//System.out.println("doGet2: " + e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("doGet3: " + e.getMessage());
			//e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				System.out.println("doGet4: " + e.getMessage());
				//e.printStackTrace();
			}
		}
		return null;
	}
  
	public static String doPost(String url, Map<String, String> param, Map<String,String> headers) {

		PoolingHttpClientConnectionManager httpCliConnMgr = getHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(httpCliConnMgr).build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = getRequestConfig();
        try {      
            // 设置请求配置信息
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Charset.forName("UTF-8"));
                httpPost.setEntity(entity);
            }

            if(headers!= null && !headers.isEmpty()){
                headers.forEach((name,value)->httpPost.setHeader(name,value));
            }

            httpPost.setConfig(config);
        }
        catch (Exception e) {
			System.out.println("doPost1: " + e.getMessage());
            //e.printStackTrace();
            return null;
        }

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				return result;
			}
		} catch (ClientProtocolException e) {
			System.out.println("doPost2: " + e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("doPost3: " + e.getMessage());
			//e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				System.out.println("doPost4: " + e.getMessage());
				//e.printStackTrace();
			}
		}
		return null;
	}

	public static String doPostJson(String url, String paraJson) {

		PoolingHttpClientConnectionManager httpCliConnMgr = getHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(httpCliConnMgr).build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = getRequestConfig();
        try {      
            // 设置请求配置信息
            StringEntity entity = new StringEntity(paraJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            httpPost.setConfig(config);
        }
        catch (Exception e) {
			System.out.println("doPostJson1: " + e.getMessage());
            //e.printStackTrace();
            return null;
        }
        
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				return result;
			}
		} catch (ClientProtocolException e) {
			System.out.println("doPostJson2: " + e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("doPostJson3: " + e.getMessage());
			//e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				System.out.println("doPostJson4: " + e.getMessage());
				//e.printStackTrace();
			}
		}
		return null;
	}
    
    //// HttpClient连接管理者
	public static PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager httpCliConnMgr = new PoolingHttpClientConnectionManager();
		httpCliConnMgr.setMaxTotal(200); // 设置最大连接数
		httpCliConnMgr.setDefaultMaxPerRoute(20); // 设置每个主机地址的并发数
		return httpCliConnMgr;
	}
 
	//// 获取请求配置信息
	public static RequestConfig getRequestConfig() { 	// 构建请求配置信息
		RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
				.setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
				.setSocketTimeout(10 * 1000) // 数据传输的最长时间
				.build();
		return config;
	}

}