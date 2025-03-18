/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cursos.avion;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

/**
 *
 * @author d4n13l
 */
public class Asientos extends javax.swing.JFrame {
    
    private static final String URL = "jdbc:mysql://localhost:3306/aerolinea";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private final int idVuelo;
    private JButton[] botonesAsientos;
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
  

    public Asientos(int idVuelo) {
        setLocationRelativeTo(null);
        this.idVuelo = idVuelo;
        initComponents();
        inicializarBotones();
        cargarEstadoAsientos();
        funcionConIdVuelo();
    }

    // Función que usa el id_vuelo (puedes personalizarla según tus necesidades)
    private void funcionConIdVuelo() {
        JOptionPane.showMessageDialog(this, "ID del vuelo seleccionado: " + idVuelo);
    }

    private void inicializarBotones() {
        botonesAsientos = new JButton[] {
            jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8, jButton9, jButton10,
            jButton11, jButton12, jButton13, jButton14, jButton15, jButton16, jButton17, jButton18, jButton19, jButton20,
            jButton21, jButton22, jButton23, jButton24, jButton25, jButton26, jButton27, jButton28, jButton29, jButton30,
            jButton31, jButton32, jButton33, jButton34, jButton35, jButton36, jButton37, jButton38, jButton39, jButton40,
            jButton41, jButton42, jButton43, jButton44, jButton45, jButton46, jButton47, jButton48, jButton49, jButton50
        };
        
        // Agregar ActionListener a cada botón
        for (int i = 0; i < botonesAsientos.length; i++) {
            final int asientoId = i + 1; // Índice del asiento (1 a 50)
            botonesAsientos[i].addActionListener(evt -> manejarReservaAsiento(asientoId));
        }
    }
    
    private void manejarReservaAsiento(int idAsiento) {
        String sqlCheck = "SELECT estado FROM asientos_disponibles WHERE id_vuelo = ? AND id_asiento = ?";
        String sqlUpdate = "UPDATE asientos_disponibles SET estado = 'reservado' WHERE id_vuelo = ? AND id_asiento = ?";
        String sqlVuelo = "SELECT origen, destino, fecha, hora, precio FROM vuelos WHERE id_vuelo = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement pstmtVuelo = conn.prepareStatement(sqlVuelo)) {

            // Verificar el estado actual del asiento
            pstmtCheck.setInt(1, idVuelo);
            pstmtCheck.setInt(2, idAsiento);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                String estado = rs.getString("estado");
                if ("reservado".equalsIgnoreCase(estado)) {
                    JOptionPane.showMessageDialog(this, "El asiento " + idAsiento + " ya está reservado.", "Reserva no disponible", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Si está disponible, actualizar el estado
                pstmtUpdate.setInt(1, idVuelo);
                pstmtUpdate.setInt(2, idAsiento);
                int filasAfectadas = pstmtUpdate.executeUpdate();

                if (filasAfectadas > 0) {
                    // Cambiar el color del botón a rojo y deshabilitarlo
                    JButton boton = botonesAsientos[idAsiento - 1];
                    boton.setBackground(Color.RED);
                    boton.setEnabled(false);

                    // Obtener datos del vuelo para la factura
                    pstmtVuelo.setInt(1, idVuelo);
                    ResultSet rsVuelo = pstmtVuelo.executeQuery();
                    if (rsVuelo.next()) {
                        String origen = rsVuelo.getString("origen");
                        String destino = rsVuelo.getString("destino");
                        String fecha = rsVuelo.getString("fecha");
                        String hora = rsVuelo.getString("hora");
                        double precio = rsVuelo.getDouble("precio");

                        // Generar mensaje de factura
                        String factura = String.format(
                            "=== Factura de Reserva ===\n" +
                            "ID Vuelo: %d\n" +
                            "Asiento: %d\n" +
                            "Origen: %s\n" +
                            "Destino: %s\n" +
                            "Fecha: %s\n" +
                            "Hora: %s\n" +
                            "Precio: %.2f USD\n" +
                            "======================",
                            idVuelo, idAsiento, origen, destino, fecha, hora, precio
                        );
                        JOptionPane.showMessageDialog(this, factura, "Factura de Reserva", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo reservar el asiento " + idAsiento + ".", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEstadoAsientos() {
        String sql = "SELECT id_asiento, estado FROM asientos_disponibles WHERE id_vuelo = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idVuelo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int idAsiento = rs.getInt("id_asiento");
                String estado = rs.getString("estado");

                if (idAsiento >= 1 && idAsiento <= 50) {
                    JButton boton = botonesAsientos[idAsiento - 1];
                    if ("reservado".equalsIgnoreCase(estado)) {
                        boton.setBackground(Color.RED);
                        boton.setEnabled(false);
                    } else {
                        boton.setBackground(Color.YELLOW);
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el estado de los asientos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(820, 426));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setText("1");
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 30, 30));

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setText("2");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 30, 30));

        jButton3.setBackground(new java.awt.Color(255, 255, 153));
        jButton3.setText("3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 30, 30));

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setText("4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 30, 30));

        jButton5.setBackground(new java.awt.Color(255, 255, 153));
        jButton5.setText("5");
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 30, 30));

        jButton6.setBackground(new java.awt.Color(255, 255, 153));
        jButton6.setText("6");
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 30, 30));

        jButton7.setBackground(new java.awt.Color(255, 255, 153));
        jButton7.setText("7");
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 30, 30));

        jButton8.setBackground(new java.awt.Color(255, 255, 153));
        jButton8.setText("8");
        getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 183, 30, 30));

        jButton9.setBackground(new java.awt.Color(0, 153, 153));
        jButton9.setForeground(new java.awt.Color(204, 204, 204));
        jButton9.setText("9");
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 300, 50, -1));

        jButton10.setBackground(new java.awt.Color(0, 153, 153));
        jButton10.setText("10");
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 50, -1));

        jButton11.setBackground(new java.awt.Color(0, 153, 153));
        jButton11.setText("11");
        getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 50, -1));

        jButton12.setBackground(new java.awt.Color(0, 153, 153));
        jButton12.setText("12");
        getContentPane().add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 50, -1));

        jButton13.setBackground(new java.awt.Color(0, 153, 153));
        jButton13.setText("13");
        getContentPane().add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 50, -1));

        jButton14.setBackground(new java.awt.Color(0, 153, 153));
        jButton14.setText("14");
        getContentPane().add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 50, -1));

        jButton15.setBackground(new java.awt.Color(0, 153, 153));
        jButton15.setText("15");
        getContentPane().add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 50, -1));

        jButton16.setBackground(new java.awt.Color(0, 153, 153));
        jButton16.setText("16");
        getContentPane().add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 50, -1));

        jButton17.setBackground(new java.awt.Color(0, 153, 153));
        jButton17.setText("17");
        getContentPane().add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 50, -1));

        jButton18.setBackground(new java.awt.Color(0, 153, 153));
        jButton18.setText("18");
        getContentPane().add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 50, -1));

        jButton19.setBackground(new java.awt.Color(0, 153, 153));
        jButton19.setText("19");
        getContentPane().add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 50, -1));

        jButton20.setBackground(new java.awt.Color(0, 153, 153));
        jButton20.setText("20");
        getContentPane().add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 50, -1));

        jButton21.setBackground(new java.awt.Color(0, 153, 153));
        jButton21.setText("21");
        getContentPane().add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 50, -1));

        jButton22.setBackground(new java.awt.Color(0, 153, 153));
        jButton22.setText("22");
        getContentPane().add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 50, -1));

        jButton23.setBackground(new java.awt.Color(0, 153, 153));
        jButton23.setText("23");
        getContentPane().add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 50, -1));

        jButton24.setBackground(new java.awt.Color(0, 153, 153));
        jButton24.setText("24");
        getContentPane().add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 210, 50, -1));

        jButton25.setBackground(new java.awt.Color(0, 153, 153));
        jButton25.setText("25");
        getContentPane().add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 50, -1));

        jButton26.setBackground(new java.awt.Color(0, 153, 153));
        jButton26.setText("26");
        getContentPane().add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 50, -1));

        jButton27.setBackground(new java.awt.Color(0, 153, 153));
        jButton27.setText("27");
        getContentPane().add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 50, -1));

        jButton28.setBackground(new java.awt.Color(0, 153, 153));
        jButton28.setText("28");
        getContentPane().add(jButton28, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 50, -1));

        jButton29.setBackground(new java.awt.Color(0, 153, 153));
        jButton29.setText("29");
        getContentPane().add(jButton29, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, 50, -1));

        jButton30.setBackground(new java.awt.Color(0, 153, 153));
        jButton30.setText("30");
        getContentPane().add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 50, -1));

        jButton31.setBackground(new java.awt.Color(0, 153, 153));
        jButton31.setText("31");
        getContentPane().add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 50, -1));

        jButton32.setBackground(new java.awt.Color(0, 153, 153));
        jButton32.setText("32");
        getContentPane().add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 50, -1));

        jButton33.setBackground(new java.awt.Color(0, 153, 153));
        jButton33.setText("33");
        getContentPane().add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, 50, -1));

        jButton34.setBackground(new java.awt.Color(0, 153, 153));
        jButton34.setText("34");
        getContentPane().add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, 50, -1));

        jButton35.setBackground(new java.awt.Color(0, 153, 153));
        jButton35.setText("35");
        getContentPane().add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 260, 50, -1));

        jButton36.setBackground(new java.awt.Color(0, 153, 153));
        jButton36.setText("36");
        getContentPane().add(jButton36, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 50, -1));

        jButton37.setBackground(new java.awt.Color(0, 153, 153));
        jButton37.setText("37");
        getContentPane().add(jButton37, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 50, -1));

        jButton38.setBackground(new java.awt.Color(0, 153, 153));
        jButton38.setText("38");
        getContentPane().add(jButton38, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 50, -1));

        jButton39.setBackground(new java.awt.Color(0, 153, 153));
        jButton39.setText("39");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton39, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 50, -1));

        jButton40.setBackground(new java.awt.Color(0, 153, 153));
        jButton40.setText("40");
        getContentPane().add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 280, 50, -1));

        jButton41.setBackground(new java.awt.Color(0, 153, 153));
        jButton41.setText("41");
        getContentPane().add(jButton41, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 260, 50, -1));

        jButton42.setBackground(new java.awt.Color(0, 153, 153));
        jButton42.setText("42");
        getContentPane().add(jButton42, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, 50, -1));

        jButton43.setBackground(new java.awt.Color(0, 153, 153));
        jButton43.setText("43");
        getContentPane().add(jButton43, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 50, -1));

        jButton44.setBackground(new java.awt.Color(0, 153, 153));
        jButton44.setText("44");
        getContentPane().add(jButton44, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 50, -1));

        jButton45.setBackground(new java.awt.Color(0, 153, 153));
        jButton45.setText("45");
        getContentPane().add(jButton45, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 300, 50, -1));

        jButton46.setBackground(new java.awt.Color(0, 153, 153));
        jButton46.setText("46");
        getContentPane().add(jButton46, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 280, 50, -1));

        jButton47.setBackground(new java.awt.Color(0, 153, 153));
        jButton47.setText("47");
        getContentPane().add(jButton47, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 260, 50, -1));

        jButton48.setBackground(new java.awt.Color(0, 153, 153));
        jButton48.setText("48");
        getContentPane().add(jButton48, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, 50, -1));

        jButton49.setBackground(new java.awt.Color(0, 153, 153));
        jButton49.setText("49");
        getContentPane().add(jButton49, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 190, 50, -1));

        jButton50.setBackground(new java.awt.Color(0, 153, 153));
        jButton50.setText("50");
        getContentPane().add(jButton50, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 170, 50, -1));

        jButton51.setBackground(new java.awt.Color(255, 255, 255));
        jButton51.setForeground(new java.awt.Color(218, 41, 28));
        jButton51.setText("Volver");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton51, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 230, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(218, 41, 28));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Seleccione un asiento vacio");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 230, 90));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cursos/avion/asientos_1.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 506));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        new VuelosInterfaz().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton51ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Asientos(1).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
