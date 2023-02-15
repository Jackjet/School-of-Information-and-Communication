package d1.project.caict.business.utils;

import net.dcrun.component.http.HttpService;
import net.dcrun.component.http.IHttpService;
import net.dcrun.component.security.HmacSignService;
import net.dcrun.component.security.IHmacSignService;

/**
 * @author: maorui
 * @date: 2020/10/12
 */
public class ServiceFactory {

    public static IHmacSignService hmacSignService;
    private static IHttpService httpService;

    public synchronized static IHmacSignService getHmacSignService() {
        if (hmacSignService == null) {
            hmacSignService = new HmacSignService();
        }
        return hmacSignService;
    }

    public synchronized static IHttpService getHttpService() {
        if (httpService == null) {
            httpService = new HttpService();
            httpService.setTimeout(60, 2 * 60 * 60, 2 * 60 * 60);
        }
        return httpService;
    }
}
