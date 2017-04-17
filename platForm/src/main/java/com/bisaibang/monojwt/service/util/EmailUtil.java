package com.bisaibang.monojwt.service.util;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.ses.SesClient;
import com.baidubce.services.ses.SesClientConfiguration;
import com.baidubce.services.ses.model.SendEmailResponse;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by xiazhen on 2017/3/6.
 */
public final class EmailUtil {

    public static String sendTestEmail(String email) {
        String AK = "773d48e469be4accafa193cf1d14ee08";  // AccessKeyId
        String SK = "91d6dd0df5e04e1abfb03635f72a26dc";  // SecretAccessKey

        SesClientConfiguration configuration = new SesClientConfiguration();
        configuration.setCredentials(new DefaultBceCredentials(AK, SK));
        SesClient client = new SesClient(configuration);
        String fromAddress = "match@allied-esports.com";  // 发信邮箱
        String[] toAddresses = {email};  // 收信邮箱
        String subject = "报名成功";  // 邮件标题
        String body = "恭喜你。";  // 邮件正文
        SendEmailResponse response = client.sendEmail(fromAddress, toAddresses, subject, body);
        return "邮件流水号: " + response.getMessageId().toString();
    }

}
