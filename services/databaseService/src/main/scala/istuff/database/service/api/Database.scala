package istuff.database.service.api

trait Database {
  def getData(collection : String, propertyName : String, propertyValue : String) : Option[String]
  def setData(collection : String, name : String, content : String)
}
