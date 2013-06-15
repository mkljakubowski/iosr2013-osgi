package istuff.user.service.tests

import org.scalatest.FlatSpec
import com.mongodb.{DBCursor, DBCollection}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito._
import istuff.user.service.impl.{WidgetsView, RegisterView}
import org.mockito.Matchers._
import matchers.IsAnyString
import org.hamcrest.core.IsAnything
import java.util

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class WidgetsViewTests extends FlatSpec with MockitoSugar{

  "WidgetsView" should "redirect to login page if session is not authenticated" in {
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

    val view = new WidgetsView(context, userColl)

    // Act
    view.doPost(req, resp)

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
    val cursor = mock[DBCursor]

    stub(req.getSession(any[Boolean])).toReturn(null)
    stub(session.getAttribute("user")).toReturn(name)
    stub(session.getAttribute("login")).toReturn("authenticated")

    val view = new WidgetsView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).sendRedirect("/login")
  }
}
