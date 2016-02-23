package ngadmin.db.lib

import java.security.SecureRandom
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKeyFactory
// import java.math.BigInteger
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException

/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */
object PasswordHasher
{
  val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1"

  // The following constants may be changed without breaking existing hashes.
  val SALT_BYTES = 24
  val HASH_BYTES = 24
  val PBKDF2_ITERATIONS = 1000

  val ITERATION_INDEX = 0
  val SALT_INDEX = 1
  val PBKDF2_INDEX = 2

  /**
    * Returns a salted PBKDF2 hash of the password.
    *
    * @param   password    the password to hash
    * @return              a salted PBKDF2 hash of the password
    */
  def createHash(password: String): String =
    createHash(password.toCharArray())


  /**
    * Returns a salted PBKDF2 hash of the password.
    *
    * @param   password    the password to hash
    * @return              a salted PBKDF2 hash of the password
    */
  def createHash(password: Array[Char]): String = {
    // Generate a random salt
    val random = new SecureRandom()
    val salt = Array.ofDim[Byte](SALT_BYTES)
    random.nextBytes(salt)

    // Hash the password
    val hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES)
    // format iterations:salt:hash
    return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash)
  }

  /**
    * Validates a password using a hash.
    *
    * @param   password    the password to check
    * @param   goodHash    the hash of the valid password
    * @return              true if the password is correct, false if not
    */
  def validatePassword(password: String, goodHash: String): Boolean = {
    validatePassword(password.toCharArray(), goodHash)
  }

  /**
    * Validates a password using a hash.
    *
    * @param   password    the password to check
    * @param   goodHash    the hash of the valid password
    * @return              true if the password is correct, false if not
    */
  def validatePassword(password: Array[Char], goodHash: String): Boolean = {
    // Decode the hash into its parameters
    val params = goodHash.split(":")
    val iterations = Integer.parseInt(params(ITERATION_INDEX))
    val salt = fromHex(params(SALT_INDEX))
    val hash = fromHex(params(PBKDF2_INDEX))
    // Compute the hash of the provided password, using the same salt,
    // iteration count, and hash length
    val testHash = pbkdf2(password, salt, iterations, hash.length)
    // Compare the hashes in constant time. The password is correct if
    // both hashes match.
    slowEquals(hash, testHash)
  }

  /**
    * Compares two byte arrays in length-constant time. This comparison method
    * is used so that password hashes cannot be extracted from an on-line 
    * system using a timing attack and then attacked off-line.
    * 
    * @param   a       the first byte array
    * @param   b       the second byte array 
    * @return          true if both byte arrays are the same, false if not
    */
  private def slowEquals(a: Array[Byte], b: Array[Byte]): Boolean = {
    var diff = a.length ^ b.length
    for(i <- Range(0, Math.min(a.length, b.length)))
      diff |= a(i) ^ b(i)
    diff == 0
  }

  /**
    *  Computes the PBKDF2 hash of a password.
    *
    * @param   password    the password to hash.
    * @param   salt        the salt
    * @param   iterations  the iteration count (slowness factor)
    * @param   bytes       the length of the hash to compute in bytes
    * @return              the PBDKF2 hash of the password
    */
  private def pbkdf2(password: Array[Char], salt: Array[Byte], iterations: Int, bytes: Int): Array[Byte] = {
    val spec = new PBEKeySpec(password, salt, iterations, bytes * 8)
    val skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
    skf.generateSecret(spec).getEncoded()
  }

  private def fromHex(hex: String): Array[Byte] = {
    val binary = Array.ofDim[Byte](hex.length() / 2)
    for (i <- Range(0, binary.length)) {
      binary(i) = Integer.parseInt(hex.substring(2*i, 2*i+2), 16).toByte
    }
    binary
  }

  /**
    * Converts a byte array into a hexadecimal string.
    *
    * @param   array       the byte array to convert
    * @return              a length*2 character string encoding the byte array
    */
  private def toHex(array: Array[Byte]): String = {
    val bi = BigInt(1, array)
    val hex = bi.toString(16)
    val paddingLength = (array.length * 2) - hex.length()
    if(paddingLength > 0)
      return ("0" * paddingLength) + hex
    else
      hex
  }

}
