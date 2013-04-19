package pl.edu.agh.istuff.social;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amdatu.opensocial.gadget.Gadget;
import org.amdatu.opensocial.gadget.GadgetProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Provides a servlet for exposing {@link GadgetProvider} in JSON-format.
 */
public class GadgetProviderJsonServlet extends HttpServlet {

    // Injected by Felix DM...
    private volatile GadgetProvider m_gadgetProvider;

    /**
     * Creates a new {@link GadgetProviderJsonServlet} instance.
     */
    public GadgetProviderJsonServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Gadget> gadgets = new ArrayList<Gadget>(m_gadgetProvider.getGadgets(null));

        Collections.sort(gadgets, new Comparator<Gadget>(){
            @Override
            public int compare(Gadget gadget1, Gadget gadget2) {
                return gadget1.getId().compareTo(gadget2.getId());
            }
        });

        JSONObject jsonResult = jsonifyGadgets(gadgets, request);

        ServletOutputStream os = response.getOutputStream();

        response.setContentType("application/json");
        os.print(jsonResult.toJSONString());
        os.flush();
    }

    /**
     * Converts the given collection of {@link Gadget}s to a JSON object.
     *
     * @param gadgets the gadgets to convert, cannot be <code>null</code>.
     * @param request
     * @return a {@link JSONObject}, representing the given gadgets, never <code>null</code>.
     */
    private JSONObject jsonifyGadgets(Collection<Gadget> gadgets, HttpServletRequest request) {
        JSONArray jsonGadgets = new JSONArray();
        for (Gadget gadget : gadgets) {
            jsonGadgets.add(jsonifyGadget(gadget, request));
        }

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("collections", jsonGadgets);
        return jsonResult;
    }

    /**
     * Converts the given {@link Gadget} to a JSON object.
     *
     * @param gadget the gadget to convert, cannot be <code>null</code>.
     * @param request
     * @return a {@link JSONObject}, representing the given gadget, never <code>null</code>.
     */
    private JSONObject jsonifyGadget(Gadget gadget, HttpServletRequest request) {
        JSONObject jsonGadget = new JSONObject();

        jsonGadget.put("id", gadget.getId());

        JSONArray apps = new JSONArray();
        jsonGadget.put("apps", apps);
        for (String entry : gadget.getApps()) {
            apps.add(toAbsoluteForm(entry, request));
        }
        return jsonGadget;
    }

    /** Convert a relative gadget URL to its absolute form, based on the supplied request. */
    private Object toAbsoluteForm(String gadgetURL, HttpServletRequest request) {
        if (gadgetURL.startsWith("/")) {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + gadgetURL;
        }
        return gadgetURL;
    }
}