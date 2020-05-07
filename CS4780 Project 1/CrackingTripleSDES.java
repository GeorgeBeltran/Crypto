
import java.util.ArrayList;
import java.util.Scanner;


public class CrackingTripleSDES {
	 
	 public static void problem3(String file) {
	        
	        System.out.println( problemNum(3) );
	        
	        Show result = askPrompt(1024*1024);
	        
	        int answerPosition = 922979;  // This value is output for the decrypted message.
	                                       
	        byte[] ciphertext;
	        
	        do {	          
	                ciphertext = psuedoParse(file);
	                    file = psuedoFileMsg2;
	        } 
	        while ( ciphertext == null );
	        
	        byte[][] key1s = keyPermutations();
	        byte[][] key2s = keyPermutations();
	        
	        ArrayList<String> deciph = new ArrayList<>();
	        int attempt = 0;
	        
	        System.out.println("  Please wait. \n");
	        
	        for ( byte[] key1 : key1s ) {
	            for ( byte[] key2 : key2s ) {
	                deciph.add( triplesdesDecryptWithKeys(key1, key2, ciphertext, attempt) );
	                attempt++;                
	            }            
	        }
	        
	        performAction(deciph, answerPosition, result);        
	    }
	 
	 private static String triplesdesDecryptWithKeys(byte[] key1, byte[] key2, byte[] ciphertext, int num) {
	        
	        byte[] plainSect;
	        byte[] section = new byte[8]; 
	        byte[] plaintext = new byte[ciphertext.length];
	        
	        for ( int i = 0; i < plaintext.length; i+=0 ) {            
	            for (int x = 0; x < 8; x++, i++) {
	                section[x] = ciphertext[i];
	            }
	            plainSect = TripleSDES.Decrypt(key1, key2, section);
	            for (int x = 0, y = i-8; x < 8; x++, y++) {
	                plaintext[y] = plainSect[x];
	            }
	        }
	        
	        String str = keysAndByteArrStr(key1, key2, plaintext);
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
	    
	    private static String keysAndByteArrStr(byte[] key1, byte[] key2, byte[] cascii) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("key 1 = { ");
	        for ( int i = 0; i < key1.length; i++ ) {
	            if ( i != key1.length - 1 )
	                sb.append(key1[i]).append(",");
	            else
	                sb.append(key1[i]).append(" } ");
	        }
	        sb.append("key 2 = { ");
	        for ( int i = 0; i < key2.length; i++ ) {
	            if ( i != key2.length - 1 )
	                sb.append(key2[i]).append(",");
	            else
	                sb.append(key2[i]).append(" } ");
	        }
	        sb.append("-> ").append(CASCII.toString(cascii));
	        
	        return sb.toString();
	    }
	    
	    
	    private static Show askPrompt(int possible) {
	        String prompt = "What would you like to display for this problem.\n"	                     
	                      + "    all  - Finds all possible values for that CASCII String and prints all of them out. \n"
	                      + "    section - Finds all possibilities for that CASCII String\n"
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
	        // This if statement is assuming that there is only one line, and
	        // that all the bytes are written in that single line.
	        
	        bytes = new byte[file.length()];
	        for ( int i = 0; i < file.length(); i++) {
	            sub = file.substring(i, i+1);
	            b = Byte.parseByte(sub);
	            bytes[i] = b;
	        }
	        return bytes;
	    }
	    
	    public static void run() {
	         
	        String  msg2;      
	            msg2 = psuedoFileMsg2; 
	            problem3(msg2);        
	    }
	    
	    private static enum Show { ALL, SECT, ANS, NONE };
	    
	    private static final String psuedoFileMsg2 = "0001111110011111111001111110110011100000001100101111001010101011000101110100110100000011001101011111111000000000101011111100000101001011"
	            + "100111100101010110000011011110001111110101110010010001010100001100110010100000010111101100001001101011110001000100100010000111110010000000100000000110110100000000101"
	            + "0111010000001000010011100101111001101111011001001010001100010100000";
	}   

