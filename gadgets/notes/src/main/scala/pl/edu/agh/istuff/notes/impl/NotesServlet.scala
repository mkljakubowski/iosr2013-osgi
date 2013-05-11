package pl.edu.agh.istuff.notes.impl

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 5/11/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
class NotesServlet (name : String) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    resp.setContentType("text/html;charset=UTF-8")
    resp.sendRedirect("/"+name+"/notes.html")
  }

}