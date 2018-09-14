package tickettimertacker;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class TicketTimeTracker extends javax.swing.JFrame {
    final static File PROPERTIES_DIR = new File(System.getProperty("user.home") + File.separator + "TicketTimeStamps");
    final static File PROPERTIES_FILE = new File(PROPERTIES_DIR + File.separator + "timestamp.properties");
    final static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    
    static boolean running = false;
    static File timeStampFile;
    static Properties prop = new Properties();
    static ImageIcon stopped;
    static ImageIcon started;
    static TicketTimeTracker mainFrame;
    static LocalDateTime startTime;
    
    public TicketTimeTracker() {
        initComponents();
        try {
            // Catch-em-all code try-catch for code to run at launch
            startUpCode();
        } catch (IOException ex) {
            Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void startUpCode() throws FileNotFoundException, IOException {
        // Startup code
        stopped = new ImageIcon(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur_stopped32x32.png")));
        started = new ImageIcon(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur32x32.png")));
        
        // Create folder if it does not exist
        if(!PROPERTIES_DIR.exists()) {
            PROPERTIES_DIR.mkdir();
        }
        
        // Create file if it does not exist
        if(!PROPERTIES_FILE.exists()) {
            PROPERTIES_FILE.createNewFile();
            prop.setProperty("path", PROPERTIES_DIR.getPath());
            prop.setProperty("activeTicket", "No active ticket.");
            prop.setProperty("ticketCount", "0");
            prop.store(new FileOutputStream(PROPERTIES_FILE), null);
        } else {
            prop.load(new FileInputStream(PROPERTIES_FILE));
        }
        
        // Properties file
        timeStampFile = new File(prop.getProperty("path") + File.separator + "TimeStamps.txt");
        // Create file if it does note exists
        if(!timeStampFile.exists()) {
            timeStampFile.createNewFile();
        }
        
        // Check active ticket
        if(prop.getProperty("activeTicket") != null) {
            String ticketName = prop.getProperty("activeTicket");
            activeTicket.setText(ticketName);
            
            // Get active text and see if it is a ticket
            if(prop.getProperty(activeTicket.getText()) != null) {
                totalTimeLabel.setText("Total time: " + prop.getProperty(activeTicket.getText() + "_time") + " min");
            }
        }
        // Get tickets
        if(prop.getProperty("ticketCount") != null) {
            int ticketCount = Integer.parseInt(prop.getProperty("ticketCount"));
            if(ticketCount > 0) {
                for(int i = 1; i <= ticketCount; i++) {
                    String ticketName = prop.getProperty("ticket" + i);
                    addTicketMenuItems(ticketName);
                }
            }
        }
    }
    
    void addTicketMenuItems(String ticketName) throws IOException {
        // Create loadTicket items with icon
        JMenuItem loadedTicket = new JMenuItem(ticketName, new ImageIcon(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/loadticket.png"))));
        // Load clicked ticket
        loadedTicket.addActionListener((ActionEvent ae) -> {
            totalTimeLabel.setText("Total time: " + prop.getProperty(ticketName+"_time"));
            activeTicket.setText(ticketName);
            prop.setProperty("activeTicket", ticketName);
            updateProperties();
        });
        
        LoadTicketMenu.add(loadedTicket);
        
        // Create removeTicket items with icon
        JMenuItem removeTicket = new JMenuItem(ticketName, new ImageIcon(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/ticket.png"))));
        // Remove clicked ticket
        removeTicket.addActionListener((ActionEvent ae) -> {
            // Get total ticket count
            int totalTicketCount = Integer.parseInt(prop.getProperty("ticketCount"));
            // Get current ticket count number
            int currentTicketNumber = Integer.parseInt((prop.getProperty(ticketName)).substring(6));
            
            // Check current active ticket
            if(activeTicket.getText().equals(prop.getProperty("ticket" + currentTicketNumber))) {
                activeTicket.setText("No active ticket.");
                prop.setProperty("activeTicket", "No active ticket.");
                labelLED.setIcon(stopped);
            }
            
            // Remove ticketName and ticketNumber
            prop.remove(ticketName);
            prop.remove(ticketName+"_time");
            prop.remove("ticket"+currentTicketNumber);
            // Reduce ticketCount
            prop.setProperty("ticketCount", String.valueOf(totalTicketCount - 1));
            
            // Check if it was last ticket
            if((totalTicketCount - 1) == 0) {
                prop.setProperty("activeTicket", "No active ticket.");
                
            // Reorder tickets
            } else if(currentTicketNumber < totalTicketCount) {
                for(int i = currentTicketNumber+1; i <= totalTicketCount; i++) {
                    String tempTicketNr = "ticket" + i;
                    String tempTicketName = prop.getProperty(tempTicketNr);
                    
                    prop.setProperty("ticket" + (i - 1), tempTicketName);
                    prop.setProperty(tempTicketName, "ticket" + (i - 1));
                }
                
                prop.remove("ticket" + totalTicketCount);
            }
            
            // Update properties file
            updateProperties();
            
            // Remove ticket from sub menus
            RemoveTicketMenu.remove(removeTicket);
            LoadTicketMenu.remove(loadedTicket);
            // End action event
        });
        
        RemoveTicketMenu.add(removeTicket);
    }
    
    // Update properties file on computer
    static void updateProperties(){
        try {
            prop.store(new FileOutputStream(PROPERTIES_FILE), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
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

        panel = new javax.swing.JPanel();
        activeTicket = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        endButton = new javax.swing.JButton();
        totalTimeLabel = new javax.swing.JLabel();
        labelLED = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        ExitMenuItem = new javax.swing.JMenuItem();
        AlwaysOnTopMenuItem = new javax.swing.JCheckBoxMenuItem();
        TicketMainMenu = new javax.swing.JMenu();
        NewTicketMenuItem = new javax.swing.JMenuItem();
        LoadTicketMenu = new javax.swing.JMenu();
        RemoveTicketMenu = new javax.swing.JMenu();
        OpenMenu = new javax.swing.JMenu();
        TimestampsMenuItem = new javax.swing.JMenuItem();
        FolderMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(205, 101));
        setResizable(false);

        activeTicket.setText("No active ticket.");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        endButton.setText("End");
        endButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endButtonActionPerformed(evt);
            }
        });

        totalTimeLabel.setText("Total time:");

        labelLED.setForeground(new java.awt.Color(240, 240, 240));
        labelLED.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/stoppur_stopped32x32.png"))); // NOI18N

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(activeTicket)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(startButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(totalTimeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(labelLED)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelLED)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(activeTicket)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalTimeLabel)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startButton)
                            .addComponent(endButton))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FileMenu.setText("File");
        FileMenu.setToolTipText("");

        ExitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        ExitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit.png"))); // NOI18N
        ExitMenuItem.setText("Exit");
        ExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(ExitMenuItem);

        AlwaysOnTopMenuItem.setSelected(true);
        AlwaysOnTopMenuItem.setText("Always on top");
        AlwaysOnTopMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlwaysOnTopMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(AlwaysOnTopMenuItem);

        jMenuBar1.add(FileMenu);

        TicketMainMenu.setText("Tickets");
        TicketMainMenu.setToolTipText("");

        NewTicketMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        NewTicketMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/newticket.png"))); // NOI18N
        NewTicketMenuItem.setText("New Ticket");
        NewTicketMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewTicketMenuItemActionPerformed(evt);
            }
        });
        TicketMainMenu.add(NewTicketMenuItem);

        LoadTicketMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/loadticket.png"))); // NOI18N
        LoadTicketMenu.setText("Load tickets");
        TicketMainMenu.add(LoadTicketMenu);

        RemoveTicketMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/removeTicket.png"))); // NOI18N
        RemoveTicketMenu.setText("Remove ticket");
        TicketMainMenu.add(RemoveTicketMenu);

        jMenuBar1.add(TicketMainMenu);

        OpenMenu.setText("Open");

        TimestampsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/file.png"))); // NOI18N
        TimestampsMenuItem.setText("Timestaps");
        TimestampsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimestampsMenuItemActionPerformed(evt);
            }
        });
        OpenMenu.add(TimestampsMenuItem);

        FolderMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/folder.png"))); // NOI18N
        FolderMenuItem.setText("Folder");
        FolderMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FolderMenuItemActionPerformed(evt);
            }
        });
        OpenMenu.add(FolderMenuItem);

        jMenuBar1.add(OpenMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_ExitMenuItemActionPerformed

    private void NewTicketMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewTicketMenuItemActionPerformed
        int ticketCount = Integer.parseInt(prop.getProperty("ticketCount"));
        String ticketName = JOptionPane.showInputDialog("Enter ticker number:");
        if(ticketName != null) {
            // Increase total count
            prop.setProperty("ticketCount", String.valueOf(++ticketCount));
            // ticket# = ticketName
            prop.setProperty("ticket" + ticketCount, ticketName);
            // ticketName = ticket#
            prop.setProperty(ticketName, "ticket" + ticketCount);
            // ticketName_time = 0
            prop.setProperty(ticketName +"_time", "0");
            // active ticket = ticket name
            prop.setProperty("activeTicket", ticketName);
            updateProperties();

            // Update texts
            activeTicket.setText(ticketName);
            totalTimeLabel.setText("Total Time: 0");

            try {
                addTicketMenuItems(ticketName);
            } catch (IOException ex) {
                Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_NewTicketMenuItemActionPerformed

    private void TimestampsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimestampsMenuItemActionPerformed
        try {
            Desktop.getDesktop().open(timeStampFile);
        } catch (IOException ex) {
            Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TimestampsMenuItemActionPerformed

    private void FolderMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FolderMenuItemActionPerformed
        try {
            Desktop.getDesktop().open(PROPERTIES_DIR);
        } catch (IOException ex) {
            Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_FolderMenuItemActionPerformed

    private void endButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endButtonActionPerformed
        if(running) {
            labelLED.setIcon(stopped);
            running = false;
            labelLED.setForeground(new Color(222,64,64));
            try {
                // Timestamp
                LocalDateTime endTime = LocalDateTime.now();                
                byte[] strBytes = (endTime.format(DATE_TIME_FORMAT)+ System.lineSeparator()).getBytes();
                Files.write(timeStampFile.toPath(), strBytes, StandardOpenOption.APPEND);
                // --- Timestamp

                // Duration
                long duration = Duration.between(startTime, endTime).toMinutes();
                if(prop.getProperty(activeTicket.getText() + "_time") != null) {
                    duration += Long.parseLong(prop.getProperty(activeTicket.getText() + "_time"));
                }

                String durationStr = String.valueOf(duration);
                strBytes = durationStr.getBytes();
                Files.write(timeStampFile.toPath(), strBytes, StandardOpenOption.APPEND);

                prop.setProperty(activeTicket.getText() + "_time", durationStr);
                updateProperties();

                totalTimeLabel.setText("Total time: " + durationStr + " min");
                // --- Duration

                // New line
                strBytes = System.lineSeparator().getBytes();
                Files.write(timeStampFile.toPath(), strBytes, StandardOpenOption.APPEND);
                // --- New line
            } catch (IOException ex) {
                Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_endButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if(!activeTicket.getText().equals("No active ticket.")) {
            if(!running) {
                labelLED.setIcon(started);
                running = true;

                labelLED.setForeground(new Color(79,219,69));

                try {
                    // Ticket stamp
                    byte[] strBytes = (activeTicket.getText() + System.lineSeparator()).getBytes();
                    Files.write(timeStampFile.toPath(), strBytes, StandardOpenOption.APPEND);
                    
                    // Start time
                    startTime = LocalDateTime.now();
                    strBytes = (startTime.format(DATE_TIME_FORMAT) + System.lineSeparator()).getBytes();
                    Files.write(timeStampFile.toPath(), strBytes, StandardOpenOption.APPEND);
                } catch (IOException ex) {
                    Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            NewTicketMenuItem.doClick();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void AlwaysOnTopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlwaysOnTopMenuItemActionPerformed
        if(AlwaysOnTopMenuItem.isSelected()) {
            mainFrame.setAlwaysOnTop(true);
        } else {
            mainFrame.setAlwaysOnTop(false);
        }
    }//GEN-LAST:event_AlwaysOnTopMenuItemActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketTimeTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            mainFrame = new TicketTimeTracker();
            try {
                List<Image> icons = new ArrayList<>();
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur16x16.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur24x24.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur32x32.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur48x48.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur64x64.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur96x96.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur128x128.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur256x256.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur512x512.png")));
                icons.add(ImageIO.read(TicketTimeTracker.class.getResourceAsStream("/Icons/stoppur1024x1024.png")));
                
                mainFrame.setIconImages(icons);
            } catch (IOException ex) {
                Logger.getLogger(TicketTimeTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            mainFrame.setVisible(true);
            mainFrame.setAlwaysOnTop(true);
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem AlwaysOnTopMenuItem;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem FolderMenuItem;
    private javax.swing.JMenu LoadTicketMenu;
    private javax.swing.JMenuItem NewTicketMenuItem;
    private javax.swing.JMenu OpenMenu;
    private javax.swing.JMenu RemoveTicketMenu;
    private javax.swing.JMenu TicketMainMenu;
    private javax.swing.JMenuItem TimestampsMenuItem;
    private javax.swing.JLabel activeTicket;
    private javax.swing.JButton endButton;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel labelLED;
    private javax.swing.JPanel panel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel totalTimeLabel;
    // End of variables declaration//GEN-END:variables
}
