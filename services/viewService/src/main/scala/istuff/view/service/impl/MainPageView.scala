package istuff.view.service.impl

import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import scala.util.parsing.json.JSONArray
import com.mongodb.{BasicDBObject, DBCollection}
import istuff.database.service.api.Database
import com.weiglewilczek.scalamodules

class MainPageView(context: BundleContext, userWidgetColl: DBCollection) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val session = req getSession (false)

    if (session == null || session.getAttribute("login") != "authenticated") {

      resp.setContentType("text/html;charset=UTF-8")
      resp.sendRedirect("/login")

    } else {

      val user = session getAttribute ("user")

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

      val widgetPreferences = userWidgetColl.find(new BasicDBObject("user", user))

      var userWidgets = Set[WidgetDescriptor]()

      tcontext.put("mainHeight", 100)
      tcontext.put("mainWidth", 200)
      tcontext.put("mainYPos", 5)
      tcontext.put("mainXPos", 5)

      while (widgetPreferences.hasNext) {
        var currentWidget = widgetPreferences.next
        for (widget <- widgets) {
          if (widget.name == currentWidget.get("widget") && widget.version.toString == currentWidget.get("version")) {
            widget.height = currentWidget.get("height") match {
              case null => 400
              case h: String => h.toInt
            }
            widget.width = currentWidget.get("width") match {
              case null => 400
              case w: String => w.toInt
            }
            widget.xpos = currentWidget.get("xpos") match {
              case null => 100
              case w: String => w.toInt
            }
            widget.ypos = currentWidget.get("ypos") match {
              case null => 100
              case w: String => w.toInt
            }

            userWidgets += widget
          }
          else if (currentWidget.get("widget") == "main") {
            tcontext.put("mainHeight", currentWidget.get("height").toString.toInt)
            tcontext.put("mainWidth", currentWidget.get("width").toString.toInt)
            tcontext.put("mainYPos", currentWidget.get("ypos").toString.toInt)
            tcontext.put("mainXPos", currentWidget.get("xpos").toString.toInt)
          }
        }
      }

      tcontext.put("widgets", userWidgets.toArray)
      tcontext.put("user", user)

      resp.setContentType("text/html;charset=UTF-8")
      resp.setContentType("text/html")
      val out = resp.getWriter()
      processor.generateStream(tcontext, out)
    }
  }
}
