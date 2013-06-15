package istuff.view.service.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.{Bundle, BundleContext}
import com.mongodb.{BasicDBObject, DBCursor, DBCollection}
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito._
import org.mockito.Matchers._
import istuff.view.service.impl.MainPageView
import com.weiglewilczek.scalamodules._
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import java.net.URL


/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class MainPageViewTests extends FlatSpec with MockitoSugar{

  "MainPageView" should "redirect to login page if session is not authenticated" in {
    // Arrange
    val name = "johnny"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val cursor = mock[DBCursor]

    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(session.getAttribute("user")).toReturn(name)
    stub(session.getAttribute("login")).toReturn("not_authenticated")

    val view = new MainPageView(context, userColl)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).sendRedirect("/login")
  }

  it should "redirect to login page if session is null" in {
    // Arrange
    val name = "johnny"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]

    stub(req.getSession(any[Boolean])).toReturn(null)
    stub(session.getAttribute("user")).toReturn(name)
    stub(session.getAttribute("login")).toReturn("authenticated")

    val view = new MainPageView(context, userColl)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).sendRedirect("/login")
  }

  it should "retrieve widget preferences" in {
    // Arrange
    val name = "johnny"

    val context = mock[BundleContext]
    val userWidgetColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val bundle = mock[Bundle]
    val templateContext = mock[TemplateContext]
    val url =  new URL("http", "8080", "index.html")
    val cursor = mock[DBCursor]
    val processor = mock[TemplateProcessor]

    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(session.getAttribute("user")).toReturn(name)
    stub(session.getAttribute("login")).toReturn("authenticated")
    stub(context.getBundle()).toReturn(bundle)
    stub(bundle.getResource("index.html")).toReturn(url)
    stub(userWidgetColl.find(any[BasicDBObject])).toReturn(cursor)
    stub(cursor.hasNext).toReturn(false)

    var view = new MainPageView(context, userWidgetColl)
    view.templateContext = templateContext
    view.processor = processor

    // Act
    view.doGet(req, resp)

    // Assert
    verify(userWidgetColl).find(any[BasicDBObject])
  }

  it should "iterate over widget preferences and set them accordingly" in {
    // Arrange
    val name = "johnny"

    val context = mock[BundleContext]
    val userWidgetColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val bundle = mock[Bundle]
    val templateContext = mock[TemplateContext]
    val url =  new URL("http", "8080", "index.html")
    val cursor = mock[DBCursor]
    val processor = mock[TemplateProcessor]
    val dbObject= mock[BasicDBObject]

    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(session.getAttribute("user")).toReturn(name)
    stub(session.getAttribute("login")).toReturn("authenticated")
    stub(context.getBundle()).toReturn(bundle)
    stub(bundle.getResource("index.html")).toReturn(url)
    stub(userWidgetColl.find(any[BasicDBObject])).toReturn(cursor)
    stub(cursor.hasNext).toReturn(true).toReturn(false)
    stub(cursor.next).toReturn(dbObject)

    var view = new MainPageView(context, userWidgetColl)
    view.templateContext = templateContext
    view.processor = processor

    // Act
    view.doGet(req, resp)

    // Assert
    verify(userWidgetColl).find(any[BasicDBObject])
  }
}
