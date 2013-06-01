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
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 5/25/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
class WidgetsView (context:BundleContext, userWidgetColl : DBCollection) extends HttpServlet with Loggable {

  val userCollName = "user"
  val widgetCollName = "widget"
  val versionCollName = "version"

  override def doPost(req : HttpServletRequest , resp : HttpServletResponse ) {
    val user = req.getCookies.toList.filter(c => c.getName == "user").head.getValue
    var items = Set[String]()
    var doubles = Set[String]()

    req.getParameterNames.asScala.foreach(s => items += s.toString)

    val widgetCursor = userWidgetColl.find(new BasicDBObject(userCollName, user))
    while(widgetCursor.hasNext){
      val entry = widgetCursor.next
      if(entry.get(widgetCollName) != "main")
      {
        if(!items.contains(entry.get(widgetCollName) + "|" + entry.get(versionCollName)))
          userWidgetColl.remove(entry)
        doubles += entry.get(widgetCollName) + "|" + entry.get(versionCollName)
      }
    }
    widgetCursor.close

    for(item <- items){
      if(!doubles.contains(item)){
        userWidgetColl.insert(new BasicDBObject(userCollName, user)
          .append(widgetCollName, item.split('|')(0))
          .append(versionCollName, item.split('|')(1)))
      }
    }

    resp.setContentType("text/html;charset=UTF-8")
    resp.sendRedirect("/")
  }

  override def doGet(req : HttpServletRequest , resp : HttpServletResponse ) {
    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var tcontext : TemplateContext = null
    eng andApply { _.createContext() }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   tcontext=x
    }

    var processor : TemplateProcessor  = null

    val url = context.getBundle().getResource("widgets.html")
    eng andApply { _.createProcessor(url)
    }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   processor=x
    }

    val widgets = context findService withInterface[WidgetService] andApply {
      _.getAvailableWidgets()
    } getOrElse (List.empty[WidgetDescriptor])
    tcontext.put("widgets", widgets.toArray)

    val user = req.getCookies.toList.filter(c => c.getName == "user").head.getValue
    tcontext.put("user", user)

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext,out)
  }

}
