import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class App {
  static Controller controller = new Controller();

  // UI elements 
  static JFrame frame;
  static JPanel panel;

  static JPanel cipherSelectPanel;
  static JLabel cipherSelectLabel;
  static JComboBox<String> cipherSelect;

  static JPanel cipherOptionsPanel;
  static JLabel cipherOptionsLabel;
  static JComboBox<String> cipherOptionsSelect;
  static JTextField cipherOptionsValue;

  static JLabel inputTextFieldLabel;
  static JTextArea inputTextField;
  static JLabel outputTextFieldLabel;
  static JTextArea outputTextField;

  static JPanel buttonsPanel;
  static JButton encryptButton;
  static JButton decryptButton;

  public static void main(String[] args) throws Exception {
    // Initialize UI
    createUI();
  }

  static void createUI() throws Exception {
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

    frame = new JFrame();
    panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    // Cipher algorithm selection
    cipherSelectPanel = new JPanel();
    cipherSelectLabel = new JLabel("Cipher Algorithm");
    cipherSelect = new JComboBox<>(controller.getAvailableCipherAlgorithms());
    cipherSelect.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          controller.setCipherAlgorithm((String) e.getItem());
          
          cipherOptionsSelect.removeAllItems();
          DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(controller.getCipherInputs());
          cipherOptionsSelect.setModel(model);
          cipherOptionsValue.setText(controller.getSelectedCipherInput());
        }
      }
    });
    cipherSelectPanel.add(cipherSelectLabel); cipherSelectPanel.add(cipherSelect);
    

    // Cipher options
    cipherOptionsPanel = new JPanel();
    cipherOptionsLabel = new JLabel("Configure Cipher Options");
    cipherOptionsSelect = new JComboBox<>(new String[]{});
    cipherOptionsValue = new JTextField();
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


    // Input/output text
    inputTextFieldLabel = new JLabel("Input Text");
    inputTextField = new JTextArea();
    outputTextFieldLabel = new JLabel("Output Text");
    outputTextField = new JTextArea();

    // Encrypt/decrypt buttons
    buttonsPanel = new JPanel();
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
    panel.add(cipherOptionsValue);

    panel.add(inputTextFieldLabel);
    panel.add(inputTextField);
    panel.add(outputTextFieldLabel);
    panel.add(outputTextField);

    panel.add(buttonsPanel);
    
    frame.add(panel);
    frame.setSize(400, 500);
    frame.setVisible(true);
  }
}
