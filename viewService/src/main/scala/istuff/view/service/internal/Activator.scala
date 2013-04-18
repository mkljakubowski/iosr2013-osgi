package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.IndexViewResource
import istuff.widget.service.api.WidgetService
import org.osgi.service.http.HttpService

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.io.File

class HTMLGenerateServlet(context:BundleContext) extends HttpServlet{

  override def doGet(req : HttpServletRequest , resp : HttpServletResponse ) {
    // Retrieve a Velocity implementation of the engine
    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var tcontext : TemplateContext = null
    eng andApply { _.createContext() }   match {
      case None => println("No key with that name!")
      case Some(x) =>   tcontext=x
    }

    var processor : TemplateProcessor  = null

    val file = new File("viewService/src/main/resources/htmls/index.html")
    eng andApply { _.createProcessor(file)
    }   match {
      case None => println("No key with that name!")
      case Some(x) =>   processor=x
    }

    tcontext.put("title", "My First Document")
    tcontext.put("text", "This is a short story about a story that was generated with a template engine.")

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    out.print(processor.generateString(tcontext))
  }
}
class Activator extends BundleActivator {
  var serviceRegistration: ServiceRegistration = _
  var httpService : ServiceFinder[HttpService] = null

  def start(context: BundleContext) {
    val widgetServiceRef:((WidgetService => Any) => Option[Any]) = context findService withInterface[WidgetService] andApply _
    val bundle = new IndexViewResource(widgetServiceRef)
    serviceRegistration = context.createService(bundle)




   httpService = context findService withInterface[HttpService]
    httpService andApply { _.registerServlet("/hell",new HTMLGenerateServlet(context),null,null) } match {
      case None => println("res fail")
      case _ => println("res success")
    }

    httpService andApply { _.registerResources("/","/htmls",null) } match {
      case None => println("res fail")
      case _ => println("res success")
    }

  }

  def stop(context: BundleContext) {
    httpService andUnget
  }
}