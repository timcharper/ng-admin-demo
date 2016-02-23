package ngadmin.protocol

import java.time.ZonedDateTime
import java.util.UUID


case class CreateBucket(

)

case class BucketView(
  bucket_id: UUID,
  created: ZonedDateTime)
