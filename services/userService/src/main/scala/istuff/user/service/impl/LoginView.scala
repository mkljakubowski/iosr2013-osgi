package istuff.user.service.impl

import org.osgi.framework.BundleContext
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import com.weiglewilczek.scalamodules._
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import scala.Some
import com.mongodb.{BasicDBObject, DBCollection}

class LoginView(context: BundleContext, userColl: DBCollection) extends HttpServlet with Loggable {

  var processor: TemplateProcessor = _

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {

    if (request.getParameter("user") != null && request.getParameter("pwd") != null) {

      val userPasswordQuery = new BasicDBObject("name", request.getParameter("user")).append("pwd", request.getParameter("pwd"))
      val result = userColl.find(userPasswordQuery)

      if (result.hasNext) {
        val session = request getSession (true)
        session setAttribute("login", "authenticated")
        session setAttribute("user", request.getParameter("user"))
        response.setHeader("redirect_to", "/")
      }
      else {

        val session = request getSession (false)
        if (session != null) session invalidate()
        response.setHeader("redirect_to", "/login")

      }
    }
    else {
      val session = request getSession (false)
      if (session != null) session invalidate()
      response.setHeader("redirect_to", "/login")
    }
  }

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {

    // Retrieve a Velocity implementation of the engine
    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var templateContext: TemplateContext = null
    eng andApply {
      _.createContext()
    } match {
      case None => logger error ("No key with that name!")
      case Some(x) => templateContext = x
    }

    val url = context.getBundle().getResource("login.html")
    eng andApply {
      _.createProcessor(url)
    } match {
      case None => logger error ("No key with that name!")
      case Some(x) => processor = x
    }

    response.setContentType("text/html;charset=UTF-8")
    response.setContentType("text/html")
    val out = response.getWriter()
    processor.generateStream(templateContext, out)
  }

}
