package pl.edu.agh.istuff.social;


import org.apache.felix.dm.Component;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

/**
 * Registers the demo dashboard content.
 */
public class DemoDashboard {

    private volatile HttpService m_service;
    private volatile HttpContext m_context;

    /**
     * Called by Felix DM.
     */
    public void start(Component component) throws Exception {
        m_service.registerResources("/social", "/", m_context);
    }

    /**
     * Called by Felix DM.
     */
    public void stop(Component component) throws Exception {
        m_service.unregister("/social");
    }
}