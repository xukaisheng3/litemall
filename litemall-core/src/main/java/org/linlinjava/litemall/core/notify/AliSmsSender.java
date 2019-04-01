package org.linlinjava.litemall.core.notify;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.google.gson.JsonObject;

/*
 * 阿里云短信服务
 */
public class AliSmsSender implements SmsSender {
    private final Log logger = LogFactory.getLog(AliSmsSender.class);
    private String regionId;
    private String accessKeyId;
    private String accessSecret;
    private String domain;
    private String signName;

    public AliSmsSender(String regionId, String accessKeyId, String accessSecret, String domain, String signName) {
        this.regionId = regionId;
        this.accessKeyId = accessKeyId;
        this.accessSecret = accessSecret;
        this.domain = domain;
        this.signName = signName;
    }

    @Override
    public SmsResult send(String phone, String content) {
        return null;
    }

    /**
     *
     * @param phone
     * @param templateCode
     * @param params  "{\"code\":\"123456\"}"
     * @return
     */
    @Override
    public SmsResult sendWithAliTemplate(String phone, String templateCode, String params) {
        try {
            DefaultProfile profile = DefaultProfile.getProfile(regionId ,accessKeyId, accessSecret);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            //request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain(domain);
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", regionId);
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam",params );
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
                com.alibaba.fastjson.JSONObject jo= com.alibaba.fastjson.JSON.parseObject(response.getData());
                SmsResult smsResult = new SmsResult();
                if(jo.get("Message").equals("OK")){
                    smsResult.setSuccessful(true);
                    smsResult.setResult("发送成功");
                }else{
                    smsResult.setSuccessful(false);
                    smsResult.setResult(jo.get("Message"));
                }
                return smsResult;
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SmsResult sendWithTemplate(String phone, int templateId, String[] params) {
        return null;
    }


}
