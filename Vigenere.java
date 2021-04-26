public class Vigenere implements ICipherAlgorithm {
  static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  @Override
  public String getName() {
    return "Vigenère Cipher";
  }

  @Override
  public String[] getCipherInputs() {
    return new String[] {"Key"};
  }

  @Override
  public String encrypt(String plaintext, String... inputs) {
    String key = inputs[0].toUpperCase();
    plaintext = plaintext.toUpperCase();

    String ciphertext = "";
    int keyIndex = 0;
    int plaintextIndex = 0;
    while (plaintextIndex < plaintext.length()) {
      if (keyIndex >= key.length()) keyIndex = 0;
      System.out.println(keyIndex);
      char temp = (char) (plaintext.charAt(plaintextIndex) + alphabet.indexOf(key.charAt(keyIndex)));
      if (temp > 'Z'){
        temp = (char) (temp + 'A' - 'Z' - 1);
      }
      ciphertext += temp;
      plaintextIndex++; keyIndex++;
    }

    return ciphertext;
  }

  @Override
  public String decrypt(String ciphertext, String... inputs) {
    String key = inputs[0].toUpperCase();
    ciphertext = ciphertext.toUpperCase();

    String plaintext = "";
    int keyIndex = 0;
    int ciphertextIndex = 0;
    while (ciphertextIndex < ciphertext.length()) {
      if (keyIndex >= key.length()) keyIndex = 0;
      System.out.println(ciphertextIndex);
      System.out.println(ciphertextIndex + "< " + ciphertext.length() + " = " + (ciphertextIndex < ciphertext.length()));
      char temp = (char) (ciphertext.charAt(ciphertextIndex) - alphabet.indexOf(key.charAt(keyIndex)));
      if (temp < 'A'){
        temp = (char) (temp - 'A' + 'Z' + 1);
      }
      plaintext += temp;
      ciphertextIndex++; keyIndex++;
    }

    return plaintext;
  }
}
