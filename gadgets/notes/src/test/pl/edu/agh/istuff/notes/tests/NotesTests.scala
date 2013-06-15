package pl.edu.agh.istuff.notes.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import pl.edu.agh.istuff.notes.impl.NotesServlet
import org.mockito.Mockito._

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class NotesTests extends FlatSpec with MockitoSugar{

  "Notes" should "redirect to correct resource" in {
    // Arrange
    val name = "notes"
    val version = 1

    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    val view = new NotesServlet(name, version)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).sendRedirect("/"+name+"/" + version + "/notes.html")
  }

  it should "set correct content type" in {
    // Arrange
    val name = "notes"
    val version = 1

    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    val view = new NotesServlet(name, version)

    // Act
    view.doGet(req, resp)

    // Assert
    verify(resp).setContentType("text/html;charset=UTF-8")
  }

}
