package istuff.database.service.tests

import org.scalatest.FlatSpec
import com.mongodb.DB
import istuff.database.service.impl.DatabaseImpl
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class DatabaseTests extends FlatSpec with MockitoSugar{

  "Database service" should "supply injected DB" in {
    // Arrange
    val db = mock[DB]
    val databaseService = new DatabaseImpl(db)

    // Act
    val obtainedDB = databaseService.getDB()

    // Assert
    assert(db.equals(obtainedDB))
  }

  it should "always return the same DB" in {
    // Arrange
    val db = mock[DB]
    val databaseService = new DatabaseImpl(db)

    // Act
    val db1 = databaseService.getDB()
    var db2 = databaseService.getDB()

    // Assert
    assert(db1.equals(db2))
  }
}