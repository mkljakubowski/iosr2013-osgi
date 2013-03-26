package istuff.hello.rest

import javax.ws.rs._
import core.MediaType

@Path("hello")
class HelloResource {

  @GET @Produces(Array(MediaType.TEXT_PLAIN))
  def hello = "Hello world!"
}
