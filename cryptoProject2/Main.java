
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


import java.io.File;

public class Main {			
	
	static Scanner sc = new Scanner(System.in);
	static String path = "";
	public static void main(String[] args) {
				
		printMenu();

	}

	private static void printMenu() {
		System.out.println("Enter a number from 1 - 5 as an option: ");
		System.out.println("[1.....Generate Random Integer Keys]");
		System.out.println("[2.....Digitally Sign a Message] ");
		System.out.println("[3.....Verify A Message] " );
		System.out.println("[4.....Corrupt a Signed File (must be .TXT.SIGNED) ]" );
		System.out.println("[5.....Exit]");
		
		String input = sc.next();
		
		int option = Integer.parseInt(input);	
		
		switch (option) { 
        case 1: 
            keyGen(); 
            break; 
        case 2: 
        	messageSigner(); 
            break; 
        case 3: 
        	messageVerifier(); 
            break; 
        case 4: 
        	signatureFileCurruptor(); 
            break; 
        case 5: 
        	System.exit(0);
            break; 
		}

	}
		
		private static void signatureFileCurruptor() {
			System.out.println("Select the (.TXT.SIGNED) file you would like to have tampered (corrupted) with: ");
			path = sc.next();
			ChangeByte byteChanger = new ChangeByte();
			try {
				System.out.println("Corrupting " + path + "...");
				byteChanger.byteChange(new File(path));
				System.out.println(path +" File has been corrupted / tampered with!");
				System.out.println();
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			}
			
			System.out.println();
			printMenu();
		}

		private static void messageVerifier() {
			System.out.println("Select the (.TXT.SIGNED) file you would like to have decrypted (VERIFIED): ");
			path = sc.next();
			
			DigitalSignature digisig = new DigitalSignature();
			try{
				System.out.println("Reading " + path + "...");
				digisig.receiver(new File(path));
				System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			System.out.println();
			printMenu();
			
		}
		
		private static void messageSigner() {
			System.out.println("Select the (.TXT) file you would like to have encrypted (digitally signed): ");
			path = sc.next();
			
			DigitalSignature digisig = new DigitalSignature();
			try{
				System.out.println("Signing " + path + "...");
				digisig.sender(new File(path));
				System.out.println(path + " Has been signed");
				System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
				
			System.out.println();
			printMenu();
			
		}
		
		private static void keyGen() {
		
			KeyGen key = new KeyGen();
			System.out.println("Value \"e\" = " + key.e);
			System.out.println("Value \"d\" = " + key.d);
			System.out.println("Value \"n\" = " + key.n);
			
			key.writeToFile();
			
			System.out.println();
			printMenu();
			
		}
	
	
}
