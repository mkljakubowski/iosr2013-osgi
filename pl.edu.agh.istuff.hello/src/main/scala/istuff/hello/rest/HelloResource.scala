package istuff.hello.rest

import javax.ws.rs._
import javax.ws.rs.core._

@Path("hello")
class HelloResource {

  @GET
  def hello : String = "Hello world!"
}
