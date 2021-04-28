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
    char[] charKey = inputs[0].toLowerCase().toCharArray(); // Convert the key to a character array.
    int[] intKey = new int[charKey.length]; // Create an int array that can hold the key array expressed as ints.
    List<Character> trimmedPlaintext = new ArrayList<Character>();

    int order = 0; 
    for (char letter = 'a'; letter <= 'z'; letter++) { // From a -> z...
      for (int i = 0; i < charKey.length; i++) { // For every letter in the key ...
        if (charKey[i] == letter) { // If key is var LETTER, then that's ORDERth letter in key.
          intKey[i] = ++order;
        }
      }  
    }
    
    // Trim plaintext, remove characters that cannot be enciphered.
    for (int i = 0; i < plaintext.length(); i++) { 
      if (plaintext.charAt(i) <= 'z' && plaintext.charAt(i) >= 'a') {
        trimmedPlaintext.add(plaintext.charAt(i));
      }
    }  

    /** Output is a 2 dimensional array because of the fact that a columnar transposition cipher
      * needs to be expressed as a grid. trimmedPlaintext is piped into the output 2d array
      * from left -> right, top -> down.
      */
    char[][] charGrid = new char[(int) Math.ceil(trimmedPlaintext.size()/intKey.length) + 1][intKey.length];

    int idx = 0;
    for (int x = 0; x < charGrid.length; x++) {
      for (int y = 0; y < charGrid[0].length; y++) {
        try {
          charGrid[x][y] = trimmedPlaintext.get(idx++);
        } catch (Exception e) {
          charGrid[x][y] = alphabet.charAt(r.nextInt(alphabet.length())); idx++;
        }
      }
    }

    String output = "";

    /**
     * Then, we need to iterate over the character grid in such a way that we go over the columns
     * in order of the key initially supplied.
     */
    for (int i = 0; i < intKey.length; i++) {
      for (int j = 0; j < intKey.length; j++) {
        if ((i+1) == intKey[j]) {
          for (int k = 0; k < charGrid.length; k++) {
            output += charGrid[k][j];
          }
        }
      }
    }

    return output.toUpperCase();
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
    
    // Divide the message into its original columns by splitting it every ciphertext length divided by the key length.
    String[] columns = ciphertext.split(String.format("(?<=\\G.{%1$d})", ciphertext.length() / charKey.length));

    // These columns are not sorted, so we need to create a new String array and sort the columns.
    String[] sortedColumns = new String[columns.length];
    for (int i = 0; i < intKey.length; i++) {
      sortedColumns[i] = columns[intKey[i]-1];
    }

    /**
     * Now that we have sorted the columns once more, we are able to reconstruct the key by going over the entire thing
     * from left  -> right, top -> down.
     */
    String output = "";
      for (int i = 0; i < sortedColumns[0].length(); i++) {
        for (int j = 0; j < sortedColumns.length; j++) {
          output += sortedColumns[j].charAt(i);
        }
      }
    return output.toUpperCase();
  }
}
