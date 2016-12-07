package kz.bsbnb.cli.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * Created by Timur.Abdykarimov on 03.09.2016.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BannerProvider extends DefaultBannerProvider {
    //@Override
    public String getBanner(){
        StringBuffer buf = new StringBuffer();
        buf.append("======================================" + OsUtils.LINE_SEPARATOR);
        buf.append("*                                    *" + OsUtils.LINE_SEPARATOR);
        buf.append("*            BLOCKCHAIN              *" + OsUtils.LINE_SEPARATOR);
        buf.append("*                                    *" + OsUtils.LINE_SEPARATOR);
        buf.append("======================================" + OsUtils.LINE_SEPARATOR);
        return buf.toString();
    }

    public String getVersion() {
        return "0.0.1";
    }

    public String getWelcomeMessage() {
        return "Welcome to BC CLI";
    }

    @Override
    public String getProviderName() {
        return "BC Banner";
    }
}
