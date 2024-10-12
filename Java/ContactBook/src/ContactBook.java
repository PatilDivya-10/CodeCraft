import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ContactBook extends JFrame {
    private JTextField nameField, phoneField, emailField, addressField;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private ArrayList<Contact> contactList;
    private File contactFile = new File("contacts.csv");

    public ContactBook() {
        setTitle("Enhanced Contact Book with File Storage");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 240, 240));

        contactList = new ArrayList<>();
        loadContactsFromFile(); // Load contacts from file on startup

        // Create the form panel with a grid layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(230, 230, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(null, "Add/Edit Contact", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Fields for contact details
        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        emailField = new JTextField(15);
        addressField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(addressField, gbc);

        // Buttons for adding, editing, and deleting contacts
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(addButton, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(editButton, gbc);
        gbc.gridx = 2; gbc.gridy = 4;
        formPanel.add(deleteButton, gbc);

        // Table for displaying contacts
        tableModel = new DefaultTableModel(new Object[]{"Name", "Phone", "Email", "Address"}, 0);
        contactTable = new JTable(tableModel);
        contactTable.setFont(new Font("Arial", Font.PLAIN, 14));
        contactTable.setRowHeight(25);
        contactTable.setSelectionBackground(new Color(173, 216, 230));
        contactTable.setSelectionForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(contactTable);

        // Add components to the main frame
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners for buttons
        addButton.addActionListener((ActionEvent e) -> addContact());
        editButton.addActionListener((ActionEvent e) -> editContact());
        deleteButton.addActionListener((ActionEvent e) -> deleteContact());

        // Populate table with contacts loaded from the file
        for (Contact contact : contactList) {
            tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress()});
        }
    }

    // Style for buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // Steel Blue color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Add a new contact
    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phone, email, address);
        contactList.add(contact);
        tableModel.addRow(new Object[]{name, phone, email, address});
        saveContactsToFile();
        clearFields();
    }

    // Edit selected contact
    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a contact to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Populate fields with selected contact's data
        nameField.setText((String) tableModel.getValueAt(selectedRow, 0));
        phoneField.setText((String) tableModel.getValueAt(selectedRow, 1));
        emailField.setText((String) tableModel.getValueAt(selectedRow, 2));
        addressField.setText((String) tableModel.getValueAt(selectedRow, 3));

        // Now, update the contact upon clicking the "Edit" button again
        JButton confirmEditButton = new JButton("Confirm Edit");
        confirmEditButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Contact contact = contactList.get(selectedRow);
            contact.setName(name);
            contact.setPhone(phone);
            contact.setEmail(email);
            contact.setAddress(address);

            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(phone, selectedRow, 1);
            tableModel.setValueAt(email, selectedRow, 2);
            tableModel.setValueAt(address, selectedRow, 3);
            saveContactsToFile();
            clearFields();
            // Remove the confirmation button after editing
            removeConfirmEditButton(confirmEditButton);
        });

        // Add the confirm button to the form panel
        ((JPanel) getContentPane().getComponent(0)).add(confirmEditButton);
        revalidate(); // Refresh the layout
        repaint(); // Repaint to show the confirm button
    }

    // Remove the confirm edit button
    private void removeConfirmEditButton(JButton confirmEditButton) {
        ((JPanel) getContentPane().getComponent(0)).remove(confirmEditButton);
        revalidate();
        repaint();
    }

    // Delete selected contact
    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a contact to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        contactList.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        saveContactsToFile();
    }

    // Save contacts to file
    private void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contactFile))) {
            for (Contact contact : contactList) {
                writer.write(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail() + "," + contact.getAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load contacts from file
    private void loadContactsFromFile() {
        if (!contactFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(contactFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Contact contact = new Contact(parts[0], parts[1], parts[2], parts[3]);
                    contactList.add(contact);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear input fields
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactBook contactBook = new ContactBook();
            contactBook.setVisible(true);
        });
    }
}
