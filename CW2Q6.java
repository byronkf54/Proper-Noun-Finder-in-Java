import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CW2Q6 {

	// set of words to remove
	ArrayList<String> words = new ArrayList<>();
	// set of all months to remove
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	int new_line = 0;
	int is_fullstop = 0;


	// ALTERNATIVE to .size
	int arrayList_length(ArrayList arr) {
		int len = 0;
		for (Object string : arr) {
			len++;
		}
		return len;
	}


	// ALTERNATIVE to length
	int array_length(String[] arr) {
		int len = 0;
		for (String string : arr) {
			len++;
		}
		return len;
	}


	// ALTERNATIVE to length()
	int string_length(String str) {
		char[] charArray = str.toCharArray();
		int length = 0;
		for (char c : charArray) {
			length++;
		}
		return length;
	}


	// function checks if character is a capital
	boolean isUpperCase(int c) {
		return 65 <= c && c <= 90;
	}


	// function checks the string against list of words to redact if it is in the list the word is replaced with asterisks
	int check_list(int i, String text) {
		int x = 0;
		for (int n = 0; n < arrayList_length(words); n++) {
			char c = text.charAt(i);
			while (c == words.get(n).charAt(x) && x < words.get(n).length() - 1) {
				x++;
				try {
					c = text.charAt(i + x);
					// if statement checks if end of word in list has been reached
					if (x == string_length(words.get(n)) - 1 && !(96 < text.charAt(i + x + 1) && text.charAt(i + x + 1) < 123)) {
						// Makes sure the words CHAPTER and BOOK are not redacted
						if (words.get(n).equals("CHAPTER") || words.get(n).equals("BOOK")) {
							for (int m = 0; m < string_length(text); m++) {
								is_fullstop = 0;
								c = text.charAt(m);
								System.out.print(c);
							}
							x = string_length(text);
						} else {
							for (int l = 0; l < x + 1; l++) {
								System.out.print("*");
							}
						}
						return i + x;
					}

				} catch (IndexOutOfBoundsException e) {
					break;
				}
			}
			x = 0;
		}

		return i;
	}


	// function prints the characters of the string out instead of asterisks
	int print_chars(int i, String text) {
		char c = text.charAt(i);
		try {
			while (text.charAt(i + 1) > 32 || text.charAt(i + 1) != ' ') {
				System.out.print(c);
				i++;
				c = text.charAt(i);
			}
			System.out.print(c);
		} catch (IndexOutOfBoundsException e) {
			System.out.print(text.charAt(i));
		}
		return i;
	}


	// function prints asterisks out for pronouns not in the redact list
	int print_stars(int i, String text) {
		// to add new words found as proper nouns in the text
		System.out.print("*");
		try {
			while (96 < text.charAt(i + 1) && text.charAt(i + 1) < 123) {
				System.out.print("*");
				char c = text.charAt(i);
				i++;
			}
			char c = text.charAt(i);
		} catch (IndexOutOfBoundsException e) {
			if (i != string_length(text) - 1) {
				System.out.print(text.charAt(i));
			}
		}
		return i;
	}


	// function checks the string passed in against numerous cases to determine if word is a proper noun
	int check_cases(int i, String text) {
		try {
			// checking punctuation/symbols
			if (i == 0 && new_line == 0 && is_fullstop == 0) {
				i = print_stars(i, text);
				return i;
			} else if (text.charAt(i - 2) == ',' || text.charAt(i - 1) == '—') {
				i = print_stars(i, text);
				return i;
			} else if (text.charAt(i - 2) < 65 && text.charAt(i - 2) > 32 && text.charAt(i - 1) != '“' && text.charAt(i + 1) != '’' && text.charAt(i - 1) != '‘' && text.charAt(i + 1) != '”' && text.charAt(i - 2) != '’') {
				i = print_chars(i, text);
				return i;
			} else if ((text.charAt(i - 1) == '“' || text.charAt(i - 1) == '‘') && 96 > text.charAt(i)) {
				i = print_chars(i, text);
			} else if (96 < text.charAt(i + 1) && text.charAt(i + 1) < 123) {
				if (text.charAt(i - 2) == '’' || text.charAt(i - 2) == '”') {
					i = print_chars(i, text);
				} else {
					i = print_stars(i, text);
				}
				return i;
			} else if (isUpperCase(text.charAt(i - 1))) {
				i = print_chars(i, text);
				return i;
			}
		} catch (IndexOutOfBoundsException e) {
			try {
				i = print_chars(i, text);
			} catch (IndexOutOfBoundsException ex) {
				i = print_stars(i, text) - 1;
			}
		}
		return i;
	}


	// procedure determines which function should be called to check if word is a proper noun, e.g. check redact list or cases
	void scan_text(String text) {
		int len = text.length();
		char c;
		for (int i = 0; i < len; i++) {
			c = text.charAt(i);
			// line ends in full stop
			if (isUpperCase(c)) {
				// check if in array of words
				int m = check_list(i, text);
				// if return value is same as i then the word was not found
				if (m == i) {
					if (c == 'I') {
						try {
							System.out.print(c);
						} catch (IndexOutOfBoundsException e) {
							System.out.print("I");
						}
					} else {
						i = check_cases(i, text);
					}

				} else {
					i = m;
				}
			} else {
				System.out.print(c);
			}
		}
		if (text.charAt(string_length(text) - 1) == '.') {
			is_fullstop = 1;

		} else {
			is_fullstop = 0;
		}
	}


	// procedure reads in text from file using buffer reader
	void read_text() {
		try (BufferedReader br = new BufferedReader(new FileReader("warandpeace.txt"))) {
			char c;
			String text = "";
			while (br.ready()) {
				text = br.readLine();
				System.out.print("\n");
				if (!text.isEmpty()) {
					// analyses text line by line
					scan_text(text);
					new_line = 0;

				} else {
					new_line = 1;
				}
			}
		} catch (IOException e) {
			System.out.println("File does not exist.");
		}
	}


	// procedure reads in and stores list of words to redact
	void read_redact_words() {
		try (BufferedReader br = new BufferedReader(new FileReader("redact.txt"))) {
			while (br.ready()) {
				String str = br.readLine();
				String word = "";
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == ' ') {
						words.add(word);
						word = "";
					} else if (i == str.length() - 1) {
						word = word + str.charAt(i);
						words.add(word);
						word = "";
					} else {
						word = word + str.charAt(i);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("File does not exist.");
		}
		// add months to list of proper nouns
		for (int i = 0; i < array_length(months); i++) {
			words.add(months[i]);
		}
		words.add("Baron");
		words.add("Prince");
		words.add("Lavater");
		words.add("Prussia");
		words.add("Her Majesty");
		words.add("CHAPTER");
		words.add("BOOK");
		System.out.println(words);
	}


	public static void main(String[] args) {
		CW2Q6 wr = new CW2Q6();
		wr.read_redact_words();
		wr.read_text();
	}

}

