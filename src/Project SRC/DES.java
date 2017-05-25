import java.util.ArrayList;

//Variable names follow Stallings book
public class DES {
   
   //All the below definitions of DES are taken from Stallings book
    /**
     * Input Permutation used to permuted the message block
     */
   private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    /**
     * Final Permutation:the final result is permuted by this
     * permutation to generate the final ciphertext
     */
    private static final byte[] IPinv = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    /**
     * Expansion Permutation: the Feistel function begins by applying
     * this permutation to its 32-bit input half-block to create a 48-bit value.
     */
    private static final byte[] E = {
            32, 1,  2,  3,  4,  5,
            4,  5,  6,  7,  8,  9,
            8,  9,  10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    /**
     * S-Boxes A 48-bit value is split into 6-bit sections, and each section is permuted
     * into a different 6-bit value according to these eight tables.
     */
    private static final byte[][] S = { {
            14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
            0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
            4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
            15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
            15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
            3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
            0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
            13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
            10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
            13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
            13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
            1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
            7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
            13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
            10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
            3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
            2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
            14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
            4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
            11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
            12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
            10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
            9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
            4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
            4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
            13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
            1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
            6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
            13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
            1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
            7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
            2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };

    /**
     * Permutation function: The Feistel function concludes by applying this
     * 32-bit permutation to the result of the  substitution
     */
    private static final byte[] P = {
            16, 7,  20, 21, 29, 12, 28, 17,
            1,  15, 23, 26, 5,  18, 31, 10,
            2,  8,  24, 14,32, 27, 3,  9,
            19, 13, 30, 6, 22, 11, 4,  25
    };

    /**
     * Permuted Choice 1: The supplied 64-bit key is permuted according
     * to this table into a 56-bit key
     */
    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1,  58, 50, 42, 34, 26, 18,
            10, 2,  59, 51, 43, 35, 27,
            19, 11, 3,  60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7,  62, 54, 46, 38, 30, 22,
            14, 6,  61, 53, 45, 37, 29,
            21, 13, 5,  28, 20, 12, 4
    };

    /**
     * PC2 Permutation: The subkey generation process applies this
     * permutation to transform its running 56-bit keystuff value into
     * the final set of 16 48-bit subkeys
     */
    private static final byte[] PC2 = {
            14, 17, 11, 24, 1,  5,
            3,  28, 15, 6,  21, 10,
            23, 19, 12, 4,  26, 8,
            16, 7,  27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    /**
     * Schedule of left shifts:  Part of the subkey generation process
     * involves rotating certain bit-sections of the keystuff by either
     * one or two bits to the left.  This table specifies how many bits
     * to rotate left for each of the 16 rounds
     */
    private static final byte[] leftShifts = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    //methods for performing the basic permutations.
    private static long IP(long src)  { return permute(IP, 64, src);                 } // 64-bit output
    private static long IPinv(long src)  { return permute(IPinv, 64, src);           } // 64-bit output
    private static long E(int src)    { return permute(E, 32, src&0xFFFFFFFFL);      } // 48-bit output
    private static int  P(int src)    { return (int)permute(P, 32, src&0xFFFFFFFFL); } // 32-bit output
    private static long PC1(long src) { return permute(PC1, 64, src);                } // 56-bit output
    private static long PC2(long src) { return permute(PC2, 56, src);                } // 48-bit output

    /**
     * Permute an input according to the supplied permutation table
     */
    private static long permute(byte[] table, int inputSize, long input) {
        long result = 0;
        for (int i=0; i<table.length; i++) {
            int position = inputSize - table[i];
            result = (result<<1) | (input>>position & 0x01);
        }
        return result;
    }

    /**
     * Permute the supplied 6-bit value based on the S-Box at the
     * specified box number
     */
    private static byte S(int boxNumber, byte input) {
        // transform the 6-bit input into an
        // absolute index based on bit shuffle:
       input = (byte) (input&0x20 | ((input&0x01)<<4) | ((input&0x1E)>>1));
       return S[boxNumber-1][input];
    }

    /**
     * Utility method to convert 8 bytes into a single 64-bit long
     * value from a specified offset. If the supplied byte array does not contain 8 elements
     * starting at offset, the missing bytes are regarded as zero padding
     */
    private static long convertBytestoLong(byte[] input, int offset) {
        long l = 0;
        for (int i=0; i<8; i++) {
            byte value;
            if ((offset+i) < input.length) {
                value = input[offset+i];
            } else {
                value = 0;
            }
            l = l<<8 | (value & 0xFFL);
        }
        return l;
    }

    /**
     * Utility method to convert a 64-bit long value into eight bytes,
     * which are written into the supplied byte array at the specified
     * offset. If the destination byte array does not have eight bytes
     * starting at offset, the remaining bytes are discarded
     */
    private static void convertBytestoLong(byte[] input, int offset, long l) {
        for (int i=7; i>=0; i--) {
            if ((offset+i) < input.length) {
               input[offset+i] = (byte) (l & 0xFF);
                l = l >> 8;
            } else {
                break;
            }
        }
    }

    /**
     * The Feistel function
     */
    private static int feistel(int r, long subkey) {
        //expansion
        long e = E(r);
        //key mixing
        long x = e ^ subkey;
        //substitution
        int dst = 0;
        for (int i=0; i<8; i++) {
            dst>>>=4;
            int s = S(8-i, (byte)(x&0x3F));
            dst |= s << 28;
            x>>=6;
        }
        //permutation
        return P(dst);
    }

    /**
     * Generate 16 48-bit subkeys based on the provided 64-bit key
     */
    private static long[] createSubkeys(long key) {
        long subkeys[] = new long[16];

        //perform the PC1 permutation
        key = PC1(key);

        //split into 28-bit left and right pairs.
        int l = (int) (key>>28);
        int r = (int) (key&0x0FFFFFFF);

        //Generate 16 subkeys, perform a bit
        //rotation on each 28-bit half, then join
        //the halves together and permute to generate the
        //subkey
        for (int i=0; i<16; i++) {
            //rotate the 28-bit values
            if (leftShifts[i] == 1) {
                //rotate by 1 bit
                l = ((l<<1) & 0x0FFFFFFF) | (l>>27);
                r = ((r<<1) & 0x0FFFFFFF) | (r>>27);
            } else {
                //rotate by 2 bits
                l = ((l<<2) & 0x0FFFFFFF) | (l>>26);
                r = ((r<<2) & 0x0FFFFFFF) | (r>>26);
            }

            //join the two halves together.
            long cd = (l&0xFFFFFFFFL)<<28 | (r&0xFFFFFFFFL);

            //perform the PC2 permutation
            subkeys[i] = PC2(cd);
        }

        return subkeys;
    }

    /**
     * Encrypt a block of plaintext message(64 bit) into ciphertext (64 bit)
     */
    public static long encrypt(long m, long key, boolean decrypt) {
        // generate the 16 subkeys
        long subkeys[] = createSubkeys(key);

        // perform the initial permutation
        long ip = IP(m);

        // split the 32-bit value into 16-bit left and right halves
        int l = (int) (ip>>32);
        int r = (int) (ip&0xFFFFFFFFL);

        // perform 16 rounds
        for (int i=0; i<16; i++) {
            int previous_l = l;
            // the right half becomes the new left half
            l = r;
            // the Feistel function is applied to the old left half
            // and the resulting value is stored in the right half
            r = previous_l ^ feistel(r, subkeys[decrypt ? (15-i) : i]);
        }

        // reverse the two 32-bit segments (left to right; right to left)
        long rl = (r&0xFFFFFFFFL)<<32 | (l&0xFFFFFFFFL);

        // apply the final permutation
        long ciphertext = IPinv(rl);

        // return the ciphertext
        return ciphertext;
    }

    /**
     * Wrapper around encrypt() that allows arguments to be byte
     * arrays instead of long
     */
    public static void encrypt(byte[] message, int messageOffset, byte[] ciphertext, 
          int ciphertextOffset,byte[] key,boolean decrypt) {
        long m = convertBytestoLong(message, messageOffset);
        long k = convertBytestoLong(key, 0);
        long c = encrypt(m, k, decrypt);
        convertBytestoLong(ciphertext, ciphertextOffset, c);
    }

    /**
     * Encrypt the supplied message with the provided key, and return
     * the ciphertext.  If the message is not a multiple of 64 bits
     * (8 bytes), then it is padded with zeros
     * Each 64-bit block is encrypted individually with the same key
     */
    public static byte[] encrypt(byte[] message, byte[] key) {
        //pad the ciphertext array to the next multiple of 8
        int extraBytes = 0;
        if (message.length%8 != 0) {
            extraBytes = 8-(message.length%8);
        }
        byte[] ciphertext = new byte[message.length+extraBytes];

        //encrypt each 8-byte (64-bit) block of the message.
        for (int i=0; i<message.length; i+=8) {
           encrypt(message, i, ciphertext, i, key, false);
        }

        return ciphertext;
    }

    /**
     * Decrypt the supplied ciphertext with the provided key, and return
     * the plaintext. If the message is not a multiple of 64 bits
     * (8 bytes), then it is padded with zeros
     */
    public static byte[] decrypt(byte[] message, byte[] key) {
        byte[] plaintext = new byte[message.length];

        // encrypt each 8-byte (64-bit) block of the message.
        for (int i=0; i<message.length; i+=8) {
           encrypt(message, i, plaintext, i, key, true);
        }

        return plaintext;
    }

    /**
     * Encrypt the supplied message with the provided key, and return
     * the ciphertext
     * The provided password is converted into a key with the bits
     * of each byte reversed, to generate a stronger key
     */
    public static byte[] encrypt(byte[] message, String key) {
        return encrypt(message, stringtoBytes(key));
    }

    /**
     * Convert a password string into a byte array, reversing the bits
     * of each byte to place more useful key bits into non-discarded
     * bit-positions of the 64-bit DES key input
     */
    private static byte[] stringtoBytes(String input) {
        byte[] bytes = input.getBytes();
        byte[] key = new byte[8];
        for (int i=0; i<8; i++) {
            if (i < bytes.length) {
                byte b = bytes[i];
                // flip the byte
                byte b2 = 0;
                for (int j=0; j<8; j++) {
                    b2<<=1;
                    b2 |= (b&0x01);
                    b>>>=1;
                }
                key[i] = b2;
            } else {
                key[i] = 0;
            }
        }
        return key;
    }
    
    //Utility methods
    
    private static int charToNibble(char c) {
        if (c>='0' && c<='9') {
            return (c-'0');
        } else if (c>='a' && c<='f') {
            return (10+c-'a');
        } else if (c>='A' && c<='F') {
            return (10+c-'A');
        } else {
            return 0;
        }
    }
    
    //parse bytes
    private static byte[] parseBytes(String s) {
        s = s.replace(" ", "");
        byte[] result = new byte[s.length()/2];
        if (s.length()%2 > 0) { s = s+'0'; }
        for (int i=0; i<s.length(); i+=2) {
           result[i/2] = (byte) (charToNibble(s.charAt(i))<<4 | charToNibble(s.charAt(i+1)));
        }
        return result;
    }
    
    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<bytes.length; i++) {
            sb.append(String.format("%02X",bytes[i]));
        }
        return sb.toString();
    }

    //Encryption
    public static String encrypt(String plaintext, byte[] key) {
        return hex(encrypt(plaintext.getBytes(), key));
    }

    //Decryption
    public static String decrypt(String ciphertext, byte[] key) {
        byte[] plaintext = DES.decrypt(parseBytes(ciphertext), key);

        //remove trailing zero bytes
        byte[] result = null;
        for (int i=(plaintext.length-1); i>=0; i--) {
            if ((result == null) && (plaintext[i] != 0)) {
               result = new byte[i+1];
            }
            if (result != null) {
               result[i] = plaintext[i];
            }
        }
        if (result == null) {
            return "";
        } else {
            return new String(result);
        }
    }
    
    //Driver main function
    public static ArrayList<String> main(String str) throws Exception {
       System.out.println("DES System"); 
       ArrayList<String> list=new ArrayList<String>();

       long EncstartTime=System.currentTimeMillis();
       String plaintext=str;
       String ciphertext=encrypt(plaintext,"security".getBytes());
       list.add(ciphertext);
       long EncendTime=System.currentTimeMillis();
       System.out.println("Encryption took "+(EncendTime-EncstartTime)+"ms");
       System.out.println("Ciphertext is: "+ciphertext);
       long DecstartTime=System.currentTimeMillis();
       String plainText=decrypt(ciphertext,"security".getBytes());
       System.out.println("Plaintext retrieved back is :"+plainText);
       long DecendTime=System.currentTimeMillis();
       list.add(plainText);
       System.out.println("Decryption took "+(DecendTime-DecstartTime)+"ms");
       
       /** Memory calculation */
       Runtime runtime = Runtime.getRuntime();
       //Run the garbage collector
       runtime.gc();
       //Calculate the used memory
       long memory = runtime.totalMemory() - runtime.freeMemory();
       double kb = memory/1000;
       System.out.println("Used memory is kilobytes: " + kb);

       return list;
   }
}