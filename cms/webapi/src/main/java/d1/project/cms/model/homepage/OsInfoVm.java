package d1.project.cms.model.homepage;

import net.dcrun.component.osinfo.model.OsInfo;

/**
 * @author baozh
 */
public class OsInfoVm {
    private OsInfo osInfo;

    private String ipInfo;

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    public OsInfo getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(OsInfo osInfo) {
        this.osInfo = osInfo;
    }
}
