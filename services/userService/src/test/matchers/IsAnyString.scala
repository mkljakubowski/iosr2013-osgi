package matchers

import org.mockito.ArgumentMatcher

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 6/15/13
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
class IsAnyString extends ArgumentMatcher[String] {
  def matches(item: Object) : Boolean = true
}

