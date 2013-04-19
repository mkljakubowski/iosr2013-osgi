package pl.edu.agh.istuff.social;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.amdatu.opensocial.spi.AppDataBackendService;
import org.amdatu.opensocial.spi.BackendException;
import org.apache.shindig.protocol.DataCollection;

/**
 * Provides application data to gadgets.
 */
public class DemoAppDataService implements AppDataBackendService {

    private Map<String, Map<String, String>> m_appData;

    /**
     * Creates a new {@link DemoAppDataService} instance.
     */
    public DemoAppDataService() {
        m_appData = new HashMap<String, Map<String, String>>();
    }

    @Override
    public DataCollection getPersonData(Collection<String> userIds, String groupId, String appId,
                                        Collection<String> fields) throws BackendException {
        Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

        for (String userId : userIds) {
            Map<String, String> appData = m_appData.get(userId);
            data.put(userId, ((appData == null) ? Collections.<String, String> emptyMap() : appData));
        }
        return new DataCollection(data);
    }

    @Override
    public void deletePersonData(String userId, String groupId, String appId, Collection<String> fields)
            throws BackendException {
        m_appData.remove(userId);
    }

    @Override
    public void updatePersonData(String userId, String groupId, String appId, Collection<String> fields,
                                 Map<String, String> values) throws BackendException {
        m_appData.put(userId, new HashMap<String, String>(values));
    }
}
