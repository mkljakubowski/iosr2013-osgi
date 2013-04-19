package pl.edu.agh.istuff.social;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.Servlet;

import org.amdatu.opensocial.Constants;
import org.amdatu.opensocial.gadget.Gadget;
import org.amdatu.opensocial.gadget.GadgetProvider;
import org.amdatu.opensocial.gadget.SimpleGadget;
import org.amdatu.opensocial.spi.AppDataBackendService;
import org.amdatu.opensocial.spi.PersonBackendService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.shindig.auth.AnonymousAuthenticationHandler;
import org.apache.shindig.auth.AuthenticationHandler;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;

/**
 * Registers the demo content for Amdatu-OpenSocial.
 */
public class Activator extends DependencyActivatorBase {

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        // Nop
    }

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        manager.add(createComponent()
                .setImplementation(DemoDashboard.class)
                .add(createServiceDependency()
                        .setService(HttpService.class)
                        .setRequired(true))
                .add(createServiceDependency()
                        .setService(HttpContext.class, "(contextId=" + Constants.CONTEXT_NAME + ")")
                        .setRequired(true))
        );
        manager.add(createComponent()
                .setImplementation(DemoConfiguration.class)
                .add(createServiceDependency()
                        .setService(ConfigurationAdmin.class)
                        .setRequired(true))
                .add(createServiceDependency()
                        .setService(LogService.class)
                        .setRequired(false))
        );

        manager.add(createComponent()
                .setInterface(PersonBackendService.class.getName(), null)
                .setImplementation(DemoPersonService.class));

        manager.add(createComponent()
                .setInterface(AppDataBackendService.class.getName(), null)
                .setImplementation(DemoAppDataService.class));

        manager.add(createComponent()
                .setInterface(AuthenticationHandler.class.getName(), null)
                .setImplementation(new AnonymousAuthenticationHandler(true)));

        Properties dict = new Properties();
        dict.put("alias", "/gadgetCollections");
        dict.put("contextId", Constants.CONTEXT_NAME);

        manager.add(createComponent()
                .setInterface(Servlet.class.getName(), dict)
                .setImplementation(GadgetProviderJsonServlet.class)
                .add(createServiceDependency()
                        .setService(GadgetProvider.class)
                        .setRequired(true))
        );

        registerDemoGadgets(context, manager);
    }

    private void registerDemoGadgets(BundleContext context, DependencyManager manager) throws Exception {
        registerDemoGadget(manager, new SimpleGadget("todo-suvivor", "http://www.labpixies.com/campaigns/todo/todo.xml", "http://www.labpixies.com/campaigns/survivor/survivor.xml"));

        // Load all other demo gadgets from our samples folder in our bundle...
        Enumeration entries = context.getBundle().findEntries("/resources/samples", "*.xml", true /* recurse */);
        int idx = 0;
        while (entries.hasMoreElements()) {
            URL entry = (URL) entries.nextElement();

            String uri = entry.getFile();
            if (uri.startsWith("/resources")) {
                uri = uri.substring(10);
            }
            String id = "gadget-" + (++idx);
            registerDemoGadget(manager, new SimpleGadget(id, uri));
        }
    }

    private void registerDemoGadget(DependencyManager manager, Gadget gadget) {
        manager.add(createComponent()
                .setInterface(Gadget.class.getName(), new Properties())
                .setImplementation(gadget)
        );
    }
}