
public class TripleSDES {
	
	public static void main(String[] args) 
	{
		run();
	}//End Main
	
	public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext )
	{
				byte[] encryptedText = SDES.Encrypt(rawkey1, plaintext);
			byte[] decryptedText = SDES.Decrypt(rawkey2, encryptedText);
		byte[] cipherText = SDES.Encrypt(rawkey1, decryptedText);
		
		return cipherText;
	}//End Encrypt
	
	public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext ) 
	{
				byte[] decryptedText = SDES.Decrypt(rawkey1, ciphertext);
			byte[] encryptedText = SDES.Encrypt(rawkey2, decryptedText);
		byte[] plaintext = SDES.Decrypt(rawkey1, encryptedText);
		
		return plaintext;
	}//End decrypt
	public static void print(byte[] b)
	{
		int i = b.length;
		for(int x = 0; i > x; x++) {
			System.out.print(b[x]);
		}
	}//End print
	
	public static void run() 
	{
		//1
		byte[] rawkey1a = {0,0,0,0,0,0,0,0,0,0};
		byte[] rawkey2a = {0,0,0,0,0,0,0,0,0,0};
		byte[] plaintextA={0,0,0,0,0,0,0,0};
		//2
		byte[] rawkey1b = {1,0,0,0,1,0,1,1,1,0};
		byte[] rawkey2b = {0,1,1,0,1,0,1,1,1,0};
		byte[] plaintextB={1,1,0,1,0,1,1,1};
		//3
		byte[] rawkey1c = {1,0,0,0,1,0,1,1,1,0};
		byte[] rawkey2c = {0,1,1,0,1,0,1,1,1,0};
		byte[] plaintextC={1,0,1,0,1,0,1,0};
		//4
		byte[] rawkey1d = {1,1,1,1,1,1,1,1,1,1};
		byte[] rawkey2d = {1,1,1,1,1,1,1,1,1,1};
		byte[] plaintextD={1,0,1,0,1,0,1,0};
		
		//5
		byte[] rawkey1e = {1,0,0,0,1,0,1,1,1,0};
		byte[] rawkey2e = {0,1,1,0,1,0,1,1,1,0};
		byte[] cipher1 ={1,1,1,0,0,1,1,0};

		//6
		byte[] rawkey1f = {1,0,1,1,1,0,1,1,1,1};
		byte[] rawkey2f = {0,1,1,0,1,0,1,1,1,0};
		byte[] cipher2 ={0,1,0,1,0,0,0,0};
		
		//7
		byte[] rawkey1g = {0,0,0,0,0,0,0,0,0,0};
		byte[] rawkey2g = {0,0,0,0,0,0,0,0,0,0};
		byte[] cipher3 ={1,0,0,0,0,0,0,0};
		
		//8
		byte[] rawkey1h = {1,1,1,1,1,1,1,1,1,1};
		byte[] rawkey2h = {1,1,1,1,1,1,1,1,1,1};
		byte[] cipher4 ={1,0,0,1,0,0,1,0};
		System.out.println("Raw Key1        Raw Key2      Plaintext      CipherText");
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1a),SDES.ByteArrayToString(rawkey2a),SDES.ByteArrayToString(plaintextA),SDES.ByteArrayToString(Encrypt(rawkey1a,rawkey2a,plaintextA)));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1b),SDES.ByteArrayToString(rawkey2b),SDES.ByteArrayToString(plaintextB),SDES.ByteArrayToString(Encrypt(rawkey1b,rawkey2b,plaintextB)));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1c),SDES.ByteArrayToString(rawkey2c),SDES.ByteArrayToString(plaintextC),SDES.ByteArrayToString(Encrypt(rawkey1c,rawkey2c,plaintextC)));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1d),SDES.ByteArrayToString(rawkey2d),SDES.ByteArrayToString(plaintextD),SDES.ByteArrayToString(Encrypt(rawkey1d,rawkey2d,plaintextD)));
		System.out.println();
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1e),SDES.ByteArrayToString(rawkey2e),SDES.ByteArrayToString(Decrypt(rawkey1e,rawkey2e,cipher1)),SDES.ByteArrayToString(cipher1));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1f),SDES.ByteArrayToString(rawkey2f),SDES.ByteArrayToString(Decrypt(rawkey1f,rawkey2f,cipher2)),SDES.ByteArrayToString(cipher2));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1g),SDES.ByteArrayToString(rawkey2g),SDES.ByteArrayToString(Decrypt(rawkey1g,rawkey2g,cipher3)),SDES.ByteArrayToString(cipher3));
		System.out.printf ("%-14s %-14s %-14s %-14s\n",SDES.ByteArrayToString(rawkey1h),SDES.ByteArrayToString(rawkey2h),SDES.ByteArrayToString(Decrypt(rawkey1h,rawkey2h,cipher4)),SDES.ByteArrayToString(cipher4));
	}
}




