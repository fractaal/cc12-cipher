/**
 * The App (main) class. Manages UI elements and dispatches actions to 
 * a controller class which handles the underlying business logic. 
 * This class only serves as a view layer and only provides information
 * and dispatches actions.
 */

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;

public class App {
  static Controller controller = new Controller();

  static String appName = "Syfer";

  // UI elements 
  static JFrame frame;
  static JPanel panel;

  static JPanel cipherSelectPanel;
  static JButton openFileButton;
  static JLabel cipherSelectLabel;
  static JComboBox<String> cipherSelect;

  static JPanel cipherOptionsPanel;
  static JLabel cipherOptionsLabel;
  static JComboBox<String> cipherOptionsSelect;
  static JTextField cipherOptionsValue;

  static JPanel textFieldPanel;
  static JLabel inputTextFieldLabel;
  static JTextArea inputTextField;
  static JScrollPane inputTextFieldScroll;
  static JLabel outputTextFieldLabel;
  static JTextArea outputTextField;
  static JScrollPane outputTextFieldScroll;

  static JPanel buttonsPanel;
  static JButton encryptButton;
  static JButton decryptButton;
  static JButton saveButton;

  public static void main(String[] args) throws Exception {
    // Attempt to set look and feel of UI to Windows style if present
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
      System.out.println("Look and feel not found");
    }
    // Initialize UI
    createUI();
  }

  // Method that calls the controller to set the selected cipher algorithm, as well as update any related UI elements.
  static void UIChangeCipherAlgorithm(String name) {
    controller.setCipherAlgorithm(name); // Call the controller 
    cipherOptionsSelect.removeAllItems();
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(controller.getCipherInputs());
    cipherOptionsSelect.setModel(model);
    cipherOptionsValue.setText(controller.getSelectedCipherInput());
  }

  // A little helper method we use to check whether file is binary/text to display warning msg if not text.
  static boolean isTextFile(File f) throws Exception {
        String type = Files.probeContentType(f.toPath());
        if (type == null) { // Type can't be determined. Assume not text file.
            return false;
        } else if (type.startsWith("text")) { // Type is text file.
            return true;
        } else { // Type is not a text file. 
            return false;
        }
    }
  
    // This method creates all the actual objects that fill in the class fields that were defined previously.
  static void createUI() throws Exception {
    // Window
    frame = new JFrame();
    panel = new JPanel();
    frame.setTitle(appName);
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    // Cipher algorithm selection
    cipherSelectPanel = new JPanel();
    cipherSelectPanel.setBorder(BorderFactory.createTitledBorder("Setup"));
    openFileButton = new JButton("Open File...");
    cipherSelectLabel = new JLabel("Cipher Algorithm");
    cipherSelect = new JComboBox<>(controller.getAvailableCipherAlgorithms());
    cipherSelect.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          UIChangeCipherAlgorithm((String) e.getItem());
        }
      }
    });

    // Handles opening of files.
    openFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();

          try {
            if (!isTextFile(selectedFile)) {
              JOptionPane.showMessageDialog(null, selectedFile.getName() + " is not a text file. It most likely won't display properly below, and some enciphering algorithms may fail to work.");
            }
          } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, appName + " can't determine if " + selectedFile.getName() + " is not a text file. It most likely won't display properly below, and some enciphering algorithms may fail to work.\nError: " + ex.getMessage());
          }

          // Goes over the entire file line by line and outputs it to the input text field.
          BufferedReader bufferedReader;
          try {
            FileReader reader = new FileReader(selectedFile);
            bufferedReader = new BufferedReader(reader);
            String all = "";
            String currentLine = "";

            for (currentLine = bufferedReader.readLine(); currentLine != null; currentLine = bufferedReader.readLine()) {
              all += currentLine + "\n";
            }

            inputTextField.setText(all);

            bufferedReader.close();
          } catch(Exception err) {
            System.out.println("file error: " + err.getMessage());
          }
        } else {
          System.out.println("no file selected");
        }
      }
    });
    cipherSelectPanel.add(openFileButton); cipherSelectPanel.add(cipherSelectLabel); cipherSelectPanel.add(cipherSelect);
    

    // Cipher options
    cipherOptionsPanel = new JPanel();
    cipherOptionsPanel.setBorder(BorderFactory.createTitledBorder("Cipher Input Configuration"));
    cipherOptionsLabel = new JLabel("Configure Cipher Options");
    cipherOptionsSelect = new JComboBox<>(new String[]{});
    cipherOptionsValue = new JTextField(12);
    cipherOptionsValue.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update();
      }

      void update() {
        controller.setSelectedCipherInput(cipherOptionsValue.getText());
      }
    });
    cipherOptionsSelect.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        controller.selectCipherInput((String) e.getItem());
        cipherOptionsValue.setText(controller.getSelectedCipherInput());
      }
    });
    cipherOptionsPanel.add(cipherOptionsLabel);
    cipherOptionsPanel.add(cipherOptionsSelect);
    cipherOptionsPanel.add(cipherOptionsValue);


    // Input/output text
    textFieldPanel = new JPanel();
    textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
    textFieldPanel.setBorder(BorderFactory.createTitledBorder("Plaintext / Ciphertext Display"));
    
    inputTextFieldLabel = new JLabel("Input Text");
    inputTextField = new JTextArea();
    inputTextField.setLineWrap(true);
    inputTextFieldScroll = new JScrollPane(inputTextField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  
    outputTextFieldLabel = new JLabel("Output Text");
    outputTextField = new JTextArea();
    outputTextField.setLineWrap(true);
    outputTextFieldScroll = new JScrollPane(outputTextField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    textFieldPanel.add(inputTextFieldLabel);
    textFieldPanel.add(inputTextFieldScroll);
    textFieldPanel.add(outputTextFieldLabel);
    textFieldPanel.add(outputTextFieldScroll);

    // Encrypt/decrypt buttons
    buttonsPanel = new JPanel();
    buttonsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
    encryptButton = new JButton("Encrypt");
    decryptButton = new JButton("Decrypt");
    saveButton = new JButton("Save File...");

    // Dispatch encrypt action to controller
    encryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        outputTextField.setText(controller.encrypt(inputTextField.getText()));
      }
    });

    // Dispatch decrypt action to controller
    decryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        outputTextField.setText(controller.decrypt(inputTextField.getText()));
      }
    });

    // Handles saving of files

    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == fileChooser.APPROVE_OPTION) {
          try {
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));
            String[] lines = outputTextField.getText().split("\\r?\\n");
            for (String line : lines) { 
              bWriter.write(line);
              bWriter.newLine(); 
            }
            bWriter.close();
            System.out.println("Wrote " + Arrays.toString(lines) + " (" + lines.length + ") lines to " + fileChooser.getSelectedFile().getAbsolutePath());
          } catch (Exception err) {
            JOptionPane.showMessageDialog(
              null, 
              "An error occurred while trying to save the file: " + err.getMessage(), 
              "Error saving file", 
              JOptionPane.ERROR_MESSAGE
              );
          }

        }
      }
    });

    
    buttonsPanel.add(encryptButton);
    buttonsPanel.add(decryptButton);
    buttonsPanel.add(saveButton);

    // Adding all these things to main panel
    panel.add(cipherSelectPanel);
    panel.add(cipherOptionsPanel);
    panel.add(textFieldPanel);
    panel.add(buttonsPanel);

    UIChangeCipherAlgorithm(controller.getAvailableCipherAlgorithms()[0]); // So that the program doesn't start in a blank state
    
    frame.add(panel);
    frame.setSize(400, 500);
    frame.setVisible(true);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
