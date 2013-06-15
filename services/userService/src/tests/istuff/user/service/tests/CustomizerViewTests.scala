package istuff.user.service.tests

import org.scalatest.FlatSpec
import com.mongodb.{DBCollection, DB}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar
import org.osgi.framework.BundleContext
import istuff.database.service.impl.DatabaseImpl

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class CustomizerViewTests extends FlatSpec with MockitoSugar{

  "CustomerView" should "supply injected DB" in {
    // Arrange

    // Act

    // Assert
  }

}
