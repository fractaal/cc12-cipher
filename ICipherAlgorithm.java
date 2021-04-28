/**
 * This is an interface that all enciphering algorithms must fulfill to ensure compatibility.
 * It also allows the Java compiler to perform checks on whether or not a class interacts
 * properly with the Controller.
 */
public interface ICipherAlgorithm {
  public String getName();
  public String encrypt(String plaintext, String... inputs);
  public String decrypt(String ciphertext, String... inputs);
  public String[] getCipherInputs();
}