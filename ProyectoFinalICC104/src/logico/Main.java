package logico;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub--
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String original = new String("Admin");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}

		System.out.println("original:" + original);
		System.out.println("digested(hex):" + sb.toString());

	}

}
///