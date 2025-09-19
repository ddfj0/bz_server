package ddfj0.few;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByIdRequest;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class JsapiNative {
    
    private String mchId = "1670810409"; 

    // 商户API证书序列号
    private String mchSerialNo = "4DDEBB6F2753A254C5A11DFE6C770D89BF3D2F1F";

    // 商户私钥文件
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key = "wwwddfj0cn2018122520190130fjfjfj";

    // APPID
    private String appid = "wx95c349d43f1db2be";
    
    // 接收支付结果通知地址
    private String notifyUrl = "https://www.ddfj0.cn/bz/api/wepay/notify";
    
    // 接收退款结果通知地址
    //private String notifyRefundUrl = "https://www.ddfj0.cn/bz/api/wepay/notifyRefund";
    

    private NativePayService service;

    public JsapiNative() {
        this.privateKeyPath = MainApplication.m_curPath + "/cert/apiclient_key.pem";
        service = null;
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
            service = new NativePayService.Builder().config(config).build();
            System.out.println("fnStartService() : native 启动微信支付服务成功！ ");
        } catch (Exception ee) {
            System.out.println("fnStartService() error: " + ee.getMessage());
        }
    }
    
    public String fnWePayPrePay(int total, String outTradeNo, String description, int iamount) {
        String rtn = "";
        try {
            // request.setXxx(val)设置所需参数，具体参数可见Request定义
            com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request = new PrepayRequest();
            Amount amount = new Amount();
            amount.setTotal(iamount);
            request.setAmount(amount);
            request.setAppid(appid);
            request.setMchid(mchId);
            request.setDescription(description);
            request.setNotifyUrl(notifyUrl);
            request.setOutTradeNo(outTradeNo);

            // 调用下单方法，得到应答
            PrepayResponse response = service.prepay(request);
            rtn = response.getCodeUrl();

        } catch (Exception ee) {
            System.out.println("fnWePayPrePay() error: " + ee.getMessage());
        }
        return rtn;
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
        queryRequest.setTransactionId("4200001569202208304701234567");
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
}
