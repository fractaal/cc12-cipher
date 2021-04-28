/**
 * Interface for the Controller class. By creating an interface for the Controller class,
 * we are able to create multiple kinds of controllers for different contexts, if need be.
 */
public interface IController {
  public String[] getAvailableCipherAlgorithms();
  public void setCipherAlgorithm(String cipherAlgorithmName);
  public String getCipherAlgorithm();

  public String[] getCipherInputs();
  public void selectCipherInput(String option);
  public String getSelectedCipherInput();
  public void setSelectedCipherInput(String value);

  public String encrypt(String plaintext);
  public String decrypt(String ciphertext);
}
