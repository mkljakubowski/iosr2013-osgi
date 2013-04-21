package pl.edu.agh.istuff.time.impl

import com.weiglewilczek.scalamodules._

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.util.Calendar
import java.text.SimpleDateFormat
import istuff.api.util.Loggable
import java.net.URL
import org.osgi.framework.Bundle

class CurrentTimeServlet(templateEngine: ServiceFinder[TemplateEngine], self: Bundle) extends HttpServlet with Loggable {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {

    // Create & fill the context
    val tcontext = templateEngine andApply { _.createContext() } match {
      case None => logger warn("No key with that name!"); null
      case Some(x) => x
    }

    var processor: TemplateProcessor = null


    templateEngine andApply { _.createProcessor(self.getResource("index.html"))  } match {
      case None => logger warn("No key with that name!")
      case Some(x) => processor = x
    }

    tcontext.put("title", "Current time is around:")
    val today = Calendar.getInstance().getTime()
    val timeFormat = new SimpleDateFormat("HH:mm:ss")
    tcontext.put("text", timeFormat.format(today))

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext, out)
  }

}
