package istuff.database.service.api

import com.mongodb.DB

trait Database {
  def getDB() : DB
}
