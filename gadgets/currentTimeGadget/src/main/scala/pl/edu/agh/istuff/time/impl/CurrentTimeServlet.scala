package pl.edu.agh.istuff.time.impl

import com.weiglewilczek.scalamodules._

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.util.Calendar
import java.text.SimpleDateFormat
import istuff.api.util.Loggable
import java.net.URL
import org.osgi.framework.Bundle

class CurrentTimeServlet(name : String) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {



    resp.setContentType("text/html;charset=UTF-8")
    resp.sendRedirect("/"+name+"/index.html")
  }

}
