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

  var templateContext : TemplateContext = _
  var processor: TemplateProcessor = _

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    val session = request getSession (false)

    if (session == null || session.getAttribute("login") != "authenticated") {

      response.setContentType("text/html;charset=UTF-8")
      response.sendRedirect("/login")

    } else {

      val user = session getAttribute ("user")

      // Retrieve a Velocity implementation of the engine
      val templateEngine = context findService classOf[TemplateEngine]

      // Create & fill the context
      templateEngine andApply {
        _.createContext()
      } match {
        case None => logger error ("No key with that name!")
        case Some(x) => templateContext = x
      }

      val url = context.getBundle().getResource("index.html")
      templateEngine andApply {
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

      templateContext.put("mainHeight", 100)
      templateContext.put("mainWidth", 200)
      templateContext.put("mainYPos", 5)
      templateContext.put("mainXPos", 5)

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
            templateContext.put("mainHeight", currentWidget.get("height").toString.toInt)
            templateContext.put("mainWidth", currentWidget.get("width").toString.toInt)
            templateContext.put("mainYPos", currentWidget.get("ypos").toString.toInt)
            templateContext.put("mainXPos", currentWidget.get("xpos").toString.toInt)
          }
        }
      }

      templateContext.put("widgets", userWidgets.toArray)
      templateContext.put("user", user)

      response.setContentType("text/html;charset=UTF-8")
      response.setContentType("text/html")
      val out = response.getWriter()
      processor.generateStream(templateContext, out)
    }
  }
}
