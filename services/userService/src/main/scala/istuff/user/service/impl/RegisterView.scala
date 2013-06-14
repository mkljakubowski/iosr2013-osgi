package istuff.user.service.impl

import org.osgi.framework.BundleContext
import com.weiglewilczek.scalamodules._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import com.mongodb.{BasicDBObject, DBCollection}

class RegisterView (context:BundleContext, userColl : DBCollection) extends HttpServlet with Loggable {

  override def doPost(req : HttpServletRequest , resp : HttpServletResponse ) {
    if(req.getParameter("user") != null && req.getParameter("pwd1") == req.getParameter("pwd2") && req.getParameter("pwd1") != null){
      val doc = new BasicDBObject("name", req.getParameter("user"))
      val res = userColl.find(doc)
      if (res.count()==0){
        val doc = new BasicDBObject("name", req.getParameter("user")).append("pwd", req.getParameter("pwd1"))
        userColl.insert(doc)
        resp.setHeader("redirect_to", "/login")
      }
      else{
        resp.setHeader("redirect_to", "/register")
      }


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

    val url = context.getBundle().getResource("register.html")
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
