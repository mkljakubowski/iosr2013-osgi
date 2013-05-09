package pl.edu.agh.istuff.gallery.impl


import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable

class GalleryServlet(name : String) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {



    resp.setContentType("text/html;charset=UTF-8")
    resp.sendRedirect("/"+name+"/gallery.html")
  }

}
