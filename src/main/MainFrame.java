/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package main;


import javax.swing.JOptionPane;
import strt.Login;
import Database.DBConnection;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        btnDeleteHouse1 = new javax.swing.JButton();
        btnViewHouse1 = new javax.swing.JButton();
        btnAddHouse1 = new javax.swing.JButton();
        NavPane = new javax.swing.JPanel();
        lblHome = new javax.swing.JLabel();
        lblResP = new javax.swing.JLabel();
        lblFamP = new javax.swing.JLabel();
        lblHouseP = new javax.swing.JLabel();
        lblLogout = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblCertificates = new javax.swing.JLabel();

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
                .addContainerGap(22, Short.MAX_VALUE))
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "House Number", "Street", "Families"
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
            tbResCert.getColumnModel().getColumn(3).setMinWidth(80);
            tbResCert.getColumnModel().getColumn(3).setMaxWidth(80);
        }

        btnDeleteHouse1.setBackground(new java.awt.Color(255, 153, 153));
        btnDeleteHouse1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDeleteHouse1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-minus-regular-24.png"))); // NOI18N
        btnDeleteHouse1.setText("Delete");
        btnDeleteHouse1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteHouse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteHouse1ActionPerformed(evt);
            }
        });

        btnViewHouse1.setBackground(new java.awt.Color(153, 153, 255));
        btnViewHouse1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnViewHouse1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/show-regular-24.png"))); // NOI18N
        btnViewHouse1.setText("View");
        btnViewHouse1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAddHouse1.setBackground(new java.awt.Color(153, 255, 153));
        btnAddHouse1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnAddHouse1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user-plus-regular-24.png"))); // NOI18N
        btnAddHouse1.setText("Add");
        btnAddHouse1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddHouse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHouse1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CertificatesPageLayout = new javax.swing.GroupLayout(CertificatesPage);
        CertificatesPage.setLayout(CertificatesPageLayout);
        CertificatesPageLayout.setHorizontalGroup(
            CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CertificatesPageLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(btnAddHouse1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewHouse1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteHouse1)
                        .addGap(18, 18, 18)
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
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16))
                    .addGroup(CertificatesPageLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CertificatesPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteHouse1)
                            .addComponent(btnViewHouse1)
                            .addComponent(btnAddHouse1)))
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
        lblCertificates.setText("Certificates");
        lblCertificates.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCertificates.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCertificatesMouseClicked(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogout)
                .addGap(32, 32, 32))
            .addComponent(jSeparator2)
        );
        NavPaneLayout.setVerticalGroup(
            NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavPaneLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(NavPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHome)
                    .addComponent(lblResP)
                    .addComponent(lblFamP)
                    .addComponent(lblHouseP)
                    .addComponent(lblLogout)
                    .addComponent(lblCertificates))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
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
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 620, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JButton btnAddHouse1;
    private javax.swing.JButton btnDeleteFam;
    private javax.swing.JButton btnDeleteHouse;
    private javax.swing.JButton btnDeleteHouse1;
    private javax.swing.JButton btnDeleteRes;
    private javax.swing.JButton btnViewFam;
    private javax.swing.JButton btnViewHouse;
    private javax.swing.JButton btnViewHouse1;
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
    private javax.swing.JLabel lblFamP;
    private javax.swing.JLabel lblFamilies;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblHouseP;
    private javax.swing.JLabel lblHouses;
    private javax.swing.JLabel lblLogout;
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
