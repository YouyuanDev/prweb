package com.prweb.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AliPayService {

    //APPID
    static final String app_id = "2018080160826887";

    //网页端API appid
    static final String web_app_id = "2018080360977050";



    //partner ID
    static final String pid = "2088231183736857";

    //商家id
    static final String seller_id = "2088231183736857";

    //回调服务器地址
    static final String notify_url="http://116.62.17.42:9090/Order/AliPayNotify.action";

    //RSA(SHA1)密钥
    static final String rsa_sha1_private_key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMY0B8ECj+CqpEGE" +
            "feHBPzbJMozbDGbgsPenFDVv1vdBdIJSO2uJGJiSws2jbpAyhPOP3vlz+OUesMJj" +
            "SPpUFhNgyLYbi3ZxZYkfSR2DpjJtxO19AK7bhASNvgLAQ+tAUpX68oYhvWJMoZkR" +
            "PnwizBWt5OjSqg6q9zJEUZvF6mvFAgMBAAECgYEAvbLsvPF7zVblMQPjC6BOpVv5" +
            "8tBwVUK2fcE+2Uo6n5nZLiYQUwL7NIQ6yGCkoISJtSe9DFnQHVK7cMFgBp9WI/LI" +
            "0KQN15W5m37kgs6f3xSpLL+DIUZFgp0PAdjHIVQFTHzQtqlqtBuUt0zWOw8H0phK" +
            "FUhGt9ph+aWCwS+dQ10CQQD3XacyTMkIqfFwl6rDWUvAcvvCn8Ydx+bo9bFlop0w" +
            "ZHETDrTF8fXqSPslsObWE6Ke2LR9aCiPgZvBS934LUojAkEAzR8VPeOhBBU3lPbt" +
            "/WHu6j6rr5ii3QsdMk2poz2gcVtOCF4+Kmrfs0mQxJUjXFtCJuqd9K9+YWOfNQPH" +
            "kIzM9wJAOqx7MSIneSeePj+rYIS4xCBB+UsemgfFjLJibqIguM7KR2PwmxcmZnkD" +
            "IVCRoGut4hGy1iAzpr5ubG4La4eKkwJBAIokeeDRcbpXM7SuTH+OOiImDOhYqq6l" +
            "codTSID42xocvMPj/IgCrB+J1uad7k+f4kBlV/XXTwz1094XaoqOWOcCQFgGJPyo" +
            "MlVJL6Kl+EZ7HN6w+wbi4Oe20LIdroIwtrB8+vcylsQVi4Li/D6hY49ZWRW6uDo5" +
            "GM8Brhe+XIae5rc=";
    //RSA(SHA1)支付宝公钥
    static final String rsa_sha1_ali_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    public static final String  SIGN_ALGORITHMS_RSA_SHA1 = "SHA1WithRSA";


    //RSA(SHA256)密钥
    static final String rsa_sha256_private_key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDcEvQVujj48FUA\n" +
            "+xNgGhPYHv7WOYnsK3v5idkKXUc3k9vCPrt7NPw7BgCWewNfNteLLULygRyWlpcu" +
            "4GtBBSB7eTEqh64a8tdqJL5lxtGy9DAhdMcPFnc9s2ijGu5TQ8FOCep9kDZbVfP3" +
            "IylxQJUHLRGlZ6FEnKE9KG5THD1yRgCTqNuCZegSxVJRxMX6ba5YizK4MpvxDwlJ" +
            "x4GuPIi1iyQfImTwZ8mV1T/vq2j5FVm9PxmZPjZ/DT7owTrgS3dlTZEel5tfUQAp" +
            "AxcR62wuTemhvWPhxugBB45n/EVLaHQg6Z+mMy2E7x0KtHDRjsAOX4P6EjQCIHuT" +
            "Q2d5Vo3/AgMBAAECggEBAKpdidMgHGaqNkPSGke1KFOQexr/5AOODiU5T2m0mCMw" +
            "NVmEEsLoW7KdjOrxQ8T7wZSeb0soJCd3YIYLeTwWafTr9uHDs0ZtY9FprHNBnKh6" +
            "XKAptMe7TofXUVOiz4hp1UVCa+LUNyAxw/E3qSdhJX0uqJBxTui8kB22JOH15KOG" +
            "mfdkoHVq6B1Vk9Qlfe4aAWEI/2RsF4mH0uB3vuv8cy5lvEWE8xqCFAh7sGs5h+kC" +
            "0AdSic2AYMu5I2ZYWeVgszNV08bHjej/TbnQpVKLQzb/+X93sdcLaLuPbqQxkjsu" +
            "6HZAuNTWFLJPXHWDkQG1BYVUjJTPxY2Ed8K3rDwAVcECgYEA8LBnPQRDqJYJQUhG" +
            "cV1l+VT3nnT2BmV+2jZ+w6gOiSLUqT5P3wwkuYI739kTKwjKGmHNNYFPy6cTaRZ3" +
            "20El2du8n4r9yoHy5t4LBB2iDyGpm4evLzkNTj6HVYoU9/j1FXp//YF+M97t1bM3" +
            "Tc/CKFizKFUkck7fSVimXzomYW8CgYEA6hLWHxfLpvUxSaIyW9IvKKQizPEAMdmE" +
            "ElUPSEirAA2gfx7cY8OEneJpr9HCKGPKobYys0JBcQeKBZ557y375yCBPDFR1loW" +
            "LjzmLOohl7tv0JoyIEenv8+uFBgP0ZUCwazvozm2rEhIHAh/8I0q4np5X9h/DqoN" +
            "ATofLAOVNHECgYBQ0nuoYqExoEg1UsbM1JcklaKc5BulZDmnnvsMSoevBzIwLst1" +
            "U8eVP6VJcsRcBBSRrVvSZrzb3xvGgxP2XTcPpBj/3hWBBggB/Hfohubfe+dPYc2y" +
            "5s6QArFBSR/ncQMjlkIaZ3xwgKCOzSJ6D5TcJYTru0OE2vVh1NHxUj6HIwKBgBFB" +
            "KDWMjH+XLXXvFeU83isRxlrzHP+PuDOdv42lha1wU7drL/XOsNTunrnOLZDKomI/" +
            "Swd5x9csJhl58CUYb6w+8ifcvtqdM2tfI9yFuco0j+6Bn1ZP0ebKyVtD1s6tPFHI" +
            "jLWsiHG0tlQSsFOsJFAjlRTioaMUF/dSMIAPF42RAoGARkOoRlC+AbndDWlPlrUx" +
            "5SsHTBinckZdMzFpGJwVpk/mYbfSTMyUNkZlZ0O+bgQE4F5babU8fsMLeqeP3PCi" +
            "t9Ak0RU0POjkM184BgNqZf3o2p2AAa9TfvnrkiUpNaQe74EHAJ3DKVQTsOzrDoO2" +
            "dvuiW16+MeReGGtyZG235ac=";

    //RSA(SHA256)支付宝公钥
    static final String rsa_sha256_ali_public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuwKq1hkOoIkNwTKV2bkrtQ+zHx3QvD0x5Ftei26vD28fNtfBqTWAJSbRmfKZxhjqGiEGAXHSgMXyno0aAyAfEt6YiI7DkSizd0C4zjSTrmfe15E0JVlBimx4XjhWK2Hiu1sNg5r9s1JCwHkHuV3pgk3fLxOEfZBNn/gYQGEulei10Ip6sMSGVDmPo+ZyLnGbIHVWrI4FjuS0A3Q6b2Ia47+gLpXIjiuy3PJSeh3prYZ3SwAq9c03tpE+80ZLmA41Mu+0uaqj1IezjB+3MQsXQ35kpJezWh0dTm6az/BmmdVFYQdXPr2SizUKB0lovvKDHOfy3Ip1QrxxA4+m7GPJZQIDAQAB";

    public static final String  SIGN_ALGORITHMS_RSA_SHA256 = "SHA256withRsa";

    /**
     *支付宝支付
     * @param orderId 订单编号
     * @param subject 商品名称
     * @param actualPay 实际支付金额
     * @return
     */
    public String getOrderInfoByAliPay(String orderId,String subject,float actualPay) {
        //回调页面
        System.out.println("AAAA:"+String.valueOf(actualPay)+"SSSS");
        String[] parameters={
                "service=\"mobile.securitypay.pay\"",//固定值（手机快捷支付）
                "partner=\""+pid+"\"",//合作身份者ID（16位）
                "_input_charset=\"utf-8\"",
                "notify_url=\""+notify_url+"\"",//通知地址
                "out_trade_no=\""+orderId+"\"",//商户内部订单号
                "subject=\""+subject+"\"",//测试
                "payment_type=\"1\"",//固定值
                "seller_id=\""+seller_id+"\"",//账户邮箱
                "total_fee=\""+String.valueOf(actualPay)+"\"",//支付金额（元）
                "body=\"order body\"",//订单说明
                "it_b_pay=\"30m\""//（订单过期时间 30分钟过期无效）
      };
        String signOrderUrl = signAllString(parameters);
        return signOrderUrl;
    }

    /**
     * 支付宝签名
     * @param array
     * @return
     */
    private String signAllString(String [] array){
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < array.length; i++) {
            if(i==(array.length-1)){
                sb.append(array[i]);
            }else{
                sb.append(array[i]+"&");
            }
        }
        System.out.println(sb.toString());
        String sign = "";
        try {
            sign = URLEncoder.encode(AliPayService.sign(sb.toString(), rsa_sha1_private_key, "utf-8"), "utf-8");//private_key私钥
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&sign=\""+sign+"\"&");
        sb.append("sign_type=\"RSA\"");
        return sb.toString();
    }





    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param input_charset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String input_charset)
    {
        try
        {
            byte[] decode = Base64.decode(privateKey);
            PKCS8EncodedKeySpec priPKCS8   = new PKCS8EncodedKeySpec(decode );
            KeyFactory keyf= KeyFactory.getInstance("RSA");
            PrivateKey priKey= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_RSA_SHA1);

            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
   //  * @param ali_public_key 支付宝公钥
   //  * @param input_charset 编码格式
     * @return 布尔值
     */
    public boolean verify(String content, String sign)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(rsa_sha1_ali_public_key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS_RSA_SHA1);

            signature.initVerify(pubKey);
            signature.update( content.getBytes("utf-8") );

            boolean bverify = signature.verify( Base64.decode(sign) );
            return bverify;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 解密
     * @param content 密文
     * @param private_key 商户私钥
     * @param input_charset 编码格式
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }


    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = Base64.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    //获取订单的支付信息
    public AlipayTradeQueryResponse getOrderPaymentInfo(String order_no,String trade_no){

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", app_id, rsa_sha256_private_key, "json", "utf-8", rsa_sha256_ali_public_key, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "   \"out_trade_no\":\""+order_no+"\"," +
                "   \"trade_no\":\""+trade_no+"\"" +
                "  }");//设置业务参数
        AlipayTradeQueryResponse response=null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
//调用成功，则处理业务逻辑
        if(response.isSuccess()){
            //.....
            System.out.println("getTradeStatus="+response.getTradeStatus());

        }
        return response;
    }

    //订单的商户费用清算转账
    public AlipayFundTransToaccountTransferResponse transferOrderPaymentToComanyAccount(String order_no,String trade_no) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", web_app_id, rsa_sha256_private_key, "json", "utf-8", rsa_sha256_ali_public_key, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"1111111\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"13774216002\"," +
                "\"amount\":\"0.1\"," +
                "\"payer_show_name\":\"熊猫救援服务费\"," +
                "\"payee_real_name\":\"王科特\"," +
                "\"remark\":\"转账备注\"" +
                "}");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {

            System.out.println("调用失败"+response.getCode());
        }

        return response;
    }



    public static void main(String[] args) throws InterruptedException {

//        String content="body=order body&buyer_email=137****6002&buyer_id=2088702692123578&discount=0.00&gmt_create=2018-08-02 21:59:15&gmt_payment=2018-08-02 21:59:16&is_total_fee_adjust=N&notify_id=1b3ac553ab43152d6704bccd4ccd01dkeh&notify_time=2018-08-02 21:59:16&notify_type=trade_status_sync&out_trade_no=OR104&payment_type=1&price=0.01&quantity=1&seller_email=ketewang@youyuantech.com&seller_id=2088231183736857&subject=test order&total_fee=0.01&trade_no=2018080221001004570508449323&trade_status=TRADE_SUCCESS&use_coupon=N";
//        String sign="dcfD3pjmxI3iJiCAWnJhVB7mZA1qtOoQVpU0mwdB0xBcih5zFVwILCPsNVmC/s7MKdUyejouAFwfKtBoubImzoRmc3xmKY9Vfr9dmb/EVRc7zn8aRvZpX5tHtXjZ9v1rctakSuNrFG87J8yQC43OSfG3wQqcgNAvlgMR731jXx0=";
        AliPayService ali=new AliPayService();
//        boolean result=ali.verify(content,sign);
//        System.out.println("result="+result);

        //ali.getOrderPaymentInfo("OR118","2018080321001004570508443810");
        ali.transferOrderPaymentToComanyAccount("OR118","2018080321001004570508443810");
    }
}
