package istuff.api.models.widget

import javax.servlet.http.HttpServlet
import org.osgi.framework.BundleContext


case class WidgetDescriptor (
    name: String,
    version: Int,
    owner: String,
    servlets: Map[String, HttpServlet],
    resources: List[String],
    var height : Int,
    var width : Int,
    var xpos : Int,
    var ypos : Int
)
{
  val addresses = servlets.map(_._1).toArray
}
