// class containing the Algorithm for the Caesar Cipher
public class Caesar implements ICipherAlgorithm {
  static String name = "Caesar Cipher";
  static String[] cipherInputs = {"Letter Shift"}; 

  public String[] getCipherInputs() {
    return cipherInputs;
  }

  public String getName() {
    return name;
  }

  public String encrypt(String plaintext, String ...inputs) {
    String pt = plaintext;
    Integer index = Integer.valueOf(inputs[0]);
    index = (index < 0) ? (26 - Math.abs(index)%26) %26 : (index % 26);

    String ct = "";
    char letters;

    for (int i = 0; i < pt.length(); i++) {
      letters = pt.charAt(i);
      if (letters >= 'a' && letters <= 'z') {
          letters = (char) (letters + index);
          if(letters > 'z'){
            letters = (char) (letters + 'a' - 'z' - 1);
          }
          ct += letters;
      } else if (letters >= 'A' && letters <= 'Z') {
          letters = (char) (letters + index);
          if (letters > 'Z'){
            letters = (char) (letters + 'A' - 'Z' - 1);
          }
          ct += letters;
      } else {
        ct += letters;
      }
    }
    return ct;
  }

  public String decrypt(String ciphertext, String... inputs) {
    String ct = ciphertext;
    Integer index = Integer.valueOf(inputs[0]);
    index = (index < 0) ? (26 - Math.abs(index)%26) %26 : (index % 26);
    String decrypt = "";

    for (int i = 0; i < ct.length(); i++) {
      char letters = ct.charAt(i);
      if (letters >= 'a' && letters <= 'z') {
          letters = (char) (letters - index);
          if (letters < 'a') {
            letters = (char) (letters-'a'+'z'+1);
          }
          decrypt += letters;
      } else if (letters >= 'A' && letters <= 'Z') {
          letters = (char) (letters - index);
          if (letters < 'A') {
            letters = (char) (letters-'A'+'Z'+1);
          }
          decrypt += letters;
      } else { 
        decrypt += letters; 
      }
    }
    return decrypt;
  }
}
