
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/** All the constants are named as in lecture slides(Stinson's book) */
public class RSA {
   BigInteger n, d, e;
   //Going beyond a bitlength of 1024 makes RSA slow, so we worked with 1024 bit length
   int bitlength = 2048;

  //Create an instance that can both encrypt and decrypt
  public RSA() {
    //provides a cryptographically strong random number generator
    SecureRandom r = new SecureRandom();
    
    //probablePrime returns a positive BigInteger that is probably prime, with the specified bitLength
    BigInteger p = BigInteger.probablePrime(bitlength, r);
    BigInteger q = BigInteger.probablePrime(bitlength, r);
    n = p.multiply(q);
    BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q
        .subtract(BigInteger.ONE));
    e = BigInteger.probablePrime(bitlength, r);
    
    //Find d such that ed = 1 mod (phi)
    d = e.modInverse(phi);
  }

  //Encrypt the given plaintext message(x^e mod n)
  public BigInteger encrypt(BigInteger message) {
    return message.modPow(e, n);
  }

  //Decrypt the given ciphertext message
  public BigInteger decrypt(BigInteger message) {
    return message.modPow(d, n);
  }

  //Test program
  public static ArrayList<BigInteger> main(String str) throws Exception{
	 System.out.println("RSA System");
	 ArrayList<BigInteger> result=new ArrayList<BigInteger>();
	 RSA rsa = new RSA();
	 //Note the start time
    long encstartTime = System.currentTimeMillis();
    
    BigInteger plaintext = new BigInteger(str.getBytes());
    BigInteger ciphertext = rsa.encrypt(plaintext);
    
    //Note the end time
    long encendTime = System.currentTimeMillis();
    
    System.out.println("Encryption took "+(encendTime-encstartTime)+"ms");
    System.out.println("Ciphertext: " + ciphertext);
    result.add(ciphertext);
    
    long decstartTime = System.currentTimeMillis();
    plaintext = rsa.decrypt(ciphertext);
    String decrypted = new String(plaintext.toByteArray());
    long decendTime = System.currentTimeMillis();
    result.add(plaintext);
    System.out.println("Plaintext: " + decrypted);
    System.out.println("Decryption took "+(decendTime-decstartTime)+"ms");
    
    /**Memory calculation*/
    Runtime runtime = Runtime.getRuntime();
    //Run the garbage collector
    runtime.gc();
    //Calculate the used memory
    long memory = runtime.totalMemory() - runtime.freeMemory();
    double kb = memory/1000;
    System.out.println("Used memory is kilobytes: " + kb);
    return result;
  }
}