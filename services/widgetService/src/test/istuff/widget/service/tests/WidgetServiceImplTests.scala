package istuff.widget.service.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.BundleContext
import istuff.widget.service.impl.WidgetServiceImpl
import istuff.api.models.widget.WidgetDescriptor
import javax.servlet.http.HttpServlet

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class WidgetServiceImplTests extends FlatSpec with MockitoSugar{

  "WidgetService" should "properly register widget" in {
    // Arrange
    val context = mock[BundleContext]
    val service = new WidgetServiceImpl
    val name = "notes"
    val servlet = mock[HttpServlet]
    val descriptor = new WidgetDescriptor(name,1,"bursant", Map((name, servlet)), List(""), 400, 400, 100, 100)

    // Act
    service.registerWidget(descriptor, context)

    // Assert
    assert(service.registredWidgets.count( x => x._1 == descriptor) == 1)
  }

  "WidgetServiceImpl" should "properly unregister widget" in {
    // Arrange
    val context = mock[BundleContext]
    val service = new WidgetServiceImpl
    val name = "notes"
    val servlet = mock[HttpServlet]
    val descriptor = new WidgetDescriptor(name,1,"bursant", Map((name, servlet)), List(""), 400, 400, 100, 100)

    // Act
    service.registredWidgets:+(descriptor, null)
    service.unRegisterWidget(descriptor)

    // Assert
    assert(service.registredWidgets.count( x => x._1 == descriptor) == 0)
  }

  "WidgetServiceImpl" should "return available widgets" in {
    // Arrange
    val context = mock[BundleContext]
    val service = new WidgetServiceImpl
    val name1 = "notes1"
    val name2 = "notes2"
    val servlet = mock[HttpServlet]
    val descriptor1 = new WidgetDescriptor(name1,1,"bursant", Map((name1, servlet)), List(""), 400, 400, 100, 100)
    val descriptor2 = new WidgetDescriptor(name2,1,"bursant", Map((name2, servlet)), List(""), 400, 400, 100, 100)

    // Act
    service.registerWidget(descriptor1, context)
    service.registerWidget(descriptor2, context)
    val list = service.getAvailableWidgets()

    // Assert
    assert(list.contains(descriptor1))
    assert(list.contains(descriptor2))

  }
}
