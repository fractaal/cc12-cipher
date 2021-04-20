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
