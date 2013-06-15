package istuff.user.service.impl

import org.osgi.framework.BundleContext
import com.weiglewilczek.scalamodules._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import collection.JavaConverters._
import com.mongodb.{BasicDBObject, DBCollection}

/**
 * User: Piotr Borowiec
 * Date: 5/25/13
 * Time: 12:58 PM
 */
class WidgetsView(context: BundleContext, userWidgetColl: DBCollection) extends HttpServlet with Loggable {

  val userCollName = "user"
  val widgetCollName = "widget"
  val versionCollName = "version"

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {

    val session = request getSession (false)
    if (session == null || session.getAttribute("login") != "authenticated") {

      response.setContentType("text/html;charset=UTF-8")
      response.sendRedirect("/login")

    } else {
      val user = session getAttribute ("user") toString()

      var items = Set[String]()
      var doubles = Set[String]()

      request.getParameterNames.asScala.foreach(s => items += s.toString)

      val widgetCursor = userWidgetColl.find(new BasicDBObject(userCollName, user))

      while (widgetCursor.hasNext) {

        val entry = widgetCursor.next

        if (entry.get(widgetCollName) != "main") {
          if (!items.contains(entry.get(widgetCollName) + "|" + entry.get(versionCollName)))
            userWidgetColl.remove(entry)
          doubles += entry.get(widgetCollName) + "|" + entry.get(versionCollName)
        }
      }
      widgetCursor.close

      for (item <- items) {
        if (!doubles.contains(item)) {
          userWidgetColl.insert(new BasicDBObject(userCollName, user)
            .append(widgetCollName, item.split('|')(0))
            .append(versionCollName, item.split('|')(1)))
        }
      }

      response.setContentType("text/html;charset=UTF-8")
      response.sendRedirect("/")
    }
  }

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    val session = request getSession (false)
    if (session == null || session.getAttribute("login") != "authenticated") {

      response.setContentType("text/html;charset=UTF-8")
      response.sendRedirect("/login")

    } else {
      val user = session getAttribute ("user") toString()
      val templateEngine = context findService classOf[TemplateEngine]

      // Create & fill the context
      var templateContext: TemplateContext = null
      templateEngine andApply {
        _.createContext()
      } match {
        case None => logger error ("No key with that name!")
        case Some(x) => templateContext = x
      }

      var processor: TemplateProcessor = null

      val url = context.getBundle().getResource("widgets.html")
      templateEngine andApply {
        _.createProcessor(url)
      } match {
        case None => logger error ("No key with that name!")
        case Some(x) => processor = x
      }

      val widgets = context findService withInterface[WidgetService] andApply {
        _.getAvailableWidgets()
      } getOrElse (List.empty[WidgetDescriptor])
      templateContext.put("widgets", widgets.toArray)


      templateContext.put("user", user)

      response.setContentType("text/html;charset=UTF-8")
      response.setContentType("text/html")

      val out = response.getWriter()
      processor.generateStream(templateContext, out)
    }
  }

}
