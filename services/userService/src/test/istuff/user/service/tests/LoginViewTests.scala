package istuff.user.service.tests

import _root_.istuff.user.service.impl.LoginView
import org.scalatest.FlatSpec
import com.mongodb.{DBCursor, BasicDBObject, DBCollection}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito._
import org.mockito.Matchers._
import matchers.IsCorrectUser

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class LoginViewTests extends FlatSpec with MockitoSugar{

  "LoginView" should "check user credentials from HttpServletRequest" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd")).toReturn(pwd)
    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(userColl.find(any[BasicDBObject])).toReturn(cursor)
    stub(cursor.hasNext).toReturn(false)

    val view = new LoginView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(userColl).find(argThat(new IsCorrectUser(name, pwd)))
  }

  it should "redirect to login page if user credentials are not valid" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd")).toReturn(pwd+"bad_pass")
    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(userColl.find(any[BasicDBObject])).toReturn(cursor)

    val view = new LoginView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).setHeader("redirect_to", "/login")
  }

  it should "redirect to login page if user credentials are void" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(null)
    stub(req.getParameter("pwd")).toReturn(null)
    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(userColl.find(any[BasicDBObject])).toReturn(cursor)

    val view = new LoginView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).setHeader("redirect_to", "/login")
  }

  it should "redirect to main page upon authentication" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val session = mock[HttpSession]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd")).toReturn(pwd)
    stub(req.getSession(any[Boolean])).toReturn(session)
    stub(userColl.find(any[BasicDBObject])).toReturn(cursor)
    stub(cursor.hasNext).toReturn(true)

    val view = new LoginView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).setHeader("redirect_to", "/")
  }

}

