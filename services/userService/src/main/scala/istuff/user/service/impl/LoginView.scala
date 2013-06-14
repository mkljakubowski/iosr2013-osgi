package istuff.user.service.impl

import org.osgi.framework.BundleContext
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import com.weiglewilczek.scalamodules._
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import scala.Some
import com.mongodb.{BasicDBObject, DBCollection}

class LoginView (context:BundleContext, userColl : DBCollection) extends HttpServlet with Loggable {

  override def doPost(req : HttpServletRequest , resp : HttpServletResponse ) {
    if(req.getParameter("user") != null && req.getParameter("pwd") != null){
      val query = new BasicDBObject("name", req.getParameter("user")).append("pwd",req.getParameter("pwd"))
      val res = userColl.find(query)
      if(res.hasNext){
        val session = req getSession(true)
        session setAttribute("login","authenticated")
        session setAttribute("user",req.getParameter("user"))
        resp.setHeader("redirect_to", "/")
      }
      else{
        val session = req getSession(false)
        if (session != null ) session invalidate()
        resp.setHeader("redirect_to", "/login")
    }
    } else {
      val session = req getSession(false)
      if (session != null ) session invalidate()
      resp.setHeader("redirect_to", "/login")
    }
  }

  override def doGet(req : HttpServletRequest , resp : HttpServletResponse ) {
    // Retrieve a Velocity implementation of the engine
    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var tcontext : TemplateContext = null
    eng andApply { _.createContext() }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   tcontext=x
    }

    var processor : TemplateProcessor  = null

    val url = context.getBundle().getResource("login.html")
    eng andApply { _.createProcessor(url)
    }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   processor=x
    }

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext,out)
  }

}
