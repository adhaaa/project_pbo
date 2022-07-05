package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class DetailInputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JPanel mainPanel;
    private JComboBox bukuComboBox;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public DetailInputFrame(){
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Lengkapi Data Nama Pengarang",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) bukuComboBox.getSelectedItem();
            int bukuId = item.getValue();
            if (bukuId == 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih Buku",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                bukuComboBox.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (this.id == 0) {
                    String cekSQL = "SELECT * FROM detail WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama  Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO detail SET nama = ?, buku_id = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2,bukuId);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM detail WHERE nama=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Nama  Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE detail SET nama=?, buku_id = ? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2,bukuId);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        init();
    }
    public void init(){
        setTitle("Input Detail");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }
    public void isiKomponen() {
        idTextField.setText(String.valueOf(this.id));

        String findSQL = "SELECT * FROM nama WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
                int bukuId = rs.getInt("buku_id");
                for (int i = 0; i <bukuComboBox.getItemCount() ; i++) {
                    bukuComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) bukuComboBox.getSelectedItem();
                    if (bukuId == item.getValue()){
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kustomisasiKomponen(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM buku ORDER BY nama";

        try{
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            bukuComboBox.addItem(new ComboBoxItem(0, "Pilih Buku"));
            while (rs.next()){
                bukuComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
