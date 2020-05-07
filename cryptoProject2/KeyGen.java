import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyGen {

	public static BigInteger p = BigInteger.probablePrime(512, new Random());  			
	public static BigInteger q = BigInteger.probablePrime(512, new Random());				
	public static BigInteger n = p.multiply(q);			
	public static BigInteger phi = p.subtract(BigInteger.ONE).multiply( q.subtract(BigInteger.ONE) );   		
	public static BigInteger e  = BigInteger.probablePrime(512, new Random());	
	public static BigInteger d = e.modInverse(phi);

	public KeyGen() {
		
		while (p==q) {
			q = BigInteger.probablePrime(512, new Random());
		}
				
	
		while(true){
			if(e != (p) && e != (q) && (e.gcd(phi)) != (BigInteger.ONE)){
				break;
			}
			e  = BigInteger.probablePrime(512, new Random());
		}
	}
	
	public List<BigInteger> readKeyFile(String file){
		List<BigInteger> keys = new ArrayList<BigInteger>();
		try(InputStream is = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(is);){
			
			keys.add( (BigInteger) ois.readObject()  );
			keys.add( (BigInteger) ois.readObject()  );
			
		}
		catch(Exception e){
			System.out.println("Error! Cannot Find Function!");
		}
		
		return keys;
	}

	public void writeToFile() {
		try {
			
			ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(new FileOutputStream("pubkey.rsa"));
			objectOutputStream1.writeObject(e);
			objectOutputStream1.writeObject(n);
	        objectOutputStream1.close();
	        
	        
	        ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream("privkey.rsa"));
			objectOutputStream2.writeObject(d);
			objectOutputStream2.writeObject(n);
	        objectOutputStream2.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}
	
	
	
 	
}
