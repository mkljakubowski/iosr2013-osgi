package pl.edu.agh.istuff.notes.impl

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable

/**
 * User: Piotr Borowiec
 * Date: 5/11/13
 * Time: 1:13 PM
 */
class NotesServlet (name : String, version: Int) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    resp.setContentType("text/html;charset=UTF-8")
    resp.sendRedirect("/"+name+"/" + version + "/notes.html")
  }

}