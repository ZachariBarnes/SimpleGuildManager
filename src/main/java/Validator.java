package main.java;

import java.util.ArrayList;

public class Validator {
	private static String INVALID_LENGTH = "Family Name must be between 3 and 16 characters.";
	private static String INVALID_CHARS = "Name Contains invlaid characters or too many underscores(limit:1)";
	private static String INVALID_LETTERS = "Family Name contains too many instances of a single letter(limit:3)";
	private static String TOO_MANY_NUMS = "Family Name ends with too many number(limit:4)";

	public ArrayList<String> validateFamilyName(String name) {
		ArrayList<String> errors = new ArrayList<String>();
		name = name.trim().toLowerCase();
		String validChars = "abcdefghijklmnopqrstuvwxyz_";
		String numbers = "0123456789";
		int count = 0;
		int numCount = 0;

		if (name.length() < 3 || name.length() > 16) {
			errors.add(INVALID_LENGTH);
		}

		for (int i = 0; i < name.length(); i++) {
			String letter = name.substring(i, i + 1);
			if ((!validChars.contains(letter) && !numbers.contains(letter))) {
				errors.add(INVALID_CHARS);
			}
			count = name.length() - name.replace("letter", "").length();
			if (count > 3 || (letter.equals("_") && count > 1)) {
				errors.add(INVALID_LETTERS);
			}
			if (i >= (name.length() - 4)) {
				if (numbers.contains(letter)) {
					numCount++;
				}
				if (numCount > 3) {
					errors.add(TOO_MANY_NUMS);
				}
			}
		}
		return errors;
	}
}
