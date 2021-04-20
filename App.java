import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
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

  public static void main(String[] args) throws Exception {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
      System.out.println("Look and feel not found");
    }
    // Initialize UI
    createUI();
  }

  static void UIChangeCipherAlgorithm(String name) {
    controller.setCipherAlgorithm(name);
    cipherOptionsSelect.removeAllItems();
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(controller.getCipherInputs());
    cipherOptionsSelect.setModel(model);
    cipherOptionsValue.setText(controller.getSelectedCipherInput());
  }

  static boolean isTextFile(File f) throws Exception {
        String type = Files.probeContentType(f.toPath());
        if (type == null) {
            //type couldn't be determined, assume binary
            return false;
        } else if (type.startsWith("text")) {
            return true;
        } else {
            //type isn't text
            return false;
        }
    }

  static void createUI() throws Exception {
    // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

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
    openFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
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

          BufferedReader bufferedReader;
          try {
            var reader = new FileReader(selectedFile);
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

    encryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        outputTextField.setText(controller.encrypt(inputTextField.getText()));
      }
    });

    decryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        outputTextField.setText(controller.decrypt(inputTextField.getText()));
      }
    });

    buttonsPanel.add(encryptButton);
    buttonsPanel.add(decryptButton);

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
