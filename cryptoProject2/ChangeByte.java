
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.InputStream;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.io.OutputStream;
	import java.math.BigInteger;
	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	import java.util.Scanner;

	public class ChangeByte {

		 byte[] digestion, message1, message2;

		    public void byteChange(File SignedTxt) throws NoSuchAlgorithmException {
		    	Scanner sc = new Scanner(System.in);
		        try (InputStream input = new FileInputStream(SignedTxt);
		             ObjectInputStream objectInputStream = new ObjectInputStream(input);) {

		            int byteReading;
		            BigInteger num;
		            byte[] message = new byte[0];

		            num = (BigInteger) objectInputStream.readObject();
		            while ((byteReading = input.read()) != -1) {
		                byte[] messageByte = {(byte) byteReading};
		               
		                message = arrayAppending(message, messageByte);
		            }
		            String messageToString = new String(message);
		            message2 = messageToString.getBytes();
		            int i = 0;
		            do {
		            	System.out.println("[......Input a byte between 0 and " + message.length + " to change.]");
		                i = sc.nextInt();
		            }
		            while (i < 0 || i >= message.length);
		            message[i] = 0;

		            OutputStream outputStream = new FileOutputStream("test.txt.signed");
		            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		            objectOutputStream.writeObject(num);
		            outputStream.write(message);
		            outputStream.flush();
		            outputStream.close();
		            input.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		    }

		    public byte[] arrayAppending(byte[] firstArray, byte[] secondArray) {
		        byte[] newArray = new byte[firstArray.length + secondArray.length];
		        System.arraycopy(firstArray, 0, newArray, 0, firstArray.length);
		        System.arraycopy(secondArray, 0, newArray, firstArray.length, secondArray.length);
		        return newArray;
		    }

}