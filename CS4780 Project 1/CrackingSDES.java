
import java.util.ArrayList;
import java.util.Scanner;

public class CrackingSDES {
	    
	    public static void problem1() {
	      
	        System.out.println( problemNum(1) );
	        
	        byte[] key = {0,1,1,1,0,0,1,1,0,1};
	        String word = "CRYPTOGRAPHY";
	        byte[] plaintext = CASCII.Convert(word);
	        
	        byte[] ciphertext = new byte[plaintext.length];
	        byte[] section = new byte[8];
	        byte[] cipherSection;
	        
	        System.out.println("\nThe plaintext in CASCII.");
	        System.out.println(word + "\n");
	        
	        System.out.print("The byte array of the plain text.");
	        printByteArr(plaintext);
	        
	        for ( int i = 0; i < plaintext.length; i+=0 ) {            
	            for (int x = 0; x <= 7; x++, i++) {
	                section[x] = plaintext[i];
	            }
	            cipherSection = SDES.Encrypt(key, section);
	            for (int x = 0, y = i-8; x <= 7; x++, y++) {
	                ciphertext[y] = cipherSection[x];
	            }
	        }
	        
	        System.out.print("The byte array of the cipher text.");
	        printByteArr(ciphertext);
	        
	        System.out.println("The cipher text in CASCII.");
	        System.out.println(CASCII.toString(ciphertext) + "\n");
	        
	    }
	    
	    public static void problem2(String file) {
	        
	        System.out.println( problemNum(2) );
	        
	        Show result = askPrompt(1024);      
	        
	        int answerPosition = 756;  // This value is the output for the decrypted message.
	                                   
	        byte[] ciphertext;
	        
	        do {
	            
	                ciphertext = psuedoParse(file);
	                    file = psuedoFileMsg1;
	            
	            
	        } while ( ciphertext == null );
	        
	        System.out.println(" Please wait.\n");
	        
	        byte[][] keys = keyPermutations();
	        
	        ArrayList<String> deciph = new ArrayList<>();
	        int attempt = 0;
	        
	        for ( byte[] key : keys ) {
	            deciph.add( sdesDecryptWithKey(key, ciphertext, attempt) );
	            attempt++;
	        }
	        
	        performAction(deciph, answerPosition, result);
	    }
	 
	    private static String sdesDecryptWithKey(byte[] key, byte[] ciphertext, int num) {
	        
	        byte[] plainSect;
	        byte[] section = new byte[8]; 
	        byte[] plaintext = new byte[ciphertext.length];
	        
	        for ( int i = 0; i < plaintext.length; i+=0 ) {            
	            for (int x = 0; x <= 7; x++, i++) {
	                section[x] = ciphertext[i];
	            }
	            plainSect = SDES.Decrypt(key, section);
	            for (int x = 0, y = i-8; x <= 7; x++, y++) {
	                plaintext[y] = plainSect[x];
	            }
	        }
	        
	        String str = keyAndByteArrStr(key, plaintext);
	        return num + ". " + str;
	    }
	    private static byte[][] processPermutes(Object[] arr) {
	        byte[][] perms = new byte[arr.length][10];
	        String str, sub;
	        byte b;
	        for ( int i = 0; i < arr.length; i++ ) {
	            str = (String) arr[i];
	            for ( int j = 0; j < str.length(); j++ ) {
	                sub = str.substring(j, j+1);
	                b = Byte.parseByte(sub);
	                perms[i][j] = b;
	            }
	        }
	        return perms;
	    }
	        
	    private static byte[][] keyPermutations() {
	        ArrayList<String> strs = new ArrayList<>();
	        permutation(10, "", strs);
	        return processPermutes(strs.toArray());
	    }

	    private static void permutation(int num, String str, ArrayList<String> list) {
	        if (num == 0) {
	            list.add(str);
	            return;
	        }
	        for (int i = 0; i <= 1; i++) {
	            permutation(num - 1, str + Integer.toString(i), list);
	        }
	    }
	    
	    private static String keyAndByteArrStr(byte[] key, byte[] cascii) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("key = { ");
	        for ( int i = 0; i < key.length; i++ ) {
	            if ( i != key.length - 1 )
	                sb.append(key[i]).append(",");
	            else
	                sb.append(key[i]).append(" } ");
	        }
	        sb.append("-> ").append(CASCII.toString(cascii));
	        
	        return sb.toString();
	    }
	    
	    private static void printByteArr(byte[] arr) {
	        printArrByRows(arr, 32);
	    }
	    
	    private static void printArrByRows(byte[] arr, int rows) {
	        System.out.println();
	        int len = arr.length;
	        for ( int i = 0; i < len; i++ ) {
	            if ( i % rows == 0)
	                System.out.println();
	            else if (i != 0 && i % 4 == 0 ) 
	                System.out.print(" ");
	            System.out.print(arr[i]);
	        }
	        System.out.println("\n");
	    }
	    
	    private static Show askPrompt(int possible) {
	        String prompt = "What would you like to display for this problem.\n"	                     
	                      + "    all  - Finds all possible values for that CASCII String and prints all of them out.\n"
	                      + "    section - Finds all possibilities for that CASCII String, but only prints a section\n"
	                      + "    answer  - Finds all possilities (same as above), but only prints out the answer instead\n";
	        return sayAndAskPrompt(prompt);
	    }
	    
	    private static Show sayAndAskPrompt(String prompt) {
	        
	        Scanner reader = new Scanner(System.in);
	        String input = null;
	        System.out.print("\n" + prompt);
	        
	        do {
	            input = reader.nextLine();
	            input = input.trim().toLowerCase();
	             
	            switch(input) {
	                case "all":
	                    return Show.ALL;
	                case "section":
	                    return Show.SECT;
	                case "answer":
	                    return Show.ANS;
	                default:
	                    System.out.print(" Not a valid Input\n");
	                    break;
	            }
	                
	        } while (true);
	        
	    }
	    
	    private static void performAction(ArrayList<String> list, int ansPos, Show chosen) {
	        
	        switch (chosen) {
	            
	            case ALL:
	                System.out.println("\nThese are ALL the CASCII decoded strings.\n");
	                for ( int i = 0; i < list.size(); i++ ) {
	                    if ( i != ansPos )
	                        System.out.println("   "+list.get(i));
	                    else
	                        System.out.println("\n-> "+list.get(i)+"\n");
	                }
	                System.out.println("\nThis is what was found: \n\n" + list.get(ansPos) + "\n");
	                break;
	                
	            case SECT:
	                System.out.println("\nThis is the SECTION where the answer was located:\n");                
	                for (int i = ansPos-5; i < ansPos+5; i++) {
	                    if ( i != ansPos )
	                        System.out.println("   "+list.get(i));
	                    else
	                        System.out.println("\n-> "+list.get(i)+"\n");
	                }
	                System.out.println("\nThis is what was found: \n\n" + list.get(ansPos) + "\n");
	                break;
	                
	            case ANS:
	                System.out.println("\nThis is the ANSWER that was found: \n\n" + list.get(ansPos) + "\n");
	                break;
			default:
				break;
	                
	        }
	        
	    }
	    
	    private static String problemNum(int questionNum) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("\n");
	        sb.append("Question # 3." + questionNum + "\n");
	        return sb.toString();
	    }    
	        	    
	    private static byte[] psuedoParse(String file) {
	        byte b;
	        String sub;
	        byte[] bytes;
	        
	        // all the bytes are written in a single line.
	        
	        bytes = new byte[file.length()];
	        for ( int i = 0; i < file.length(); i++) {
	            sub = file.substring(i, i+1);
	            b = Byte.parseByte(sub);
	            bytes[i] = b;
	        }
	        return bytes;
	    }
	    
		 public static void run() {
	         
		        String msg1;
		        
		            msg1 = psuedoFileMsg1; 
		        
		        
		        problem1();
		        problem2 (msg1);
		    }

	    private static enum Show { ALL, SECT, ANS, NONE };
	    
	    private static final String psuedoFileMsg1 = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111"
	            + "100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011"
	            + "101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010"
	            + "010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001"
	            + "110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111"
	            + "000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
}
