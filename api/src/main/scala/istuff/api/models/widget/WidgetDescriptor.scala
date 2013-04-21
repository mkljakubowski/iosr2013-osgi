package istuff.api.models.widget

import javax.servlet.http.HttpServlet
import org.osgi.framework.BundleContext


case class WidgetDescriptor(
    name: String,
    version: Int,
    owner: String,
    servlets: List[(String, HttpServlet)],
    resources: List[String]
)
{
  val addresses = servlets.map(_._1).toArray
}
