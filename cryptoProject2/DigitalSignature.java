import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class DigitalSignature {

	byte[] digestion, message1, message2;
	BigInteger digestBigInt;
	KeyGen generator;
	List<BigInteger> bigIntegerKey;

	public byte[] intToDig(BigInteger signature) {

		BigInteger bigInteger = signature.modPow(bigIntegerKey.get(0), bigIntegerKey.get(1));
		byte[] digestion = bigInteger.toByteArray();

		return digestion;
	}

	public byte[] getRidOfLeadingZeros(byte[] array) {
		int num = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0) {
				break;
			}
			num++;
		}
		byte[] newArray = new byte[array.length - num];
		for (int j = 0; j < (array.length - num); j++) {
			newArray[j] = array[j + num];
		}
		return newArray;
	}

	public BigInteger dig2Int(byte[] md) {
		BigInteger bigInteger = new BigInteger(1, md);

		bigInteger = bigInteger.modPow(bigIntegerKey.get(0), bigIntegerKey.get(1));
		return bigInteger;
	}

	public byte[] arrayAppending(byte[] arrayX, byte[] arrayY) {
		byte[] secondArray = new byte[arrayX.length + arrayY.length];
		System.arraycopy(arrayX, 0, secondArray, 0, arrayX.length);
		System.arraycopy(arrayY, 0, secondArray, arrayX.length, arrayY.length);
		return secondArray;
	}

	public void sender(File plaintext) throws NoSuchAlgorithmException {

		generator = new KeyGen();
		MessageDigest digestingMessage = MessageDigest.getInstance("MD5");

		try (FileInputStream input = new FileInputStream(plaintext);) {
			int byteReading;
			byte[] message = new byte[0];

			while ((byteReading = input.read()) != -1) {
				byte[] messageByte = { (byte) byteReading };
				message = arrayAppending(message, messageByte);
			}
			String messageToString = new String(message);
			message1 = messageToString.getBytes();

			digestingMessage.update(message1);
			digestion = digestingMessage.digest();

			bigIntegerKey = generator.readKeyFile("privkey.rsa");
			if (bigIntegerKey.isEmpty()) {
				System.out.println("Quiting");
			} else {
				digestBigInt = dig2Int(digestion);

				SignToFile(message, digestBigInt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void SignToFile(byte[] msg, BigInteger digInt) {
		try (OutputStream outputStream = new FileOutputStream("test.txt.signed");
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);) {
			
			objectOutputStream.writeObject(digInt);
			
			outputStream.write(msg);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiver(File signature) throws NoSuchAlgorithmException {

		generator = new KeyGen();
		MessageDigest messageDigestion = MessageDigest.getInstance("MD5");
		byte[] backwardsDigest;

		try (InputStream input = new FileInputStream(signature);
				ObjectInputStream objectInputStream = new ObjectInputStream(input);) {

			BigInteger num;

			byte[] message = new byte[0];
			int byteReader;

			num = (BigInteger) objectInputStream.readObject();
			while ((byteReader = input.read()) != -1) {
				byte[] msgByte = { (byte) byteReader };
				
				message = arrayAppending(message, msgByte);
			}
			String messageToString = new String(message);
			message2 = messageToString.getBytes();

			input.close();
			
			bigIntegerKey = generator.readKeyFile("pubkey.rsa");

			if (bigIntegerKey.isEmpty()) {
				System.out.println("Quiting");
			} else {
			
				backwardsDigest = intToDig(num);
				byte[] revisedRevDigest = getRidOfLeadingZeros(backwardsDigest);

				messageDigestion.update(message2);

				byte[] digestion = messageDigestion.digest();
				System.out.println("The message was: "
						+ (MessageDigest.isEqual(digestion, revisedRevDigest) ? "valid" : " invalid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
