package dispatch.spec

import org.scalacheck._

object BasicSpecification
extends Properties("Json4s Native Json")
with DispatchCleanup {
  import Prop.forAll

  import org.json4s._
  import org.json4s.native.JsonMethods._
  import JsonDSL._

  object Json {
    import unfiltered.response._
    def jsonToString(json: JValue) = compact(render(json))

    def apply(json: JValue): ComposeResponse[io.netty.handler.codec.http.HttpResponse] =
      new ComposeResponse(JsonContent ~> ResponseString(jsonToString(json)))

    def apply(json: JValue, cb: String): ComposeResponse[io.netty.handler.codec.http.HttpResponse] =
      new ComposeResponse(JsContent ~> ResponseString("%s(%s)" format(cb, jsonToString(json))))
  }

  private val port = unfiltered.util.Port.any
  val server = {
    import unfiltered.netty
    import unfiltered.request._

    object In extends Params.Extract("in", Params.first)
    netty.Server.local(port).handler(netty.cycle.Planify {
      case Params(In(in)) => Json(("out" -> in))
    }).start()
  }

  import dispatch._

  def localhost = host("127.0.0.1", port)

  property("parse json") = forAll(Gen.alphaStr) { (sample: String) =>
    val res = Http.default(
      localhost <:< Map("Accept" -> "application/json") <<? Map("in" -> sample) > as.json4s.Json
    )
    sample == (for { JObject(fields) <- res(); JField("out", JString(o)) <- fields } yield o).head
  }
}
