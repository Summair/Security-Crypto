/* This class defines methods for encrypting and decrypting using the Triple DES algorithm and for generating, reading, and writing Triple DES keys
 *
 * 
 *it also defines a main() method that allows these methods to be used from the command line 
 * 
 * 
 */


import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;



public class TripleDES {


/* the first arguments to run the program must be -e -d, or -g to encrypt of decrypt or generate a key
 * the second argymane if the name of a file from which the key is read or to which it is written for -g
 * the -e and the -d arguments cause the program to read from the standard
 */

	
	
	
	
	public static void main(String[] args) {

		try {
			 try {
				 	Cipher c = Cipher.getInstance("DESede"); }
			 catch (Exception e) {
				// this catch try block will try to find if these is already a provide installed on your system  to do TripleDES Encryption
			
			
// not present then we install *The Java Cryptography Extension (JCE) from Sun Microsystems is an optional package to the Java 2 platform. It is a framework for implementing encryption, key generation and key agreement, and Message Authentication Code (MAC) algorithms.
// Oracle JCE Provider package supplies a concrete implementation of a subset of the cryptographic services defined in JCE 1.2.1.
	
			 System.err.println("installing SunJCE provider");
			 Provider sunjce = new com.sun.crypto.provider.SunJCE();
			 Security.addProvider(sunjce);
		}
		
		// block where read the key from or write it to 
		File keyfile = new File(args[1]);
		
		if (args[0].contentEquals("-g")) {
			
			System.out.print("Generating key. this may take some time..");
			System.out.flush();
			SecretKey key = generateKey();
			writekey(key, keyfile);
			System.out.println("done");
			System.out.println("Secret key written to " + args[1] +".Protect that file carefully!");
		}
		else if (args[0].contentEquals("-e")) {
			
			SecretKey key = readKey(keyfile);
			encrypt( key, System.in, System.out);
		}
	
	else if (args[0].equals("-d ")){
		SecretKey key = readKey(keyfile);
		decrypt(key, System.in, System.out);
		}
	
	}
	catch(Exception e) {
	 System.err.println(e);
	System.err.println("Usage: Java" + TripleDES.class.getName() + "-d| -e| -g <keyfile>");
		
		
			
	}
}

/*
 * Generate a secret TripleDES encryption/decryption key
 */
		
 public static SecretKey generateKey() throws NoSuchAlgorithmException {
	 
	 KeyGenerator keygen = KeyGenerator.getInstance("DESede");
	 
	 return keygen.generateKey();
	 
		
	 }
public static void writekey(SecretKey key, File f)
throws IOException, NoSuchAlgorithmException, InvalidKeySpecException

{
	SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
	DESedeKeySpec keyspec = 
			(DESedeKeySpec)keyfactory.getKeySpec(key, DESedeKeySpec.class);
	byte[] rawkey = keyspec.getKey();
 
	 FileOutputStream out = new FileOutputStream(f);
	 out.write(rawkey);
	 out.close();
}

public static SecretKey readKey(File f)
	throws IOException, NoSuchAlgorithmException,
	       InvalidKeyException, InvalidKeySpecException


{
	DataInputStream in = new DataInputStream(new FileInputStream(f));
	byte[] rawkey = new byte[(int)f.length()];
	in.readFully(rawkey);
	in.close();
	
	DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
	SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
	SecretKey key = keyfactory.generateSecret(keyspec);
	return key;
	
	}

	public static void encrypt(SecretKey key, InputStream in, OutputStream out) 
		
	throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException
		{
		 Cipher cipher = Cipher.getInstance("DESede");
		 cipher.init(cipher.ENCRYPT_MODE, key);
			
		 {
	//https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html on documentation of Cipher class
	
	CipherOutputStream cos = new CipherOutputStream(out, cipher);

		byte[] buffer = new byte[2048];
		int bytesRead;
	
	while((bytesRead = in.read(buffer)) !=1) {
			
			cos.write(buffer, 0, bytesRead);
			
				}
		
		cos.close();

		java.util.Arrays.fill(buffer, (byte) 0 );
		
		 }

	
	//Decryption methods 
	
public static void decrypt(SecretKey key, InputStream in, OutputStream out)
throws NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, NoSuchPaddingException,
BadPaddingException
		
{
	
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.DECRYPT_MODE,key);
		
		
		byte[] buffer = new byte[2048];
		int bytesRead;
		while((bytesRead = in.read(buffer)) !=-1) {
			out.write(cipher.update(buffer, 0, bytesRead));
	
	}

		 out.write(cipher.doFinal());
		 out.flush();
		}
	}
