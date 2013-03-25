package istuff.hello.rest

import javax.ws.rs._
import core.MediaType

@Path("hello")
class HelloResource {

  @GET @Produces(MediaType.TEXT_PLAIN)
  def hello : String = "Hello world!"
}
