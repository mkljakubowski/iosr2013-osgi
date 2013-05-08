package istuff.database.service.impl

import com.mongodb._
import istuff.database.service.api._
import istuff.api.util.Loggable

class DatabaseImpl(db : DB) extends Database {

  val database = db

  def getDB() : DB = database

}
