package pl.edu.agh.istuff.social;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.amdatu.opensocial.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;

/**
 * Provides a proper configuration for this demo dashboard.
 */
public class DemoConfiguration {
    private volatile ConfigurationAdmin m_configAdmin;
    private volatile LogService m_logService;

    public void start() {
        try {
            Configuration configuration = m_configAdmin.getConfiguration(Constants.SHINDIG_CONFIGURATION_PID, null);
            Properties properties = new Properties();

            InputStream is = getClass().getResourceAsStream("/resources/shindig.properties");
            if (is != null) {
                properties.load(is);
            }
            else {
                m_logService.log(LogService.LOG_WARNING, "Failed to locate & load properties file!");
            }
            configuration.update(properties);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}