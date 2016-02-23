package ngadmin.db

import com.github.tminglei.slickpg._

class AppPgDriver extends ExPostgresDriver with PgDate2Support {
  override val api = MyAPI

  object MyAPI extends API with DateTimeImplicits
}
object AppPgDriver extends AppPgDriver
