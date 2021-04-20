
public interface ICipherAlgorithm {
  public String encrypt(String plaintext, String... inputs);
  public String decrypt(String ciphertext, String... inputs);
  public String[] getCipherInputs();
}