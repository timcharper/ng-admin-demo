package ngadmin.db.lib

import org.scalatest.{FunSpec, Matchers}

class PasswordHasherSpec extends FunSpec with Matchers {
  import PasswordHasher._
  describe("hasher") {
    it("generates unique hashes each time") {
      val range = 0 to 100
      val passwords = range.map(_ => createHash("p\r\nassw0Rd!"))
      passwords.distinct.length should be (range.length)
    }

    it("accepts a valid password, and rejects an invalid one") {
      for(i <- 0 to 99) {
        val password = i.toString
        val hash = createHash(password)
        val secondHash = createHash(password)
        hash shouldNot equal (secondHash)

        val wrongPassword = (i+1).toString
        assert(
          ! validatePassword(wrongPassword, hash),
          s"FAILURE: WRONG PASSWORD ACCEPTED! ${wrongPassword} ${hash}")

        assert(
          validatePassword(password, hash),
          "FAILURE: GOOD PASSWORD NOT ACCEPTED!")
      }
    }
  }
}
