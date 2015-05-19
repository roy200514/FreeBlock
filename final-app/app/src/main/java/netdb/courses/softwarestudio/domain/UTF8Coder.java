package netdb.courses.softwarestudio.domain;

import java.nio.charset.StandardCharsets;

public class UTF8Coder {
	
	public static String encode(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.ISO_8859_1);
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	public static String decode(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
		return new String(bytes, StandardCharsets.ISO_8859_1);
	}
}
