package pl.edu.agh.istuff.time.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito._
import pl.edu.agh.istuff.time.impl.CurrentTimeServlet

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class TimeTests extends FlatSpec with MockitoSugar{

  "Time" should "redirect to correct resource" in {
    // Arrange
    val name = "time"
    val version = 1

    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    val view = new CurrentTimeServlet(name, version)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).sendRedirect("/"+name+"/"+version+"/index.html")
  }

  it should "set correct content type" in {
    // Arrange
    val name = "notes"
    val version = 1

    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    val view = new CurrentTimeServlet(name, version)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).setContentType("text/html;charset=UTF-8")
  }

}
