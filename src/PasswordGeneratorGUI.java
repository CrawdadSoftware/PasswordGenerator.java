import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Frontend

public class PasswordGeneratorGUI extends JFrame
{
    private PasswordGenerator passwordGenerator;

    public PasswordGeneratorGUI()
    {
        // render frame and add a title
        super("Password Generator");

        setSize(540, 570);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Path for background file
        setBackgroundImage("resources/background.jpg");

        // init password generator
        passwordGenerator = new PasswordGenerator();

        // GUI
        addGuiComponents();
    }

    private void addGuiComponents()
    {

        JLabel titleLabel = new JLabel("Password Generator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 540, 39);

        // titleLabel text color
        titleLabel.setForeground(Color.BLUE);

        add(titleLabel);


        JTextArea passwordOutput = new JTextArea();
        passwordOutput.setEditable(false);
        passwordOutput.setFont(new Font("Dialog", Font.BOLD, 32));

        // passwordOutput text color
        passwordOutput.setForeground(Color.BLUE);

        // Add Scroll
        JScrollPane passwordOutputPane = new JScrollPane(passwordOutput);
        passwordOutputPane.setBounds(25, 97, 479, 70);
        passwordOutputPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        add(passwordOutputPane);

        // Password length label
        JLabel passwordLengthLabel = new JLabel("Password Length: ");
        passwordLengthLabel.setFont(new Font("Dialog", Font.PLAIN, 32));

        // passwordLengthLabel text color
        passwordLengthLabel.setForeground(Color.BLUE);

        passwordLengthLabel.setBounds(25, 215, 272, 39);
        add(passwordLengthLabel);

        // create password length input
        JTextArea passwordLengthInputArea = new JTextArea();

        // passwordLenghtInputArea text color
        passwordLengthInputArea.setForeground(Color.BLUE);

        passwordLengthInputArea.setFont(new Font("Dialog", Font.PLAIN, 32));
        passwordLengthInputArea.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        passwordLengthInputArea.setBounds(310, 215, 192, 39);

        // Simple validation
        passwordLengthInputArea.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                validateInput();
            }

            private void validateInput()
            {
                String inputText = passwordLengthInputArea.getText();
                if (!inputText.matches("\\d*"))
                {
                    // Only allow digits, Remove non-digits
                    passwordLengthInputArea.setText(inputText.replaceAll("\\D", ""));
                }
            }
        });

        add(passwordLengthInputArea);

        // Toggle buttons

        // uppercase letter toggle
        JToggleButton uppercaseToggle = new JToggleButton("Uppercase");

        // uppercaseToggle text color
        uppercaseToggle.setForeground(Color.BLUE);

        uppercaseToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        uppercaseToggle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        uppercaseToggle.setBounds(25, 302, 225, 56);
        add(uppercaseToggle);

        // lowercase letter toggle
        JToggleButton lowercaseToggle = new JToggleButton("Lowercase");

        // lowercaseToggle text color
        lowercaseToggle.setForeground(Color.BLUE);

        lowercaseToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        lowercaseToggle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        lowercaseToggle.setBounds(282, 302, 225, 56);
        add(lowercaseToggle);

        // numbers toggle
        JToggleButton numbersToggle = new JToggleButton("Numbers");

        // numbersToggle text color
        numbersToggle.setForeground(Color.BLUE);

        numbersToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        numbersToggle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        numbersToggle.setBounds(25, 373, 225, 56);
        add(numbersToggle);

        // symbols toggle
        JToggleButton symbolsToggle = new JToggleButton("Symbols");

        // symbolsToggle text color
        symbolsToggle.setForeground(Color.BLUE);

        symbolsToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        symbolsToggle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        symbolsToggle.setBounds(282, 373, 225, 56);
        add(symbolsToggle);

        // Initialization for Toggles
        uppercaseToggle.setSelected(true);
        lowercaseToggle.setSelected(true);
        numbersToggle.setSelected(true);
        symbolsToggle.setSelected(true);

        // create generate button
        JButton generateButton = new JButton("Generate");

        // generateButton text color
        generateButton.setForeground(Color.BLUE);

        generateButton.setFont(new Font("Dialog", Font.PLAIN, 32));
        //generateButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        generateButton.setBounds(155, 477, 222, 41);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // validation: generate a password only when length > 0 and one of the toggled buttons is pressed
                if (passwordLengthInputArea.getText().length() <= 0) return;
                boolean anyToggleSelected = lowercaseToggle.isSelected() ||
                        uppercaseToggle.isSelected() ||
                        numbersToggle.isSelected() ||
                        symbolsToggle.isSelected();

                // generate password

                // converts the text to an integer value
                int passwordLength = Integer.parseInt(passwordLengthInputArea.getText());
                if (anyToggleSelected) {
                    String generatedPassword = passwordGenerator.generatePassword(passwordLength,
                            uppercaseToggle.isSelected(),
                            lowercaseToggle.isSelected(),
                            numbersToggle.isSelected(),
                            symbolsToggle.isSelected());

                    // display password back to the user
                    passwordOutput.setText(generatedPassword);
                }
            }
        });

        add(generateButton);

        // Create copy button
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(new Font("Dialog", Font.PLAIN, 16));
        copyButton.setForeground(Color.BLUE);
        copyButton.setBounds(385, 477, 119, 41);
        copyButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String generatedPassword = passwordOutput.getText();
                if (!generatedPassword.isEmpty())
                {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection stringSelection = new StringSelection(generatedPassword);
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(PasswordGeneratorGUI.this,
                            "Password copied to clipboard!");
                }
            }
        });
        add(copyButton);
    }

    // Method for load and set the background image
    private void setBackgroundImage(String resourcePath)
    {
        try
        {
            File imageFile = new File(resourcePath);
            BufferedImage backgroundImage = ImageIO.read(imageFile);
            setContentPane(new JLabel(new ImageIcon(backgroundImage)));

            // Background resize
            Image scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            setContentPane(new JLabel(new ImageIcon(scaledImage)));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}