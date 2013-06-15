package istuff.user.service.impl

import org.osgi.framework.BundleContext
import com.mongodb.{DBCursor, BasicDBObject, DBCollection}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable

/**
 * User: Piotr Borowiec
 * Date: 6/1/13
 * Time: 12:02 PM
 */
class CustomizerView(context: BundleContext, userWidgetColl: DBCollection) extends HttpServlet with Loggable {

  val userCollName = "user"
  val widgetCollName = "widget"
  val versionCollName = "version"
  val heightCollName = "height"
  val widthCollName = "width"
  val xposCollName = "xpos"
  val yposCollName = "ypos"

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {

    val session = request getSession (false)
    if (session == null || session.getAttribute("login") != "authenticated") {

      response.setContentType("text/html;charset=UTF-8")
      response.sendRedirect("/login")

    } else {
      val user = session getAttribute ("user") toString()

      val name = request.getParameter("name")
      val version = request.getParameter("version")
      val height = request.getParameter("height")
      val width = request.getParameter("width")
      val xpos = request.getParameter("xposition")
      val ypos = request.getParameter("yposition")

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

}
