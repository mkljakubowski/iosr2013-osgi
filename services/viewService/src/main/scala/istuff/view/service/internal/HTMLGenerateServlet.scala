package istuff.view.service.internal

import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable

class HTMLGenerateServlet(context:BundleContext) extends HttpServlet with Loggable {

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

    val url = context.getBundle().getResource("htmls/index.html")
    eng andApply { _.createProcessor(url)
    }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   processor=x
    }

    tcontext.put("title", "My First Document")
    tcontext.put("text", "This is a shorter story about a story that was generated with a template engine.")

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext,out)
  }
}
