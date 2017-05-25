import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/** All the constants are named as in lecture slides(Stinson's book) */
public class ElGamal {
   
   /**
    * Method to create a BigInteger with specified bitlength
    * @param bitlength
    * @return
    */
   public static BigInteger getRandomNumber(final int bitlength){
      SecureRandom sc = new SecureRandom();
      final char[] ch = new char[bitlength];
      for(int i = 0; i < bitlength; i++){
          ch[i] =
              (char) ('0' + (i == 0 ? sc.nextInt(9) + 1 : sc.nextInt(10)));
      }
      return new BigInteger(new String(ch));
   }
   
   //Implementing ElGamal
   public static ArrayList<BigInteger> ElgamalMain(String str) {
      ArrayList<BigInteger> result = new ArrayList<BigInteger>();
      SecureRandom sc = new SecureRandom();
      
      BigInteger p, alpha, beta, a;
      p = BigInteger.probablePrime(2048, sc);
      alpha = new BigInteger("3");
      //Generate a random number a with specified bit length
      a = getRandomNumber(200);
      
      long encstartTime = System.currentTimeMillis();
      
      beta = alpha.modPow(a, p);
     
      BigInteger x = new BigInteger(str.getBytes());
      BigInteger k = new BigInteger(1024, sc);
      BigInteger Y1 = alpha.modPow(k, p);
      BigInteger Y2 = x.multiply(beta.modPow(k, p)).mod(p);
     
      long encendTime=System.currentTimeMillis();
      result.add(Y2);
      System.out.println("Encryption = " + Y2);
      System.out.println("Encryption took "+(encendTime-encstartTime)+"ms");
      //Decryption
      long decstartTime = System.currentTimeMillis();
      BigInteger plaintext = Y2.multiply(Y1.modPow(a, p).modInverse(p)).mod(p);
      String decrypted=new String(plaintext.toByteArray());
      long decEndTime = System.currentTimeMillis();
      result.add(plaintext);
      System.out.println("Decryption: " + decrypted);
      System.out.println("Decryption took "+(decEndTime-decstartTime)+"ms");
      
      /** Memory calculation */
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