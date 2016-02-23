package ngadmin

import db.schema.{User,CreateUser,UpdateUser,ReadUser,CreateBucket,Bucket}
import play.api.libs.json._
import java.time.ZonedDateTime
import java.util.UUID

object JsonFormats {
  implicit val uuidFormat = new Format[UUID] {
    def reads(a: JsValue): JsResult[UUID] =
      a.validate[String].map(UUID.fromString)

    def writes(u: UUID): JsValue =
      JsString(u.toString)
  }

  implicit val zonedTimeTimeFormat = new Format[ZonedDateTime] {
    def reads(a: JsValue): JsResult[ZonedDateTime] =
      a.validate[String].map(ZonedDateTime.parse)

    def writes(d: ZonedDateTime): JsValue =
      JsString(d.toString)
  }

  implicit val byteArrayFormat = new Format[Array[Byte]] {
    def reads(v: JsValue): JsResult[Array[Byte]] =
      v.validate[String].map(
        java.util.Base64.getDecoder.decode)
    def writes(a: Array[Byte]): JsValue =
      JsString(java.util.Base64.getEncoder.encodeToString(a))
  }

  implicit val createUserFormat = Json.reads[CreateUser]
  implicit val readUserFormat = Json.writes[ReadUser]
  implicit val updateUserFormat = Json.reads[UpdateUser]
  implicit val createBucketFormat = Json.reads[CreateBucket]

  implicit val bucketFormat = Json.format[Bucket]
}
