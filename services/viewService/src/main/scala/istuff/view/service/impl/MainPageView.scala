package istuff.view.service.impl

import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import scala.util.parsing.json.JSONArray

class MainPageView(context: BundleContext) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    if (req.getCookies.toList.filter(c => c.getName == "auth" && c.getValue == "true").isEmpty) {
      resp.setContentType("text/html;charset=UTF-8")
      resp.sendRedirect("/login")
    } else {
      val user = req.getCookies.toList.filter(c => c.getName == "user").head.getValue
      // Retrieve a Velocity implementation of the engine
      val eng = context findService classOf[TemplateEngine]

      // Create & fill the context
      var tcontext: TemplateContext = null
      eng andApply {
        _.createContext()
      } match {
        case None => logger error ("No key with that name!")
        case Some(x) => tcontext = x
      }

      var processor: TemplateProcessor = null

      val url = context.getBundle().getResource("index.html")
      eng andApply {
        _.createProcessor(url)
      } match {
        case None => logger error ("No key with that name!")
        case Some(x) => processor = x
      }
      val widgets = context findService withInterface[WidgetService] andApply {
        _.getAvailableWidgets()
      } getOrElse (List.empty[WidgetDescriptor])
      tcontext.put("widgets", widgets.toArray)
      tcontext.put("user", user)

      resp.setContentType("text/html;charset=UTF-8")
      resp.setContentType("text/html")
      val out = resp.getWriter()
      processor.generateStream(tcontext, out)
    }
  }
}
