package istuff.database.service.api

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 4/19/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
trait DatabaseApi {
  def getData(collection : String, propertyName : String, propertyValue : String) : String
  def setData(collection : String, name : String, content : String)
}
