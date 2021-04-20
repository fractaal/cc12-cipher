import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Transposition implements ICipherAlgorithm {

  static String alphabet = "abcdefghijklmnopqrstuvwxyz";
  static Random r = new Random();

  public String getName() {
    return "Columnar Transposition Cipher";
  }

  public String[] getCipherInputs() {
    return new String[] {"Key"};
  }

  public String encrypt(String plaintext, String... inputs) {
    plaintext = plaintext.toLowerCase();
    char[] charKey = inputs[0].toLowerCase().toCharArray();
    int[] intKey = new int[charKey.length];
    int order = 0;
    
    List<Character> trimmedPlaintext = new ArrayList<Character>();

    for (char letter = 'a'; letter <= 'z'; letter++) {
      for (int i = 0; i < charKey.length; i++) {
        if (charKey[i] == letter) {
          intKey[i] = ++order;
        }
      }  
    }
    
    for (int i = 0; i < plaintext.length(); i++) {
      if (plaintext.charAt(i) <= 'z' && plaintext.charAt(i) >= 'a') {
        System.out.println(plaintext.charAt(i) + " is a letter!");
        trimmedPlaintext.add(plaintext.charAt(i));
      }
    }  

    char[][] output = new char[(int) Math.ceil(trimmedPlaintext.size()/intKey.length) + 1][intKey.length];


    System.out.println(trimmedPlaintext.size() + " / " + intKey.length);
    System.out.println("x: " + output[0].length + ", y: " + output.length);

    int idx = 0;
    for (int x = 0; x < output.length; x++) {
      for (int y = 0; y < output[0].length; y++) {
        try {
          output[x][y] = trimmedPlaintext.get(idx++);
        } catch (Exception e) {
          output[x][y] = alphabet.charAt(r.nextInt(alphabet.length())); idx++;
        }
      }
    }

    String stringOutput = "";

    for (int i = 0; i < intKey.length; i++) {
      for (int j = 0; j < intKey.length; j++) {
        if ((i+1) == intKey[j]) {
          for (int k = 0; k < output.length; k++) {
            stringOutput += output[k][j];
          }
        }
      }
    }

    return stringOutput.toUpperCase();
  }
  
  public String decrypt(String ciphertext, String... inputs) {
    ciphertext = ciphertext.toLowerCase();
    char[] charKey = inputs[0].toLowerCase().toCharArray();
    int[] intKey = new int[charKey.length];
    int order = 0;
    

    for (char letter = 'a'; letter <= 'z'; letter++) {
      for (int i = 0; i < charKey.length; i++) {
        if (charKey[i] == letter) {
          intKey[i] = ++order;
        }
      }  
    }
    
    String[] columns = ciphertext.split(String.format("(?<=\\G.{%1$d})", ciphertext.length() / charKey.length));
    String[] sortedColumns = new String[columns.length];

    // System.out.println(Arrays.toString(columns));
    
    for (int i = 0; i < intKey.length; i++) {
      sortedColumns[i] = columns[intKey[i]-1];
    }
    
    // System.out.println(Arrays.deepToString(sortedColumns).replace("],", "],\n"));

    String output = "";
      for (int i = 0; i < sortedColumns[0].length(); i++) {
        for (int j = 0; j < sortedColumns.length; j++) {
          output += sortedColumns[j].charAt(i);
        }
      }
    return output.toUpperCase();
  }
}
