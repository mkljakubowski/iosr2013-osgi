package istuff.api.util

import istuff.api.models.widget.WidgetDescriptor
import java.io.File
import javax.servlet.http.HttpServlet
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import org.osgi.framework.BundleContext


class ResourceApiImpl(context:BundleContext) extends ResourceApi {

  var nameMap : Map[String,String] = Map()
  var httpService : ServiceFinder[HttpService] = null
  /**
  Enables to register a file as a widget, returns the  address based on the name.
  */
  def registerResource(file: File, name: String) {

  }

  /**
    Enables to register a servlet as a widget, returns the address based on the name.
  */
  def registerServlet(servlet: HttpServlet, name: String) {
             httpService = context findService withInterface[HttpService]

              if (!nameMap.contains(name)){
                httpService andApply { _.registerServlet("/servlets/"+name,servlet,null,null) } match {
                  case None => println("res fail")
                  case _ => println("res success")
                }
             }
  }

  /*
    Enables to simply register css for a widget.
    */
  def addCss(file: File, name: String) {}

  /*
    Enables to simply register css for a page, returns the address based on the name. Maybe use OpenSocial gadgets?
    */
  def registerWidget(widget: WidgetDescriptor, name: String) {}
}
