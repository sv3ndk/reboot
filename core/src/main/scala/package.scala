package object dispatch {
  /** Type alias for Response, avoid need to import */
  type Res = org.asynchttpclient.Response
  /** Type alias for URI, avoid need to import */
  type Uri = java.net.URI

  implicit def implyRequestHandlerTuple(builder: Req): RequestHandlerTupleBuilder =
    new RequestHandlerTupleBuilder(builder)


  implicit def implyRunnable[U](f: () => U): Runnable = () => {
    f(); ()
  }

  implicit def enrichFuture[T](future: Future[T]): EnrichedFuture[T] =
    new EnrichedFuture(future)

  /** Type alias to scala.concurrent.Future so you don't have to import */
  type Future[+T] = scala.concurrent.Future[T]

  val Future = scala.concurrent.Future
}
