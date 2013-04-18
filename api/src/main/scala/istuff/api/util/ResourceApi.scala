package istuff.api.util

import javax.servlet.http.HttpServlet
import java.io.File
import istuff.api.models.widget.WidgetDescriptor


trait ResourceApi {

  /*
  Enables to register a file as a widget, returns the  address based on the name.
   */
  def registerResource(file : File, name : String)

  /*
  Enables to register a servlet as a widget, returns the address based on the name.
   */
  def registerServlet(servlet : HttpServlet, name : String)

  /*
  Enables to simply register css for a widget.
  */
  def addCss(file : File, name : String)

  /*
  Enables to simply register css for a page, returns the address based on the name. Maybe use OpenSocial gadgets?
  */
  def registerWidget(widget : WidgetDescriptor, name : String)

}
