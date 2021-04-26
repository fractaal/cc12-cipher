import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Controller implements IController {

  private static String selectedCipherAlgorithm = "";
  private static String selectedCipherInput = "";

  private static String[][] cipherInputs = new String[][] {{}, {}};
  // private static Map<String, String> cipherInputs = new HashMap<String, String>();
  private static Map<String, ICipherAlgorithm> algorithms = new HashMap<String, ICipherAlgorithm>();

  Controller() {
    Caesar caesar = new Caesar();
    Transposition transposition = new Transposition();
    Vigenere vigenere = new Vigenere();

    algorithms.put(caesar.getName(), caesar);
    algorithms.put(transposition.getName(), transposition);
    algorithms.put(vigenere.getName(), vigenere);

  }

  public String[] getAvailableCipherAlgorithms() {
    String[] result = new String[algorithms.keySet().size()];
    algorithms.keySet().toArray(result);
    return result;
  }

  public void setCipherAlgorithm(String cipher) {
    selectedCipherAlgorithm = cipher;
    
    // Reset cipher inputs and repopulate them with inputs for
    // newly selected cipher algo.
    cipherInputs[0] = getCipherInputs();
    cipherInputs[1] = new String[getCipherInputs().length];
    selectCipherInput(getCipherInputs()[0]);
  }

  public String getCipherAlgorithm() {
    return selectedCipherAlgorithm;
  }

  public String[] getCipherInputs() {
    try {
      return algorithms.get(selectedCipherAlgorithm).getCipherInputs();
    } catch(Exception e) {
      System.out.println("Failed to get cipher inputs: " + e.getMessage());
      return new String[] {};
    }
  }

  public void selectCipherInput(String option) {
    selectedCipherInput = option;
  }

  public String getSelectedCipherInput() {
    return cipherInputs[1][indexOf(cipherInputs[0], selectedCipherInput)];
  }

  public void setSelectedCipherInput(String value) {
    cipherInputs[1][indexOf(cipherInputs[0], selectedCipherInput)] = value;
  }

  public int indexOf(String[] arr, String value) {
    System.out.println(Arrays.deepToString(arr) + " --- " + value);
    for (int i = 0; i < arr.length; i++) if (arr[i] == (value)) return i;
    return -1;
  }

  public String encrypt(String plaintext) {
    System.out.println("Starting encryption of plaintext " + plaintext + " with inputs " + Arrays.deepToString(cipherInputs));
    return algorithms.get(selectedCipherAlgorithm).encrypt(plaintext, cipherInputs[1]);
  };

  public String decrypt(String ciphertext) {
    System.out.println("Starting decryption of cipher" + ciphertext + " with inputs " + Arrays.deepToString(cipherInputs));
    return algorithms.get(selectedCipherAlgorithm).decrypt(ciphertext, cipherInputs[1]);
  }
}
