package com.prweb.util;

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

    //partner ID
    static final String pid = "2088231183736857";

    //商家id
    static final String seller_id = "2088231183736857";

    //回调服务器地址
    static final String notify_url="http://116.62.17.42:9090/Order/AliPayNotify.action";

    //开发者私钥
    static final String private_key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMY0B8ECj+CqpEGE" +
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

    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";

    //支付宝公钥
    static final String ali_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


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
            sign = URLEncoder.encode(AliPayService.sign(sb.toString(), private_key, "utf-8"), "utf-8");//private_key私钥
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

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

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
     * @param ali_public_key 支付宝公钥
   //  * @param input_charset 编码格式
     * @return 布尔值
     */
    public boolean verify(String content, String sign)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(ali_public_key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

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


    public static void main(String[] args) throws InterruptedException {

        String content="body=\"order body\"&buyer_email=\"137****6002\"&buyer_id=\"2088702692123578\"&discount=\"0.00\"&gmt_create=\"2018-08-02 21:49:31\"&gmt_payment=\"2018-08-02 21:49:32\"&is_total_fee_adjust=\"N\"&notify_id=\"36d09ba8096c402581e84da13c576b1keh\"&notify_time=\"2018-08-02 21:49:32\"&notify_type=\"trade_status_sync\"&out_trade_no=\"OR103\"&payment_type=\"1\"&price=\"0.01\"&quantity=\"1\"&seller_email=\"ketewang@youyuantech.com\"&seller_id=\"2088231183736857\"&subject=\"test order\"&total_fee=\"0.01\"&trade_no=\"2018080221001004570508722798\"&trade_status=\"TRADE_SUCCESS\"&use_coupon=\"N\"";
        String sign="n+jlPQoubIZxBNHgbgbmoU8MXJGxgxkk3wYk5byyh9zzRsqskzlDYCYz37vWVe+7lwtbVb+3BuRNrIutTDRwXwLctieM5XCU0PQVmQ0XhhzTL7lFyKIu/GjU553B7uYZIh+/NKMdoasrsPgy9Z7qhj4EPazwhGFPGCibJ7orZRU=";
        AliPayService ali=new AliPayService();
        boolean result=ali.verify(content,sign);
        System.out.println("result="+result);

    }
}
