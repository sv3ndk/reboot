package dispatch

package object oauth {
  implicit def implySigningVerbs(builder: Req): SigningVerbs =
    new SigningVerbs(builder)
}
