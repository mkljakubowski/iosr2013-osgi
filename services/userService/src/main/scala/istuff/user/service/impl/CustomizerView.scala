package istuff.user.service.impl

import org.osgi.framework.BundleContext
import com.mongodb.{DBCursor, BasicDBObject, DBCollection}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/1/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
class CustomizerView (context:BundleContext, userWidgetColl : DBCollection) extends HttpServlet with Loggable {

  val userCollName = "user"
  val widgetCollName = "widget"
  val versionCollName = "version"
  val heightCollName = "height"
  val widthCollName = "width"
  val xposCollName = "xpos"
  val yposCollName = "ypos"

  override def doPost(req : HttpServletRequest , resp : HttpServletResponse ) {
    val user = req.getCookies.toList.filter(c => c.getName == "user").head.getValue
    val name = req.getParameter("name")
    val version = req.getParameter("version")
    val height = req.getParameter("height")
    val width = req.getParameter("width")
    val xpos = req.getParameter("xposition")
    val ypos = req.getParameter("yposition")

    userWidgetColl.remove(
      new BasicDBObject(userCollName, user)
      .append(widgetCollName, name)
      .append(versionCollName, version)
    )

    userWidgetColl.insert(
      new BasicDBObject(userCollName, user)
      .append(widgetCollName, name)
      .append(versionCollName, version)
      .append(heightCollName, height)
      .append(widthCollName, width)
      .append(xposCollName, xpos)
      .append(yposCollName, ypos)
    )

  }

}
