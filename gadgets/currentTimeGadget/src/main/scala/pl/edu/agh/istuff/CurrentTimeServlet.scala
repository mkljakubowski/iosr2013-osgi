package pl.edu.agh.istuff

import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import java.io.File

import org.osgi.framework._
import com.weiglewilczek.scalamodules._

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

class CurrentTimeServlet (context:BundleContext) extends HttpServlet{

  override def doGet(req : HttpServletRequest , resp : HttpServletResponse ) {

    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var tcontext : TemplateContext = null
    eng andApply { _.createContext() }   match {
      case None => println("No key with that name!")
      case Some(x) =>   tcontext=x
    }

    var processor : TemplateProcessor  = null

    val url=context.getBundle().getResource("index.html")

    eng andApply { _.createProcessor(url)
    }   match {
      case None => println("No key with that name!")
      case Some(x) =>   processor=x
    }

    tcontext.put("title", "Current time is around:")
    val today = Calendar.getInstance().getTime()
    val timeFormat = new SimpleDateFormat("HH:mm:ss")
    tcontext.put("text", timeFormat.format(today))

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext,out)
  }

}
