// class containing the Algorithm for the Caesar Cipher
/** The Caesar Cipher is one of the simplest and most widely known encryption techniques.
* It is a type of substitution cipher, which each letter in plaintext
* is replaced by a letter some fixed number of positions down the alphabet.*/
 
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
    /**This code acts as a limiter such that
     * the value is converted to in modulus 26
     * if the integer is negative, 
     * it finds the absolute value of the integer before being converted.
     */
    index = (index < 0) ? (26 - Math.abs(index)%26) %26 : (index % 26);

    String ct = "";
    
    for (int i = 0; i < pt.length(); i++) {
      //traverses through the plaintext one character at a time
      char letters = pt.charAt(i);
      //if letters lie between 'a' and 'z'
      if (letters >= 'a' && letters <= 'z') {
        //alphabet is shifted with the corresponding index
          letters = (char) (letters + index);
          //if shift alphabet goes beyond 'z'
          if(letters > 'z'){
            //reshift to the starting position
            letters = (char) (letters + 'a' - 'z' - 1);
          }
          ct += letters;
      //if the letters lie between 'A' and 'Z'
      } else if (letters >= 'A' && letters <= 'Z') {
        //alphabet is shifted with the corresponding index
          letters = (char) (letters + index);
          //if shift alphabet goes beyond 'Z'
          if (letters > 'Z'){
            //reshift to the starting position
            letters = (char) (letters + 'A' - 'Z' - 1);
          }
          ct += letters;  
      } else {
        ct += letters;
      } //this is to ensure that any character aside from the letters will still be included in the ciphertext
    }
    return ct;
  }

  public String decrypt(String ciphertext, String... inputs) {
    String ct = ciphertext;
    Integer index = Integer.valueOf(inputs[0]);
    index = (index < 0) ? (26 - Math.abs(index)%26) %26 : (index % 26);
    String decrypt = "";

    for (int i = 0; i < ct.length(); i++) {
      //traverses through the ciphertext one character at a time.
      char letters = ct.charAt(i);
      //if letters lie between 'a' and 'z'
      if (letters >= 'a' && letters <= 'z') {
        //alphabet is shifted with the corresponding index
          letters = (char) (letters - index);
          //if shifted alphabet goes beyond 'a'
          if (letters < 'a') {
            //reshift to starting position
            letters = (char) (letters-'a'+'z'+1);
          }
          decrypt += letters;
      //if letters lie between 'A' and 'Z'    
      } else if (letters >= 'A' && letters <= 'Z') {
        //shift alphabet with corresponding index
          letters = (char) (letters - index);
          //if shift alphabet goes beyond 'A'
          if (letters < 'A') {
            //reshift
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
