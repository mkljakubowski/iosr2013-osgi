package matchers

import org.mockito.ArgumentMatcher
import com.mongodb.BasicDBObject

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
class IsAnyDBObject extends ArgumentMatcher[BasicDBObject] {
  def matches(item: Object) : Boolean = true
}
