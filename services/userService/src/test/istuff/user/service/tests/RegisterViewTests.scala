package istuff.user.service.tests

import _root_.istuff.user.service.impl.RegisterView
import org.scalatest.FlatSpec
import com.mongodb.{DBCursor, DBCollection}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.BundleContext
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito._
import org.mockito.Matchers.argThat
import matchers.{IsAnyDBObject, IsAnyString}

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class RegisterViewTests extends FlatSpec with MockitoSugar{

  "RegisterView" should "not register user with void username" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd1")).toReturn(pwd)

    val view = new RegisterView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp, never()).setHeader(argThat(new IsAnyString()),argThat(new IsAnyString()))
  }

  it should "not register user with two different passwords" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd1")).toReturn(pwd)
    stub(req.getParameter("pwd2")).toReturn(pwd+"different password")

    val view = new RegisterView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp, never()).setHeader(argThat(new IsAnyString()),argThat(new IsAnyString()))
  }

  it should "not register user with username which is already taken" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd1")).toReturn(pwd)
    stub(req.getParameter("pwd2")).toReturn(pwd)
    stub(userColl.find(argThat(new IsAnyDBObject))).toReturn(cursor)
    stub(cursor.count).toReturn(1)

    val view = new RegisterView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).setHeader("redirect_to", "/register")
    verify(userColl, never()).insert(argThat(new IsAnyDBObject))
  }

  it should "register user with unique username and matching passwords and redirect to login page" in {
    // Arrange
    val name = "johnny1"
    val pwd = "JohnSmith"

    val context = mock[BundleContext]
    val userColl = mock[DBCollection]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val cursor = mock[DBCursor]

    stub(req.getParameter("user")).toReturn(name)
    stub(req.getParameter("pwd1")).toReturn(pwd)
    stub(req.getParameter("pwd2")).toReturn(pwd)
    stub(userColl.find(argThat(new IsAnyDBObject))).toReturn(cursor)
    stub(cursor.count).toReturn(0)

    val view = new RegisterView(context, userColl)

    // Act
    view.doPost(req, resp)

    // Assert
    verify(resp).setHeader("redirect_to", "/login")
    verify(userColl).insert(argThat(new IsAnyDBObject))
  }
}
