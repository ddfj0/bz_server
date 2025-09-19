package ddfj0.few;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.payments.h5.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.h5.H5Service;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class JsapiH5 {
    
    private String mchId = "1670810409"; 

    // 商户API证书序列号
    private String mchSerialNo = "4DDEBB6F2753A254C5A11DFE6C770D89BF3D2F1F";

    // 商户私钥文件
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key = "wwwddfj0cn2018122520190130fjfjfj";

    
    // 接收支付结果通知地址
    private String notifyUrl = "https://www.ddfj0.cn/bz/api/user/pay/mp/notify";
    
    // 接收退款结果通知地址
    //private String notifyRefundUrl = "https://www.ddfj0.cn/bz/api/user/pay/mp/notifyRefund";
    
    public ArrayList<WechatAppId> m_appIdList = new ArrayList<WechatAppId>();

    private H5Service service;

    public JsapiH5() {
        this.privateKeyPath = MainApplication.m_curPath + "/cert/apiclient_key.pem";
        service = null;

        WechatAppId app = new WechatAppId();
        app.appid = "wx606192470a81b9c7";   /// 个体户小程序
        app.secret = "efbb85b786e306abbc36c60d1db70093";
        m_appIdList.add(app);

        app = new WechatAppId();
        app.appid = "wx82b943c4e3f6354a";  /// 测试小程序
        app.secret = "ad3d60a0a083600230811f5134ca5445";
        m_appIdList.add(app);        

        app = new WechatAppId();
        app.appid = "wx95c349d43f1db2be";  /// 公众号
        app.secret = "15bdda643e28b193ba039add46287b2a";
        m_appIdList.add(app);        
        
    }
    

    public void fnStartService() {
        try {
            // 初始化配置
            Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(mchId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(mchSerialNo)
                        .apiV3Key(apiV3Key)
                        .build();

            // 创建支付服务
            service = new H5Service.Builder().config(config).build();
            System.out.println("fnStartService() : 启动微信支付服务成功！ ");
        } catch (Exception ee) {
            System.out.println("fnStartService() error: " + ee.getMessage());
        }
    }
    
    public TreeMap<String, String> fnWePayPrePayMp(String appid, String outTradeNo, String description, Integer iAmount) {

        TreeMap<String, String> params = new TreeMap<>();

        try {
            PrepayRequest request = new PrepayRequest();
            Amount amount = new Amount();
            amount.setTotal(iAmount);

            request.setAmount(amount);
            request.setAppid(appid);
            request.setMchid(mchId);
            request.setDescription(description);
            request.setNotifyUrl(notifyUrl);
            request.setOutTradeNo(outTradeNo);
    
            PrepayResponse response = service.prepay(request);
    
            WechatPaySign sign = sign( appid, response.getH5Url() );
            params.put("trans_no", outTradeNo);// 订单号(业务需要）
            params.put("appId", appid);
            params.put("nonceStr", sign.nonceStr);
            params.put("package", "prepay_id=" + sign.prepayId);
            params.put("signType", "RSA");
            params.put("timeStamp", sign.timeStamp);
            params.put("paySign", sign.sign);
    
        } catch (Exception ee) {
            System.out.println("fnWePayPrePay() error: " + ee.getMessage());
        }

        return params;
    }

    public void fnWePayCloseOrder(String outTradeNo) {
        try {
            CloseOrderRequest closeRequest = new CloseOrderRequest();
            closeRequest.setMchid(mchId);
            closeRequest.setOutTradeNo(outTradeNo);
            // 方法没有返回值，意味着成功时API返回204 No Content
            service.closeOrder(closeRequest);            
        } catch (Exception ee) {
            System.out.println("fnWePayCloseOrder() error: " + ee.getMessage());
        }
    }

    public String fnWePayQueryOrder(String outTradeNo) {
        String rtn = "";
    
        QueryOrderByIdRequest queryRequest = new QueryOrderByIdRequest();
        queryRequest.setMchid(mchId);
        queryRequest.setTransactionId(outTradeNo);
        try {
            Transaction result = service.queryOrderById(queryRequest);
            rtn = result.getTradeState().toString();
        } catch (ServiceException e) {
            // API返回失败, 例如ORDER_NOT_EXISTS
            System.out.printf("code=[%s], message=[%s]\n", e.getErrorCode(), e.getErrorMessage());
            System.out.printf("reponse body=[%s]\n", e.getResponseBody());
        }        
        return rtn;
    }

    public void fnWePayResultNotify(String body) {
        try {

            JSONObject obj = JSON.parseObject(body);
            String requestId = obj.getString("id");
            System.out.println("支付通知的ID requestId: " + requestId);


        } catch (ServiceException e) {
            // 处理异常
            System.out.printf("code=[%s], message=[%s]\n", e.getErrorCode(), e.getErrorMessage());
            System.out.printf("reponse body=[%s]\n", e.getResponseBody());
        }
    }

    public String fnWePayNotify(String body) {
        String rtn = "";
        int TAG_LENGTH_BIT = 128;
        String associated_data="";
        String nonce="";
        String ciphertext="";
        try {

			JSONObject jsonRtn = JSON.parseObject(body);
            String resource = jsonRtn.getString("resource");

			JSONObject jsonRes = JSON.parseObject(resource);
			associated_data = jsonRes.getString("associated_data");
            nonce = jsonRes.getString("nonce");
            ciphertext = jsonRes.getString("ciphertext");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");


            SecretKeySpec key = new SecretKeySpec(apiV3Key.getBytes(), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes());
      
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associated_data.getBytes());
      
            rtn = new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");

        } catch (Exception e) {
            // 处理异常
            System.out.printf(" fnWePayNotify =[%s]\n", e.getMessage());
        }
        return rtn;
    }

    public WechatPaySign sign(String appid, String prepayId)  {
        
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String packageStr = "prepay_id=" + prepayId;
        String signStrBase64 = "";
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int ii = 0 ; ii < 30 ; ii++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        String nonceStr = sb.toString();//随机字符串

        // 不能去除'.append("\n")'，否则失败
        String signStr = appid + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n" +
                packageStr + "\n";

        byte[] message = signStr.getBytes(StandardCharsets.UTF_8);

        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            PrivateKey pk = PemUtil.loadPrivateKeyFromPath(privateKeyPath);
            sign.initSign(pk);
            sign.update(message);
            signStrBase64 = Base64.getEncoder().encodeToString(sign.sign());    
        }
        catch(Exception ee) {
            System.out.println("sign() error: " + ee.getMessage());
            return null;
        }

        WechatPaySign wechatPaySign = new WechatPaySign();
        wechatPaySign.prepayId = prepayId;
        wechatPaySign.timeStamp = timeStamp;
        wechatPaySign.nonceStr = nonceStr;
        wechatPaySign.packageStr = packageStr;
        wechatPaySign.sign = signStrBase64;
        return wechatPaySign;
    }

    public String fnGetSupportAppidSecret( String appid ) {
        String strRtn = "";
        if( appid == null || appid.length() == 0 ) {
            return strRtn;
        }
        for(WechatAppId app : m_appIdList) {
            if( app.appid.equals(appid) ) {
                strRtn = app.secret;
                break;
            }
        }
        return strRtn;
    }

    public boolean fnIsSupportAppid( String appid ) {
        if( appid == null || appid.length() == 0 ) {
            return false;
        }
        for(WechatAppId app : m_appIdList) {
            if( app.appid.equals(appid) ) {
                return true;
            }
        }
        return false;
    }

    public String fnWePayOpenId(String appid, String secret, String code ) {
        String strRtn = "";
        try {

            String url = "https://api.weixin.qq.com/sns/jscode2session";
            url += "?appid=" + appid;//自己的appid
            url += "&secret=" + secret;//自己的appSecret
            url += "&js_code=" + code;
            url += "&grant_type=authorization_code";
            url += "&connect_redirect=1";


            strRtn = HttpClientUtils.doGet( url );
            if(strRtn != null) {
                System.out.println("openid: " + strRtn);
                JSONObject jo = JSON.parseObject(strRtn);
                strRtn = jo.getString("openid");
                if( strRtn != null && !strRtn.isEmpty() ) {
                    MainApplication.m_WechatOpenId.add(new WechatOpenId(strRtn, new Date()));
                }
            }
            else {
                strRtn = "";
            }
        } catch (Exception ee) {
            strRtn = "";
            System.out.println("fnGetOpenId() error: " + ee.getMessage());
        }
        return strRtn;
    }
}



