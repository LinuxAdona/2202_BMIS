/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package main;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import strt.Login;
import Database.DBConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ADMIN
 */
public class MainFrame extends javax.swing.JFrame {
    private final Color DEFAULT_COLOR = new Color(0, 0, 0);
    private final Color ACTIVE_COLOR = new Color(0, 128, 241);
    private ImageIcon homeDefaultIcon;
    private ImageIcon homeActiveIcon;

    /**
     * Creates new form Dashboard
     */
    public MainFrame() {
        initComponents();
        loadCards();
        loadResidents("");
        loadResCert("");
        loadFamilies("");
        loadHouses("");
        updateNotificationCount();

        homeDefaultIcon = new ImageIcon(getClass().getResource("/assets/home-solid-24.png"));
        homeActiveIcon = new ImageIcon(getClass().getResource("/assets/home-solid-24-black.png"));

        HomePage.setVisible(true);
        ResidentPage.setVisible(false);
        FamiliesPage.setVisible(false);
        HousesPage.setVisible(false);
        CertificatesPage.setVisible(false);

        lblHome.setIcon(homeDefaultIcon);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private int getLoggedInUserID() {
        return Login.loggedInUserID;
    }

    private void loadCards() {
        String sql = "CALL GetAllCards()";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String residents = rs.getString("residents");
                    String families = rs.getString("families");
                    String officials = rs.getString("officials");
                    String houses = rs.getString("houses");

                    lblResidents.setText(residents);
                    lblFamilies.setText(families);
                    lblOfficials.setText(officials);
                    lblHouses.setText(houses);
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }

    public void loadResidents(String searchTerm, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "CALL GetAllResidents(?)";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, searchTerm);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int residentId = rs.getInt("resident_id");
                    String name = rs.getString("name");
                    String gender = rs.getString("gender");

                    model.addRow(new Object[]{residentId, name, gender});
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }

    public void loadResidents(String searchTerm) {
        loadResidents(searchTerm, tbResidents);
    }

    public void loadResCert(String searchTerm) {
        loadResidents(searchTerm, tbResCert);
    }

    public void loadFamilies(String searchTerm) {
        DefaultTableModel model = (DefaultTableModel) tbFamilies.getModel();
        model.setRowCount(0);

        String sql = "CALL GetAllFamily(?)";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, searchTerm);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int familyId = rs.getInt("family_id");
                    String name = rs.getString("name");
                    String head = rs.getString("head");

                    model.addRow(new Object[]{familyId, name, head});
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }

    public void loadHouses(String searchTerm) {
        DefaultTableModel model = (DefaultTableModel) tbHouses.getModel();
        model.setRowCount(0);

        String sql = "CALL GetAllHouses(?)";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, searchTerm);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int familyId = rs.getInt("house_id");
                    String houseNo = rs.getString("house_number");
                    String street = rs.getString("street");
                    int families = rs.getInt("families");

                    model.addRow(new Object[]{familyId, houseNo, street, families});
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }
    
    private void loadOfficials() {
        // Create the table model
        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Position"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new java.awt.Font("Poppins", java.awt.Font.PLAIN, 18));
        table.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(table);

        // Fetch data from the database
        String sql = "CALL GetAllOfficials()";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String position = rs.getString("position");
                model.addRow(new Object[]{name, position});
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }

        // Show the table in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Barangay Officials", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadHealthRecords(int residentId) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Diagnosis", "Remarks"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new java.awt.Font("Poppins", java.awt.Font.PLAIN, 12));
        table.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 18));
        JScrollPane scrollPane = new JScrollPane(table);

        String sql = "CALL GetHealthRecords(?)";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date_recorded");
                String diagnosis = rs.getString("diagnosis");
                String remarks = rs.getString("remarks");
                model.addRow(new Object[]{date, diagnosis, remarks});
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }

        // Create "Add Record" button
        JButton btnAddRecord = new JButton("Add Health Record");
        btnAddRecord.setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 16));
        btnAddRecord.addActionListener(e -> addHealthRecord(residentId, model));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAddRecord, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Health Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addHealthRecord(int residentId, DefaultTableModel model) {
        // Predefined list of diagnoses
        String[] diagnoses = {"General Checkup", "Fever", "Cough", "Hypertension", "Diabetes", "Flu", "Asthma", "Allergy", "Other"};
        JComboBox<String> diagnosisBox = new JComboBox<>(diagnoses);
        JTextField remarksField = new JTextField();

        Object[] message = {
            "Diagnosis:", diagnosisBox,
            "Remarks:", remarksField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Health Record", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String diagnosis = (String) diagnosisBox.getSelectedItem();
            String remarks = remarksField.getText().trim();

            String sql = "INSERT INTO health_records (resident_id, date_recorded, diagnosis, remarks) VALUES (?, CURDATE(), ?, ?)";
            try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, residentId);
                ps.setString(2, diagnosis);
                ps.setString(3, remarks);
                ps.executeUpdate();

                // Add new record to JTable
                model.addRow(new Object[]{LocalDate.now().toString(), diagnosis, remarks});
                JOptionPane.showMessageDialog(this, "Health Record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }
        }
    }
    
    private void loadBlotterRecords(int residentId) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Complainant", "Respondent", "Case Details", "Date Filed", "Status"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new java.awt.Font("Poppins", java.awt.Font.PLAIN, 16));
        table.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(table);

        String sql = "SELECT * FROM blotter_records WHERE complainant = (SELECT CONCAT(first_name, ' ', last_name) FROM resident WHERE resident_id = ?) OR respondent = (SELECT CONCAT(first_name, ' ', last_name) FROM resident WHERE resident_id = ?)";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            ps.setInt(2, residentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int blotterId = rs.getInt("blotter_id");
                String complainant = rs.getString("complainant");
                String respondent = rs.getString("respondent");
                String caseDetails = rs.getString("case_details");
                String dateFiled = rs.getString("date_filed");
                String status = rs.getString("status");
                model.addRow(new Object[]{blotterId, complainant, respondent, caseDetails, dateFiled, status});
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }

        JButton btnAddBlotter = new JButton("Add Blotter Record");
        btnAddBlotter.setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 16));
        btnAddBlotter.addActionListener(e -> addBlotterRecord(residentId, model));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAddBlotter, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Blotter Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addBlotterRecord(int residentId, DefaultTableModel model) {
        // Select complainant
        int complainantId = selectResident("Select Complainant");
        if (complainantId == -1) {
            return; // User canceled selection
        }
        // Select respondent
        int respondentId = selectResident("Select Respondent");
        if (respondentId == -1) {
            return; // User canceled selection
        }
        // Enter case details
        JTextArea caseDetailsField = new JTextArea(5, 20);
        JScrollPane caseScroll = new JScrollPane(caseDetailsField);

        Object[] message = {"Case Details:", caseScroll};
        int option = JOptionPane.showConfirmDialog(this, message, "Add Blotter Record", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        String caseDetails = caseDetailsField.getText().trim();
        if (caseDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Case details cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve names for complainant and respondent
        String complainantName = getResidentName(complainantId);
        String respondentName = getResidentName(respondentId);

        // Insert into database
        String sql = "INSERT INTO blotter_records (complainant, respondent, case_details, date_filed, status) VALUES (?, ?, ?, CURDATE(), 'Pending')";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, complainantName);
            ps.setString(2, respondentName);
            ps.setString(3, caseDetails);
            ps.executeUpdate();

            loadBlotterRecords(residentId);
            JOptionPane.showMessageDialog(this, "Blotter Record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }
    
    private int selectResident(String title) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new java.awt.Font("Poppins", java.awt.Font.PLAIN, 16));
        table.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(table);

        // Fetch residents from the database
        String sql = "SELECT resident_id, first_name, last_name FROM resident";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("resident_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                model.addRow(new Object[]{id, firstName, lastName});
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
            return -1;
        }

        int option = JOptionPane.showConfirmDialog(this, scrollPane, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                return (int) table.getValueAt(selectedRow, 0); // Return resident_id
            }
        }
        return -1; // Return -1 if no selection was made
    }

    private String getResidentName(int residentId) {
        String sql = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM resident WHERE resident_id = ?";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
        return "Unknown"; // Default if not found
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        ContentPane = new javax.swing.JLayeredPane();
        ResidentPage = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        lblRefresh = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResidents = new javax.swing.JTable();
        btnDeleteRes = new javax.swing.JButton();
        btnViewRes = new javax.swing.JButton();
        HomePage = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblResidents = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblFamilies = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblOfficials = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblHouses = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        FamiliesPage = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        lblSearch1 = new javax.swing.JLabel();
        txtSearch1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        lblRefresh1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbFamilies = new javax.swing.JTable();
        btnDeleteFam = new javax.swing.JButton();
        btnViewFam = new javax.swing.JButton();
        btnAddFam = new javax.swing.JButton();
        HousesPage = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        lblSearch2 = new javax.swing.JLabel();
        txtSearch2 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        lblRefresh2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbHouses = new javax.swing.JTable();
        btnDeleteHouse = new javax.swing.JButton();
        btnViewHouse = new javax.swing.JButton();
        btnAddHouse = new javax.swing.JButton();
        CertificatesPage = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lblSearch3 = new javax.swing.JLabel();
        txtSearch3 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        lblRefresh3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbResCert = new javax.swing.JTable();
        btnCertificate = new javax.swing.JButton();
        btnHealth = new javax.swing.JButton();
        btnOfficials = new javax.swing.JButton();
        btnBlotter = new javax.swing.JButton();
        NavPane = new javax.swing.JPanel();
        lblHome = new javax.swing.JLabel();
        lblResP = new javax.swing.JLabel();
        lblFamP = new javax.swing.JLabel();
        lblHouseP = new javax.swing.JLabel();
        lblLogout = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblCertificates = new javax.swing.JLabel();
        lblNotifs = new javax.swing.JLabel();
        lblCount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Barangay Management Information System");
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(247, 247, 247));

        ResidentPage.setBackground(new java.awt.Color(247, 247, 247));
        ResidentPage.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel9.setText("Resident Tracker");

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));

        lblSearch.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-regular-36.png"))); // NOI18N
        lblSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSearchMouseClicked(evt);
            }
        });

        txtSearch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel11.setText("Resident List");

        lblRefresh.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblRefresh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/refresh-regular-36.png"))); // NOI18N
        lblRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRefreshMouseClicked(evt);
            }
        });

        tbResidents.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        tbResidents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Gender"
            }
        ));
        tbResidents.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbResidents.setRowHeight(50);
        tbResidents.getTableHeader().setResizingAllowed(false);
        tbResidents.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbResidents);
        if (tbResidents.getColumnModel().getColumnCount() > 0) {
            tbResidents.getColumnModel().getColumn(0).setMaxWidth(50);
            tbResidents.getColumnModel().getColumn(2).setMinWidth(200);
            tbResidents.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        btnDeleteRes.setBackground(new java.awt.Color(255, 153, 153));
        btnDeleteRes.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDeleteRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-minus-regular-24.png"))); // NOI18N
        btnDeleteRes.setText("Delete");
        btnDeleteRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteResActionPerformed(evt);
            }
        });

        btnViewRes.setBackground(new java.awt.Color(153, 153, 255));
        btnViewRes.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnViewRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/show-regular-24.png"))); // NOI18N
        btnViewRes.setText("View");
        btnViewRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout ResidentPageLayout = new javax.swing.GroupLayout(ResidentPage);
        ResidentPage.setLayout(ResidentPageLayout);
        ResidentPageLayout.setHorizontalGroup(
            ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResidentPageLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addGroup(ResidentPageLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                        .addComponent(btnViewRes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteRes)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ResidentPageLayout.setVerticalGroup(
            ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResidentPageLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ResidentPageLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11))
                    .addGroup(ResidentPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteRes)
                            .addComponent(btnViewRes)))
                    .addGroup(ResidentPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ResidentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        HomePage.setBackground(new java.awt.Color(247, 247, 247));
        HomePage.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/imgs/icons/People.png"))); // NOI18N

        lblResidents.setFont(new java.awt.Font("Poppins", 1, 64)); // NOI18N
        lblResidents.setText("0");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Residents");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblResidents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(lblResidents)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 221, 221));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/imgs/icons/Families.png"))); // NOI18N

        lblFamilies.setFont(new java.awt.Font("Poppins", 1, 64)); // NOI18N
        lblFamilies.setText("0");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("Families");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(lblFamilies, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(lblFamilies)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/imgs/icons/Officials.png"))); // NOI18N

        lblOfficials.setFont(new java.awt.Font("Poppins", 1, 64)); // NOI18N
        lblOfficials.setText("0");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Officials");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOfficials, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(lblOfficials)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 204, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblHouses.setFont(new java.awt.Font("Poppins", 1, 64)); // NOI18N
        lblHouses.setText("0");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setText("Houses");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/imgs/icons/Purok.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(lblHouses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(lblHouses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout HomePageLayout = new javax.swing.GroupLayout(HomePage);
        HomePage.setLayout(HomePageLayout);
        HomePageLayout.setHorizontalGroup(
            HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePageLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        HomePageLayout.setVerticalGroup(
            HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomePageLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HomePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        FamiliesPage.setBackground(new java.awt.Color(247, 247, 247));
        FamiliesPage.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel10.setText("Families");

        jSeparator3.setBackground(new java.awt.Color(204, 204, 204));

        lblSearch1.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblSearch1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-regular-36.png"))); // NOI18N
        lblSearch1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSearch1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSearch1MouseClicked(evt);
            }
        });

        txtSearch1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel12.setText("Family List");

        lblRefresh1.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblRefresh1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/refresh-regular-36.png"))); // NOI18N
        lblRefresh1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRefresh1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRefresh1MouseClicked(evt);
            }
        });

        tbFamilies.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        tbFamilies.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Head"
            }
        ));
        tbFamilies.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbFamilies.setRowHeight(50);
        tbFamilies.getTableHeader().setResizingAllowed(false);
        tbFamilies.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbFamilies);
        if (tbFamilies.getColumnModel().getColumnCount() > 0) {
            tbFamilies.getColumnModel().getColumn(0).setMaxWidth(50);
            tbFamilies.getColumnModel().getColumn(2).setMinWidth(200);
            tbFamilies.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        btnDeleteFam.setBackground(new java.awt.Color(255, 153, 153));
        btnDeleteFam.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDeleteFam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-minus-regular-24.png"))); // NOI18N
        btnDeleteFam.setText("Delete");
        btnDeleteFam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteFam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFamActionPerformed(evt);
            }
        });

        btnViewFam.setBackground(new java.awt.Color(153, 153, 255));
        btnViewFam.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnViewFam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/show-regular-24.png"))); // NOI18N
        btnViewFam.setText("View");
        btnViewFam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAddFam.setBackground(new java.awt.Color(153, 255, 153));
        btnAddFam.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnAddFam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-plus-regular-24.png"))); // NOI18N
        btnAddFam.setText("Add");
        btnAddFam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddFam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FamiliesPageLayout = new javax.swing.GroupLayout(FamiliesPage);
        FamiliesPage.setLayout(FamiliesPageLayout);
        FamiliesPageLayout.setHorizontalGroup(
            FamiliesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FamiliesPageLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(FamiliesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10)
                    .addGroup(FamiliesPageLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(btnAddFam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewFam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteFam)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jSeparator3))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        FamiliesPageLayout.setVerticalGroup(
            FamiliesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FamiliesPageLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(FamiliesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FamiliesPageLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel12))
                    .addGroup(FamiliesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(FamiliesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteFam)
                            .addComponent(btnViewFam)
                            .addComponent(btnAddFam)))
                    .addGroup(FamiliesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FamiliesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HousesPage.setBackground(new java.awt.Color(247, 247, 247));
        HousesPage.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel13.setText("Houses");

        jSeparator4.setBackground(new java.awt.Color(204, 204, 204));

        lblSearch2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblSearch2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearch2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-regular-36.png"))); // NOI18N
        lblSearch2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSearch2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSearch2MouseClicked(evt);
            }
        });

        txtSearch2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel14.setText("House List");

        lblRefresh2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblRefresh2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefresh2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/refresh-regular-36.png"))); // NOI18N
        lblRefresh2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRefresh2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRefresh2MouseClicked(evt);
            }
        });

        tbHouses.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        tbHouses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "House Number", "Street", "Families"
            }
        ));
        tbHouses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbHouses.setRowHeight(50);
        tbHouses.getTableHeader().setResizingAllowed(false);
        tbHouses.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbHouses);
        if (tbHouses.getColumnModel().getColumnCount() > 0) {
            tbHouses.getColumnModel().getColumn(0).setMaxWidth(50);
            tbHouses.getColumnModel().getColumn(2).setMinWidth(200);
            tbHouses.getColumnModel().getColumn(2).setMaxWidth(200);
            tbHouses.getColumnModel().getColumn(3).setMinWidth(80);
            tbHouses.getColumnModel().getColumn(3).setMaxWidth(80);
        }

        btnDeleteHouse.setBackground(new java.awt.Color(255, 153, 153));
        btnDeleteHouse.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDeleteHouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-minus-regular-24.png"))); // NOI18N
        btnDeleteHouse.setText("Delete");
        btnDeleteHouse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHouseActionPerformed(evt);
            }
        });

        btnViewHouse.setBackground(new java.awt.Color(153, 153, 255));
        btnViewHouse.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnViewHouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/show-regular-24.png"))); // NOI18N
        btnViewHouse.setText("View");
        btnViewHouse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAddHouse.setBackground(new java.awt.Color(153, 255, 153));
        btnAddHouse.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnAddHouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-plus-regular-24.png"))); // NOI18N
        btnAddHouse.setText("Add");
        btnAddHouse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHouseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HousesPageLayout = new javax.swing.GroupLayout(HousesPage);
        HousesPage.setLayout(HousesPageLayout);
        HousesPageLayout.setHorizontalGroup(
            HousesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HousesPageLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(HousesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(HousesPageLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(btnAddHouse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewHouse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteHouse)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblRefresh2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3)
                    .addComponent(jSeparator4))
                .addGap(22, 22, 22))
        );
        HousesPageLayout.setVerticalGroup(
            HousesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HousesPageLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(HousesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HousesPageLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel14))
                    .addGroup(HousesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(HousesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteHouse)
                            .addComponent(btnViewHouse)
                            .addComponent(btnAddHouse)))
                    .addGroup(HousesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HousesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRefresh2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CertificatesPage.setBackground(new java.awt.Color(247, 247, 247));
        CertificatesPage.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel15.setText("Certificate Issuance");

        jSeparator5.setBackground(new java.awt.Color(204, 204, 204));

        lblSearch3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblSearch3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearch3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search-regular-36.png"))); // NOI18N
        lblSearch3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSearch3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSearch3MouseClicked(evt);
            }
        });

        txtSearch3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel16.setText("Resident List");

        lblRefresh3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblRefresh3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefresh3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/refresh-regular-36.png"))); // NOI18N
        lblRefresh3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRefresh3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRefresh3MouseClicked(evt);
            }
        });

        tbResCert.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        tbResCert.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Gender"
            }
        ));
        tbResCert.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbResCert.setRowHeight(50);
        tbResCert.getTableHeader().setResizingAllowed(false);
        tbResCert.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tbResCert);
        if (tbResCert.getColumnModel().getColumnCount() > 0) {
            tbResCert.getColumnModel().getColumn(0).setMaxWidth(50);
            tbResCert.getColumnModel().getColumn(2).setMinWidth(200);
            tbResCert.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        btnCertificate.setBackground(new java.awt.Color(238, 238, 238));
        btnCertificate.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnCertificate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/report-solid-24.png"))); // NOI18N
        btnCertificate.setText("Request Certificate");
        btnCertificate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCertificate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCertificateMouseClicked(evt);
            }
        });
        btnCertificate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCertificateActionPerformed(evt);
            }
        });

        btnHealth.setBackground(new java.awt.Color(238, 238, 238));
        btnHealth.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnHealth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/report-solid-24.png"))); // NOI18N
        btnHealth.setText("View Health Record");
        btnHealth.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHealth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHealthMouseClicked(evt);
            }
        });
        btnHealth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHealthActionPerformed(evt);
            }
        });

        btnOfficials.setBackground(new java.awt.Color(238, 238, 238));
        btnOfficials.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnOfficials.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/landmark-solid-24.png"))); // NOI18N
        btnOfficials.setText("View Officials");
        btnOfficials.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOfficials.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOfficialsMouseClicked(evt);
            }
        });
        btnOfficials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOfficialsActionPerformed(evt);
            }
        });

        btnBlotter.setBackground(new java.awt.Color(238, 238, 238));
        btnBlotter.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnBlotter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/report-solid-24.png"))); // NOI18N
        btnBlotter.setText("Blotter Record");
        btnBlotter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlotter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBlotterMouseClicked(evt);
            }
        });
        btnBlotter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlotterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CertificatesPageLayout = new javax.swing.GroupLayout(CertificatesPage);
        CertificatesPage.setLayout(CertificatesPageLayout);
        CertificatesPageLayout.setHorizontalGroup(
            CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CertificatesPageLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(btnHealth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCertificate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBlotter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(btnOfficials))
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblRefresh3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4)
                    .addComponent(jSeparator5))
                .addGap(22, 22, 22))
        );
        CertificatesPageLayout.setVerticalGroup(
            CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CertificatesPageLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnHealth)
                        .addComponent(btnCertificate)
                        .addComponent(btnBlotter)
                        .addComponent(btnOfficials))
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16))
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRefresh3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        ContentPane.setLayer(ResidentPage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ContentPane.setLayer(HomePage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ContentPane.setLayer(FamiliesPage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ContentPane.setLayer(HousesPage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ContentPane.setLayer(CertificatesPage, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout ContentPaneLayout = new javax.swing.GroupLayout(ContentPane);
        ContentPane.setLayout(ContentPaneLayout);
        ContentPaneLayout.setHorizontalGroup(
            ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ResidentPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContentPaneLayout.createSequentialGroup()
                    .addGap(0, 23, Short.MAX_VALUE)
                    .addComponent(HomePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 7, Short.MAX_VALUE)))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(FamiliesPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HousesPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(CertificatesPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ContentPaneLayout.setVerticalGroup(
            ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ResidentPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContentPaneLayout.createSequentialGroup()
                    .addGap(0, 21, Short.MAX_VALUE)
                    .addComponent(HomePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 22, Short.MAX_VALUE)))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContentPaneLayout.createSequentialGroup()
                    .addComponent(FamiliesPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HousesPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContentPaneLayout.createSequentialGroup()
                    .addComponent(CertificatesPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 15, Short.MAX_VALUE)))
        );

        NavPane.setBackground(new java.awt.Color(255, 255, 255));

        lblHome.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblHome.setForeground(new java.awt.Color(0, 128, 241));
        lblHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/home-solid-24.png"))); // NOI18N
        lblHome.setText(" Home");
        lblHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHomeMouseClicked(evt);
            }
        });

        lblResP.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblResP.setText(" Residents");
        lblResP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblResP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblResPMouseClicked(evt);
            }
        });

        lblFamP.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblFamP.setText("Families");
        lblFamP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblFamP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFamPMouseClicked(evt);
            }
        });

        lblHouseP.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblHouseP.setText("Houses");
        lblHouseP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblHouseP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHousePMouseClicked(evt);
            }
        });

        lblLogout.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/log-out-regular-24.png"))); // NOI18N
        lblLogout.setText("Log out ");
        lblLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLogout.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogoutMouseClicked(evt);
            }
        });

        lblCertificates.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblCertificates.setText("Documents");
        lblCertificates.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCertificates.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCertificatesMouseClicked(evt);
            }
        });

        lblNotifs.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblNotifs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/bell-solid-24.png"))); // NOI18N
        lblNotifs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblNotifs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNotifsMouseClicked(evt);
            }
        });

        lblCount.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lblCount.setText("0");
        lblCount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCountMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout NavPaneLayout = new javax.swing.GroupLayout(NavPane);
        NavPane.setLayout(NavPaneLayout);
        NavPaneLayout.setHorizontalGroup(
            NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavPaneLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblHome)
                .addGap(18, 18, 18)
                .addComponent(lblResP)
                .addGap(18, 18, 18)
                .addComponent(lblFamP)
                .addGap(18, 18, 18)
                .addComponent(lblHouseP)
                .addGap(18, 18, 18)
                .addComponent(lblCertificates)
                .addGap(307, 307, 307)
                .addGroup(NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavPaneLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblCount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblNotifs))
                .addGap(2, 2, 2)
                .addComponent(lblLogout))
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1005, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        NavPaneLayout.setVerticalGroup(
            NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavPaneLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(NavPaneLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHome)
                            .addComponent(lblResP)
                            .addComponent(lblFamP)
                            .addComponent(lblHouseP)
                            .addComponent(lblCertificates)
                            .addComponent(lblNotifs)
                            .addComponent(lblLogout))))
                .addGap(21, 21, 21)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContentPane)
            .addComponent(NavPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(NavPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(ContentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void generateIndigencyCertificatePDF(int residentId, String purpose) {
        Document document = new Document();
        String fileName = "Certificate_Indigency_" + residentId + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Fetch resident details from the database
            String name = "", address = "", citizenship = "Filipino", captain = "";
            int age = 0;
            try (Connection conn = DBConnection.Connect(); 
                    PreparedStatement ps = conn.prepareStatement("CALL GetResident(?)"); 
                    PreparedStatement psO = conn.prepareStatement("CALL GetOfficial('Barangay Captain/Chairman')")) {
                ps.setInt(1, residentId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    address = rs.getString("address");
                    age = rs.getInt("age");
                }
                
                ResultSet rsO = psO.executeQuery();
                
                if (rsO.next()) {
                    captain = rsO.getString("name");
                }
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }

            // Set fonts
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font brgyFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

            // Header
            String headerText = "Republic of the Philippines\nProvince of Batangas\nMunicipality of Balayan\nBarangay Malalay\nOffice Of The Punong Barangay\n_____________________________________________________";
            Paragraph header = new Paragraph(headerText.toUpperCase(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            Paragraph brgy = new Paragraph("BARANGAY MALALAY CERTIFICATE", brgyFont);
            brgy.setAlignment(Element.ALIGN_CENTER);
            document.add(brgy);
            
            Paragraph title = new Paragraph("CERTIFICATE OF INDIGENCY", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n\n\n"));

            // Body
            Paragraph body = new Paragraph();
            body.add(new Chunk("TO WHOM IT MAY CONCERN:\n\n", boldFont));
            body.add(new Chunk("This is to certify that ", regularFont));
            body.add(new Chunk(name, boldFont));
            body.add(new Chunk(", ", regularFont));
            body.add(new Chunk(age + " years old, ", regularFont));
            body.add(new Chunk(citizenship + " citizen, and a resident of ", regularFont));
            body.add(new Chunk(address, regularFont));
            body.add(new Chunk(", is classified as an indigent in this barangay.\n\n", regularFont));
            body.add(new Chunk("This certification is issued upon the request of the above-named person for ", regularFont));
            body.add(new Chunk(purpose, boldFont));
            body.add(new Chunk(" and for whatever legal purpose it may serve.\n\n", regularFont));
            document.add(body);

            // Date
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String date = today.format(formatter);
            Paragraph dateParagraph = new Paragraph("Issued this " + date + " at Barangay Malalay, Balayan, Batangas.", regularFont);
            dateParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(dateParagraph);

            // Footer
            document.add(new Paragraph("\n\n\n\n", regularFont));
            Paragraph signature = new Paragraph("__________________________\n" + captain, boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();
            openPDF(fileName);
            JOptionPane.showMessageDialog(this, "Indigency Certificate has been generated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            showErrorMessage("Error generating PDF: " + e.getMessage());
        }
    }

    private void generateBarangayClearancePDF(int residentId, String purpose) {
        Document document = new Document();
        String fileName = "Barangay_Clearance_" + residentId + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Fetch resident details from the database
            String name = "", address = "", citizenship = "Filipino", captain = "";
            int age = 0;
            try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement("CALL GetResident(?)"); PreparedStatement psO = conn.prepareStatement("CALL GetOfficial('Barangay Captain/Chairman')")) {
                ps.setInt(1, residentId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    address = rs.getString("address");
                    age = rs.getInt("age");
                }

                ResultSet rsO = psO.executeQuery();

                if (rsO.next()) {
                    captain = rsO.getString("name");
                }
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }

            // Set fonts
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font brgyFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

            // Header
            String headerText = "Republic of the Philippines\nProvince of Batangas\nMunicipality of Balayan\nBarangay Malalay\nOffice Of The Punong Barangay\n_____________________________________________________";
            Paragraph header = new Paragraph(headerText.toUpperCase(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            Paragraph brgy = new Paragraph("BARANGAY MALALAY CERTIFICATE", brgyFont);
            brgy.setAlignment(Element.ALIGN_CENTER);
            document.add(brgy);

            Paragraph title = new Paragraph("BARANGAY CLEARANCE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n\n\n"));

            // Body
            Paragraph body = new Paragraph();
            body.add(new Chunk("TO WHOM IT MAY CONCERN:\n\n", boldFont));
            body.add(new Chunk("This is to certify that ", regularFont));
            body.add(new Chunk(name, boldFont));
            body.add(new Chunk(", ", regularFont));
            body.add(new Chunk(age + " years old, and a resident of ", regularFont));
            body.add(new Chunk(address, regularFont));
            body.add(new Chunk(", has no derogatory record and is of good moral character.\n\n", regularFont));
            body.add(new Chunk("This clearance is issued upon request for ", regularFont));
            body.add(new Chunk(purpose, boldFont));
            body.add(new Chunk(" and any other legal purpose it may serve.\n\n", regularFont));
            document.add(body);

            // Date
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String date = today.format(formatter);
            document.add(new Paragraph("Issued this " + date + " at Barangay Malalay, Balayan, Batangas.", regularFont));

            // Signature
            document.add(new Paragraph("\n\n\n"));
            Paragraph signature = new Paragraph("__________________________\n" + captain, boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();
            openPDF(fileName);
            JOptionPane.showMessageDialog(this, "Barangay Clearance has been generated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            showErrorMessage("Error generating PDF: " + e.getMessage());
        }
    }

    private void generateBusinessPermitPDF(int residentId, String purpose) {
        Document document = new Document();
        String fileName = "Business_Permit_" + residentId + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Fetch resident details from the database
            String name = "", address = "", citizenship = "Filipino", captain = "";
            int age = 0;
            try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement("CALL GetResident(?)"); PreparedStatement psO = conn.prepareStatement("CALL GetOfficial('Barangay Captain/Chairman')")) {
                ps.setInt(1, residentId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    address = rs.getString("address");
                    age = rs.getInt("age");
                }

                ResultSet rsO = psO.executeQuery();

                if (rsO.next()) {
                    captain = rsO.getString("name");
                }
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }

            // Set fonts
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font brgyFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

            // Header
            String headerText = "Republic of the Philippines\nProvince of Batangas\nMunicipality of Balayan\nBarangay Malalay\nOffice Of The Punong Barangay\n_____________________________________________________";
            Paragraph header = new Paragraph(headerText.toUpperCase(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            Paragraph brgy = new Paragraph("BARANGAY MALALAY CERTIFICATE", brgyFont);
            brgy.setAlignment(Element.ALIGN_CENTER);
            document.add(brgy);

            Paragraph title = new Paragraph("BUSINESS PERMIT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n\n\n"));
            
            Paragraph body = new Paragraph();
            body.add(new Chunk("TO WHOM IT MAY CONCERN:\n\n", boldFont));
            body.add(new Chunk("This is to certify that ", regularFont));
            body.add(new Chunk(name, boldFont));
            body.add(new Chunk(", ", regularFont));
            body.add(new Chunk(age + " years old, ", regularFont));
            body.add(new Chunk(citizenship + " citizen, and a resident of ", regularFont));
            body.add(new Chunk(address, regularFont));
            body.add(new Chunk(", has been a law-abiding citizen and has no derogatory record in this barangay.\n\n", regularFont));
            body.add(new Chunk("This certification is issued upon request for ", regularFont));
            body.add(new Chunk(purpose, boldFont));
            body.add(new Chunk(" and for whatever legal purpose it may serve.\n\n", regularFont));
            document.add(body);

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String date = today.format(formatter);
            Paragraph dateParagraph = new Paragraph("Issued this " + date + " at Barangay Malalay, Balayan, Batangas.", regularFont);
            dateParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(dateParagraph);

            document.add(new Paragraph("\n\n\n\n", regularFont));
            Paragraph signature = new Paragraph("__________________________\n" + captain, boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();
            openPDF(fileName);
            JOptionPane.showMessageDialog(this, "Barangay Clearance has been generated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            showErrorMessage("Error generating PDF: " + e.getMessage());
        }
    }

    private void generateCertificateOfResidencyPDF(int residentId) {
        Document document = new Document();
        String fileName = "Certificate_Indigency_" + residentId + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Fetch resident details from the database
            String name = "", address = "", citizenship = "Filipino", captain = "";
            int age = 0;
            try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement("CALL GetResident(?)"); PreparedStatement psO = conn.prepareStatement("CALL GetOfficial('Barangay Captain/Chairman')")) {
                ps.setInt(1, residentId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    address = rs.getString("address");
                    age = rs.getInt("age");
                }

                ResultSet rsO = psO.executeQuery();

                if (rsO.next()) {
                    captain = rsO.getString("name");
                }
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }

            // Set fonts
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
            Font brgyFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

            // Header
            String headerText = "Republic of the Philippines\nProvince of Batangas\nMunicipality of Balayan\nBarangay Malalay\nOffice Of The Punong Barangay\n_____________________________________________________";
            Paragraph header = new Paragraph(headerText.toUpperCase(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));

            Paragraph brgy = new Paragraph("BARANGAY MALALAY CERTIFICATE", brgyFont);
            brgy.setAlignment(Element.ALIGN_CENTER);
            document.add(brgy);

            Paragraph title = new Paragraph("CERTIFICATE OF RESIDENCY", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n\n\n"));

            // Body
            Paragraph body = new Paragraph();
            body.add(new Chunk("TO WHOM IT MAY CONCERN:\n\n", boldFont));
            body.add(new Chunk("This is to certify that ", regularFont));
            body.add(new Chunk(name, boldFont));
            body.add(new Chunk(", ", regularFont));
            body.add(new Chunk(age + " years old, ", regularFont));
            body.add(new Chunk(citizenship + " citizen, and a resident of ", regularFont));
            body.add(new Chunk(address, regularFont));
            body.add(new Chunk(", is classified as one of the residents in this barangay.\n\n", regularFont));
            body.add(new Chunk("This certification is issued upon the request of the above-named person for ", regularFont));
            body.add(new Chunk("proof of residency", boldFont));
            body.add(new Chunk(" and for whatever legal purpose it may serve.\n\n", regularFont));
            document.add(body);

            // Date
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String date = today.format(formatter);
            Paragraph dateParagraph = new Paragraph("Issued this " + date + " at Barangay Malalay, Balayan, Batangas.", regularFont);
            dateParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(dateParagraph);

            // Footer
            document.add(new Paragraph("\n\n\n\n", regularFont));
            Paragraph signature = new Paragraph("__________________________\n" + captain, boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();
            openPDF(fileName);
            JOptionPane.showMessageDialog(this, "Indigency Certificate has been generated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            showErrorMessage("Error generating PDF: " + e.getMessage());
        }
    }

    private void openPDF(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                showErrorMessage("File not found: " + filePath);
            }
        } catch (IOException e) {
            showErrorMessage("Error opening PDF: " + e.getMessage());
        }
    }
    
    private void btnCertificateActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbResCert.getSelectedRow();
        if (selectedRow != -1) {
            int residentId = Integer.parseInt(tbResCert.getValueAt(selectedRow, 0).toString());

            String[] certificateOptions = {
                "Certificate of Indigency",
                "Barangay Clearance",
                "Business Permit",
                "Certificate of Residency"
            };

            String certificateType = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the type of certificate:",
                    "Certificate Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    certificateOptions,
                    certificateOptions[0]
            );

            if (certificateType != null) {
                String purpose = JOptionPane.showInputDialog(this, "Purpose:");
                if (purpose == null || purpose.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Purpose cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Insert request into the database
                String sql = "INSERT INTO certificate_requests (resident_id, certificate_type, purpose, request_date, status) VALUES (?, ?, ?, CURDATE(), 'Pending')";
                try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, residentId);
                    ps.setString(2, certificateType);
                    ps.setString(3, purpose);
                    ps.executeUpdate();

                    updateNotificationCount();
                    JOptionPane.showMessageDialog(this, "Certificate request submitted for approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    showErrorMessage("Database Error: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to request a certificate.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void loadCertificateRequests() {
        // Check if the logged-in user is a Barangay Captain
        if (!isUserBarangayCaptain()) {
            JOptionPane.showMessageDialog(this, "Access Denied! Only the Barangay Captain and Secretary can view this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create table model
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Resident ID", "Certificate Type", "Purpose", "Request Date", "Status"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new java.awt.Font("Poppins", java.awt.Font.PLAIN, 12));
        table.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(table);

        // Fetch pending requests from database
        String sql = "SELECT * FROM certificate_requests WHERE status = 'Pending'";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                int residentId = rs.getInt("resident_id");
                String certType = rs.getString("certificate_type");
                String purpose = rs.getString("purpose");
                String requestDate = rs.getString("request_date");
                String status = rs.getString("status");
                model.addRow(new Object[]{requestId, residentId, certType, purpose, requestDate, status});
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }

        // Buttons for approval and denial
        JButton btnApprove = new JButton("Approve");
        JButton btnDeny = new JButton("Deny");

        btnApprove.addActionListener(e -> processCertificateRequest(table, "Approved"));
        btnDeny.addActionListener(e -> processCertificateRequest(table, "Denied"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnApprove);
        buttonPanel.add(btnDeny);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Pending Certificate Requests", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processCertificateRequest(JTable table, String action) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int requestId = (int) table.getValueAt(selectedRow, 0);
            int residentId = (int) table.getValueAt(selectedRow, 1);
            String certificateType = (String) table.getValueAt(selectedRow, 2);
            String purpose = (String) table.getValueAt(selectedRow, 3);

            String sql = "UPDATE certificate_requests SET status = ? WHERE request_id = ?";
            try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, action);
                ps.setInt(2, requestId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Certificate request " + action.toLowerCase() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (action.equals("Approved")) {
                    generateCertificate(residentId, certificateType, purpose);
                }

                updateNotificationCount();
                SwingUtilities.getWindowAncestor(table).dispose();
            } catch (SQLException e) {
                showErrorMessage("Database Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a request to process.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateNotificationCount() {
        String sql = "SELECT COUNT(*) AS pending_count FROM certificate_requests WHERE status = 'Pending'";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                lblCount.setText(String.valueOf(rs.getInt("pending_count")));
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }
    
    private boolean isUserBarangayCaptain() {
        String sql = "SELECT position FROM officials WHERE resident_id = (SELECT resident_id FROM users WHERE user_id = ?)";
        try (Connection conn = DBConnection.Connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, getLoggedInUserID()); // Implement getCurrentUserId() to get the logged-in user
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("position").equalsIgnoreCase("Barangay Captain/Chairman") || rs.getString("position").equalsIgnoreCase("Barangay Secretary");
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
        return false;
    }
    
    private void generateCertificate(int residentId, String certificateType, String purpose) {
        switch (certificateType) {
            case "Certificate of Indigency":
                generateIndigencyCertificatePDF(residentId, purpose);
                break;
            case "Barangay Clearance":
                generateBarangayClearancePDF(residentId, purpose);
                break;
            case "Business Permit":
                generateBusinessPermitPDF(residentId, purpose);
                break;
            case "Certificate of Residency":
                generateCertificateOfResidencyPDF(residentId);
                break;
        }
    }
    
    private void btnCertificateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCertificateMouseClicked
        
    }//GEN-LAST:event_btnCertificateMouseClicked

    private void btnHealthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHealthMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHealthMouseClicked

    private void btnHealthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHealthActionPerformed
        int selectedRow = tbResCert.getSelectedRow();
        if (selectedRow != -1) {
            int residentId = Integer.parseInt(tbResCert.getValueAt(selectedRow, 0).toString());
            loadHealthRecords(residentId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to view health records.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnHealthActionPerformed

    private void btnOfficialsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOfficialsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOfficialsMouseClicked

    private void btnOfficialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOfficialsActionPerformed
        loadOfficials();
    }//GEN-LAST:event_btnOfficialsActionPerformed

    private void lblNotifsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNotifsMouseClicked
        loadCertificateRequests();
    }//GEN-LAST:event_lblNotifsMouseClicked

    private void lblCountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCountMouseClicked

    private void btnBlotterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBlotterMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBlotterMouseClicked

    private void btnBlotterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlotterActionPerformed
        int selectedRow = tbResCert.getSelectedRow();
        if (selectedRow != -1) {
            int residentId = Integer.parseInt(tbResCert.getValueAt(selectedRow, 0).toString());
            loadBlotterRecords(residentId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to view blotter records.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnBlotterActionPerformed

    private void deleteResident(int residentId) {
        String sql = "CALL DeleteResident(?)";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, residentId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Resident has been deleted.", "Delete", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }

    private void btnDeleteResActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbResidents.getSelectedRow();
        if (selectedRow != -1) {
            int residentId = Integer.parseInt(tbResidents.getValueAt(selectedRow, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Resident?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteResident(residentId);
                loadResidents("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an student to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void lblSearchMouseClicked(java.awt.event.MouseEvent evt) {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadResidents(searchTerm);
        txtSearch.setText("");
    }

    private void lblRefreshMouseClicked(java.awt.event.MouseEvent evt) {
        loadResidents("");
    }

    private void lblResPMouseClicked(java.awt.event.MouseEvent evt) {
        HomePage.setVisible(false);
        ResidentPage.setVisible(true);
        FamiliesPage.setVisible(false);
        HousesPage.setVisible(false);
        CertificatesPage.setVisible(false);

        lblHome.setForeground(DEFAULT_COLOR);
        lblHome.setIcon(homeActiveIcon);
        lblResP.setForeground(ACTIVE_COLOR);
        lblFamP.setForeground(DEFAULT_COLOR);
        lblHouseP.setForeground(DEFAULT_COLOR);
        lblCertificates.setForeground(DEFAULT_COLOR);
    }

    private void lblFamPMouseClicked(java.awt.event.MouseEvent evt) {
        ResidentPage.setVisible(false);
        HomePage.setVisible(false);
        FamiliesPage.setVisible(true);
        HousesPage.setVisible(false);
        CertificatesPage.setVisible(false);

        lblHome.setForeground(DEFAULT_COLOR);
        lblHome.setIcon(homeActiveIcon);
        lblResP.setForeground(DEFAULT_COLOR);
        lblFamP.setForeground(ACTIVE_COLOR);
        lblHouseP.setForeground(DEFAULT_COLOR);
        lblCertificates.setForeground(DEFAULT_COLOR);
    }

    private void lblHousePMouseClicked(java.awt.event.MouseEvent evt) {
        ResidentPage.setVisible(false);
        HomePage.setVisible(false);
        FamiliesPage.setVisible(false);
        HousesPage.setVisible(true);
        CertificatesPage.setVisible(false);

        lblHome.setForeground(DEFAULT_COLOR);
        lblHome.setIcon(homeActiveIcon);
        lblResP.setForeground(DEFAULT_COLOR);
        lblFamP.setForeground(DEFAULT_COLOR);
        lblHouseP.setForeground(ACTIVE_COLOR);
        lblCertificates.setForeground(DEFAULT_COLOR);
    }

    private void lblLogoutMouseClicked(java.awt.event.MouseEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to Log Out?", "Log Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Login login = Login.getInstance();
            login.setVisible(true);
            this.dispose();
        }
    }

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {
        ResidentPage.setVisible(false);
        HomePage.setVisible(true);
        FamiliesPage.setVisible(false);
        HousesPage.setVisible(false);
        CertificatesPage.setVisible(false);

        lblHome.setForeground(ACTIVE_COLOR);
        lblHome.setIcon(homeDefaultIcon);
        lblResP.setForeground(DEFAULT_COLOR);
        lblFamP.setForeground(DEFAULT_COLOR);
        lblHouseP.setForeground(DEFAULT_COLOR);
        lblCertificates.setForeground(DEFAULT_COLOR);
    }

    private void lblSearch1MouseClicked(java.awt.event.MouseEvent evt) {
        String searchTerm = txtSearch1.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadFamilies(searchTerm);
        txtSearch1.setText("");
    }

    private void lblRefresh1MouseClicked(java.awt.event.MouseEvent evt) {
        loadFamilies("");
    }

    private void deleteFamily(int familyId) {
        String sql = "CALL DeleteFamily(?)";
        try (Connection conn = DBConnection.Connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, familyId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Family has been deleted.", "Delete", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showErrorMessage("Database Error: " + e.getMessage());
        }
    }

    private void btnDeleteFamActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbFamilies.getSelectedRow();
        if (selectedRow != -1) {
            int familyId = Integer.parseInt(tbFamilies.getValueAt(selectedRow, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Family?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteFamily(familyId);
                loadFamilies("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an student to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnAddFamActionPerformed(java.awt.event.ActionEvent evt) {
        AddFam addFam = new AddFam(this);
        addFam.setVisible(true);
    }

    private void lblSearch2MouseClicked(java.awt.event.MouseEvent evt) {
        String searchTerm = txtSearch2.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadHouses(searchTerm);
        txtSearch2.setText("");
    }

    private void lblRefresh2MouseClicked(java.awt.event.MouseEvent evt) {
        loadHouses("");
    }

    private void btnDeleteHouseActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbHouses.getSelectedRow();
        if (selectedRow != -1) {
            int familyId = Integer.parseInt(tbHouses.getValueAt(selectedRow, 0).toString());
            int familyNo = Integer.parseInt(tbHouses.getValueAt(selectedRow, 3).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this House?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (familyNo > 0) {
                    JOptionPane.showMessageDialog(this, "There should be no families in this house to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                deleteFamily(familyId);
                loadFamilies("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an student to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnAddHouseActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void lblCertificatesMouseClicked(java.awt.event.MouseEvent evt) {
        ResidentPage.setVisible(false);
        HomePage.setVisible(false);
        FamiliesPage.setVisible(false);
        HousesPage.setVisible(false);
        CertificatesPage.setVisible(true);

        lblHome.setForeground(DEFAULT_COLOR);
        lblHome.setIcon(homeActiveIcon);
        lblResP.setForeground(DEFAULT_COLOR);
        lblFamP.setForeground(DEFAULT_COLOR);
        lblHouseP.setForeground(DEFAULT_COLOR);
        lblCertificates.setForeground(ACTIVE_COLOR);
    }

    private void lblSearch3MouseClicked(java.awt.event.MouseEvent evt) {
        String searchTerm = txtSearch3.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadResCert(searchTerm);
        txtSearch3.setText("");
    }

    private void lblRefresh3MouseClicked(java.awt.event.MouseEvent evt) {
        loadResCert("");
    }

    private void btnDeleteHouse1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnAddHouse1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CertificatesPage;
    private javax.swing.JLayeredPane ContentPane;
    private javax.swing.JPanel FamiliesPage;
    private javax.swing.JPanel HomePage;
    private javax.swing.JPanel HousesPage;
    private javax.swing.JPanel NavPane;
    private javax.swing.JPanel ResidentPage;
    private javax.swing.JButton btnAddFam;
    private javax.swing.JButton btnAddHouse;
    private javax.swing.JButton btnBlotter;
    private javax.swing.JButton btnCertificate;
    private javax.swing.JButton btnDeleteFam;
    private javax.swing.JButton btnDeleteHouse;
    private javax.swing.JButton btnDeleteRes;
    private javax.swing.JButton btnHealth;
    private javax.swing.JButton btnOfficials;
    private javax.swing.JButton btnViewFam;
    private javax.swing.JButton btnViewHouse;
    private javax.swing.JButton btnViewRes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblCertificates;
    private javax.swing.JLabel lblCount;
    private javax.swing.JLabel lblFamP;
    private javax.swing.JLabel lblFamilies;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblHouseP;
    private javax.swing.JLabel lblHouses;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblNotifs;
    private javax.swing.JLabel lblOfficials;
    private javax.swing.JLabel lblRefresh;
    private javax.swing.JLabel lblRefresh1;
    private javax.swing.JLabel lblRefresh2;
    private javax.swing.JLabel lblRefresh3;
    private javax.swing.JLabel lblResP;
    private javax.swing.JLabel lblResidents;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearch1;
    private javax.swing.JLabel lblSearch2;
    private javax.swing.JLabel lblSearch3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTable tbFamilies;
    private javax.swing.JTable tbHouses;
    private javax.swing.JTable tbResCert;
    private javax.swing.JTable tbResidents;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearch1;
    private javax.swing.JTextField txtSearch2;
    private javax.swing.JTextField txtSearch3;
    // End of variables declaration//GEN-END:variables

}
