package ngadmin

import play.api.libs.json._
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.model._

object AppMarshalling {
  implicit val unitMarshaller:
      ToEntityMarshaller[Unit] =
    Marshaller.opaque { _ => HttpEntity.Empty }

  implicit def fromJson[T](implicit reader: Reads[T]): FromEntityUnmarshaller[T] = {
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(MediaTypes.`application/json`)
      .mapWithCharset { (data, charset) =>
        val input =
          if (charset == HttpCharsets.`UTF-8`)
            data.utf8String
          else
            data.decodeString(charset.nioCharset.name)
        Json.parse(input).as[T]
    }
  }

  implicit def toJson[T](implicit writer: Writes[T]): ToEntityMarshaller[T] =
    Marshaller.stringMarshaller(MediaTypes.`application/json`).
      compose { data: T =>
        Json.stringify(Json.toJson(data)) }
}
