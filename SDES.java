

public class SDES {
	
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		
		//the keys that we got in generate keys
		byte[][] keys = generateKeys(rawkey);
		
		//This is the IP. It is given in the book but like in generate key (permuted key) subtract by one on each number.
		byte[] ciphertext = { plaintext[1], plaintext[5], plaintext[2], plaintext[0], plaintext[3], plaintext[7], plaintext[4], plaintext[6] };
		
		ciphertext = fk(ciphertext, keys[0]);
		
		ciphertext = switchFunction(ciphertext);
		
		ciphertext = fk(ciphertext, keys[1]);
		
		byte[] result = { ciphertext[3], ciphertext[0], ciphertext[2], ciphertext[4], ciphertext[6], ciphertext[1], ciphertext[7], ciphertext[5] };
		return result;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		
		//the keys that we got in generate keys
		byte[][] keys = generateKeys(rawkey);
		
		//
		byte[] plaintext = { ciphertext[1], ciphertext[5], ciphertext[2], ciphertext[0], ciphertext[3], ciphertext[7], ciphertext[4], ciphertext[6] };

		plaintext = fk(plaintext, keys[1]);
		
		plaintext = switchFunction(plaintext);
		
		plaintext = fk(plaintext, keys[0]);
		byte[] result = { plaintext[3], plaintext[0], plaintext[2], plaintext[4], plaintext[6], plaintext[1], plaintext[7], plaintext[5] };
		return result;
	}
	
	private static byte[][] generateKeys(byte[] rawKey){
		// 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 before
		// 2, 4, 1, 6, 3, 9, 0, 8, 7, 5 after permute.. given in book 
		byte[] permutedKey = { rawKey[2], rawKey[4], rawKey[1], rawKey[6], rawKey[3], rawKey[9], rawKey[0], rawKey[8], rawKey[7], rawKey[5]};
		byte[][] keys = new byte[2][8];

		permutedKey = shiftLeft(permutedKey, 0, 5);
		permutedKey = shiftLeft(permutedKey, 5, 10);	
		
		//move P10 to get P8
		byte[] key1 = { permutedKey[5], permutedKey[2], permutedKey[6], permutedKey[3], permutedKey[7], permutedKey[4], permutedKey[9], permutedKey[8] };
		keys[0] = key1;
		
		//Spliting in half and shift twice each time its splited.
		permutedKey = shiftLeft(permutedKey, 0, 5); permutedKey = shiftLeft(permutedKey, 0, 5);
		permutedKey = shiftLeft(permutedKey, 5, 10); permutedKey = shiftLeft(permutedKey, 5, 10);
		
		//get P8 gets applied.
		byte[] key2 = { permutedKey[5], permutedKey[2], permutedKey[6], permutedKey[3], permutedKey[7], permutedKey[4], permutedKey[9], permutedKey[8] };
		keys[1] = key2;
		
		return keys;
	}
	//byte array was byte key.
	private static byte[] shiftLeft(byte[] array, int start, int end) { // End is exclusive
		byte[] newArray = new byte[array.length];
		for(int i = 0; i < start; i++){
			newArray[i] = array[i];
		}
		
		// Start Shift
		newArray[end - 1] = array[start];
		
		for(int i = start; i < end - 1; i++){
			newArray[i] = array[i+1];
		}
		
		// End Shift
		
		for(int i = end; i < array.length; i++){
			newArray[i] = array[i];
		}
		
		return newArray;
	}
	
	//This is to get the IP^-1. 
	public static byte[] fk(byte[] plaintext, byte[] key){
		byte[] output = new byte[8]; //rightmost 4 bits.
		byte[] right = new byte[4]; //leftmost 4 bits.
		
		for(int i = 4; i < 8; i++){
			output[i] = plaintext[i]; //moving through the right
			right[i-4] = plaintext[i]; //moving through the left
		}
		
		byte[] toXOR = SK(right, key);
		
		for(int i = 0; i < 4; i++){
			// Set to XOR
			output[i] = (byte) (plaintext[i] == toXOR[i] ? 0 : 1);
		}	

		
		return output;
	}
	
	private static byte[] SK(byte[] right, byte[] key){
		// Expansion and Permutation or E/P but subtracted by 1 from the book
		byte[] expandRight = {right[3], right[0], right[1], right[2], right[1], right[2], right[3], right[0] };
		
		//From book S0 = [1,0,3,2],[3,2,1,0],[0,2,1,3],[,3,1,3,2]
		byte[] s0Diagram = {1,0,3,2,3,2,1,0,0,2,1,3,3,1,3,2};
		
		//From book S1 = [0,1,2,3], [2,0,1,3], [3,0,1,0], [2,1,0,3]
		byte[] s1Diagram = {0,1,2,3,2,0,1,3,3,0,1,0,2,1,0,3};
		
		//getting the binary. If expand[x], set key[x] to 0, else set key[x] to 1.
		expandRight[0] = (byte) (key[0] == expandRight[0] ? 0 : 1);
		expandRight[1] = (byte) (key[1] == expandRight[1] ? 0 : 1);
		expandRight[2] = (byte) (key[2] == expandRight[2] ? 0 : 1);
		expandRight[3] = (byte) (key[3] == expandRight[3] ? 0 : 1);
		expandRight[4] = (byte) (key[4] == expandRight[4] ? 0 : 1);
		expandRight[5] = (byte) (key[5] == expandRight[5] ? 0 : 1);
		expandRight[6] = (byte) (key[6] == expandRight[6] ? 0 : 1);
		expandRight[7] = (byte) (key[7] == expandRight[7] ? 0 : 1);
		
		byte[] customBits = findSbox(0, expandRight, s0Diagram);
		byte[] mandatoryBits = findSbox(1, expandRight, s1Diagram);
		
		byte[] output = {customBits[1], mandatoryBits[1], mandatoryBits[0], customBits[0] };
		

		
		return output;
	}
	
	
	private static byte[] findSbox(int row, byte[] pbox, byte[] diagram){
		int rowIndex;
		int columnIndex;
		if(row == 0){
			
			//S0
			//The S-boxes operate as follows. 
			//The first and fourth input bits are treated as a 2-bit number that specify a row of the S-box
			rowIndex = twoDigitBinaryToDecimal(pbox[0], pbox[3]);
			
			//The second and third input bits specify a column of the Sbox. 
			columnIndex = twoDigitBinaryToDecimal(pbox[1], pbox[2]);
		} else {
			
			//S1
			rowIndex = twoDigitBinaryToDecimal(pbox[4], pbox[7]);
			columnIndex = twoDigitBinaryToDecimal(pbox[5], pbox[6]);
		}
		
		//
		//
		return decimalToTwoDigitBinary(diagram[rowIndex * 4 + columnIndex]);
	}
	
	private static byte[] decimalToTwoDigitBinary(byte decimal){
		byte[] binary = new byte[2];
		//this makes the (p0,1 p0,2) = (10)
		binary[0] = (byte) (decimal >= 2 ? 1 : 0);
		binary[1] = (byte) (decimal % 2 == 1 ? 1 : 0);
		return binary;
	}
	
	//this is to get the the p0,0 p1,1 
	private static int twoDigitBinaryToDecimal(byte b1, byte b2){
		return b1 * 2 + b2;
	}
	
	//Switching 0123 4567 become 4567 0123
	private static byte[] switchFunction(byte[] text){
		byte[] switched = {text[4], text[5], text[6], text[7], text[0], text[1], text[2], text[3]};
		return switched;
	}
	
	
	protected static String ByteArrayToString(byte[] key) {
		String arr = "";
		for(int i = 0; i < key.length; i++){
			if(i == key.length/2){
				arr += " ";
			}
			arr += key[i];
		}
		return arr;
	}
	
	public static void run() {
		byte[] rawkey1 = {0,0,0,0,0,0,0,0,0,0};
		byte[] plaintext1 = {1, 0, 1, 0, 1, 0, 1, 0};
		
		byte[] rawkey2 = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
		byte[] plaintext2 = {1, 0, 1, 0, 1, 0, 1, 0};
		
		byte[] rawkey3  = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
		byte[] plaintext3  = {0, 1, 0, 1, 0, 1, 0, 1};

		byte[] rawkey4  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		byte[] plaintext4  = {1, 0, 1, 0, 1, 0, 1, 0};

		byte[] rawkey5  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] plaintext5  = {0, 0, 0, 0, 0, 0, 0, 0};

		byte[] rawkey6  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		byte[] plaintext6  = {1, 1, 1, 1, 1, 1, 1, 1};

		byte[] rawkey7  = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
		byte[] plaintext7  = {0, 0, 0, 0, 0, 0, 0, 0};

		byte[] rawkey8  = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
		byte[] plaintext8  = {1, 1, 1, 1, 1, 1, 1, 1};
		
		byte[] rawkey9 = {1, 0, 0, 0, 1, 0, 1, 1, 1, 0};
		byte[] ciphertext9 = {0, 0, 0, 1, 1, 1, 0, 0};
		
		byte[] rawkey10 = {1, 0, 0, 0, 1, 0, 1, 1, 1, 0};
		byte[] ciphertext10 = {1, 1, 0, 0, 0, 0, 1, 0};

		byte[] rawkey11 = {0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
		byte[] ciphertext11 = {1, 0, 0, 1, 1, 1, 0, 1};

		byte[] rawkey12 = {0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
		byte[] ciphertext12 = {1, 0, 0, 1, 0, 0, 0, 0};
		
		System.out.println("Raw Key        Plaintext      Ciphertext      DecipheredText");
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey1), ByteArrayToString(plaintext1), ByteArrayToString(Encrypt(rawkey1, plaintext1)), ByteArrayToString(Decrypt(rawkey1, Encrypt(rawkey1, plaintext1))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey2), ByteArrayToString(plaintext2), ByteArrayToString(Encrypt(rawkey2, plaintext2)), ByteArrayToString(Decrypt(rawkey2, Encrypt(rawkey2, plaintext2))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey3), ByteArrayToString(plaintext3), ByteArrayToString(Encrypt(rawkey3, plaintext3)), ByteArrayToString(Decrypt(rawkey3, Encrypt(rawkey3, plaintext3))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey4), ByteArrayToString(plaintext4), ByteArrayToString(Encrypt(rawkey4, plaintext4)), ByteArrayToString(Decrypt(rawkey4, Encrypt(rawkey4, plaintext4))));
		System.out.println();
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey5), ByteArrayToString(plaintext5), ByteArrayToString(Encrypt(rawkey5, plaintext5)), ByteArrayToString(Decrypt(rawkey5, Encrypt(rawkey5, plaintext5))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey6), ByteArrayToString(plaintext6), ByteArrayToString(Encrypt(rawkey6, plaintext6)), ByteArrayToString(Decrypt(rawkey6, Encrypt(rawkey6, plaintext6))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey7), ByteArrayToString(plaintext7), ByteArrayToString(Encrypt(rawkey7, plaintext7)), ByteArrayToString(Decrypt(rawkey7, Encrypt(rawkey7, plaintext7))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", ByteArrayToString(rawkey8), ByteArrayToString(plaintext8), ByteArrayToString(Encrypt(rawkey8, plaintext8)), ByteArrayToString(Decrypt(rawkey8, Encrypt(rawkey8, plaintext8))));
		System.out.println();
		System.out.printf ("%-14s %-14s %-14s\n", ByteArrayToString(rawkey9), ByteArrayToString(Decrypt(rawkey9, ciphertext9)), ByteArrayToString(ciphertext9));
		System.out.printf ("%-14s %-14s %-14s\n", ByteArrayToString(rawkey10), ByteArrayToString(Decrypt(rawkey10, ciphertext10)), ByteArrayToString(ciphertext10));
		System.out.printf ("%-14s %-14s %-14s\n", ByteArrayToString(rawkey11), ByteArrayToString(Decrypt(rawkey11, ciphertext11)), ByteArrayToString(ciphertext11));
		System.out.printf ("%-14s %-14s %-14s\n", ByteArrayToString(rawkey12), ByteArrayToString(Decrypt(rawkey12, ciphertext12)), ByteArrayToString(ciphertext12));

	}
	

}