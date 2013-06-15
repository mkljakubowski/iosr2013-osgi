package istuff.user.service.impl

import org.osgi.framework.BundleContext
import com.weiglewilczek.scalamodules._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import com.mongodb.{BasicDBObject, DBCollection}

class RegisterView (context:BundleContext, userCollection : DBCollection) extends HttpServlet with Loggable {

  var processor : TemplateProcessor = _

  override def doPost(request : HttpServletRequest , response : HttpServletResponse ) {

    if(request.getParameter("user") != null && request.getParameter("pwd1") == request.getParameter("pwd2") && request.getParameter("pwd1") != null){

      val query = new BasicDBObject("name", request.getParameter("user"))
      val result = userCollection.find(query)

      if (result.count()==0){
        val document = new BasicDBObject("name", request.getParameter("user")).append("pwd", request.getParameter("pwd1"))
        userCollection.insert(document)
        response.setHeader("redirect_to", "/login")
      }
      else{
        response.setHeader("redirect_to", "/register")
      }
    }
  }

  override def doGet(request : HttpServletRequest , response : HttpServletResponse ) {

    // Retrieve a Velocity implementation of the engine
    val templateEngine = context findService classOf[TemplateEngine]

    // Create & fill the context
    var templateContext : TemplateContext = null
    templateEngine andApply { _.createContext() }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   templateContext=x
    }

    val url = context.getBundle().getResource("register.html")
    templateEngine andApply { _.createProcessor(url)
    }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   processor=x
    }

    response.setContentType("text/html;charset=UTF-8")
    response.setContentType("text/html")
    val out = response.getWriter()
    processor.generateStream(templateContext,out)
  }

}
