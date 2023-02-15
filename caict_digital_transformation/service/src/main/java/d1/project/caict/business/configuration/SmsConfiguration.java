package d1.project.caict.business.configuration;

import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import net.dcrun.component.sms.ucpass.SmsUcpassService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maorui
 */
@Configuration
public class SmsConfiguration {

    @Value("${sms.ucpaas.sid}")
    private String smsId;

    @Value("${sms.ucpaas.token}")
    private String smsToken;

    @Value("${sms.ucpaas.appid}")
    private String smsAppId;

    @Bean
    ISmsUcpassService getSmsUcpassService() {
        SmsUcpassService smsUcpassService = new SmsUcpassService();
        smsUcpassService.init(smsId, smsToken, smsAppId);
        return smsUcpassService;
    }
}

