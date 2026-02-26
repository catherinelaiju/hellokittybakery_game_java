import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class HelloKittyBakery{
    public static void main(String[]args){
        JFrame frame= new JFrame("Hello Kitty Bakery");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //background
     ImageIcon bgIcon = new ImageIcon("images/bg.png");

     JLabel background=new JLabel(bgIcon);
     background.setLayout(new GridBagLayout());
    
    //start button
    JButton startbutton = new JButton("Start Game");
    startbutton.setFont(new Font("Arial",Font.BOLD,18));
    //button action start
    startbutton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            background.remove(startbutton);//after clicking on start we remove strat button
            background.setLayout(null);//use manual layout
            //bg bluring 
            JPanel dim = new JPanel();//to make a dark transparent overlay
            dim.setBounds(0, 0, 800, 600);
            dim.setBackground(new Color(0, 0, 0, 80)); // dark glass effect
            dim.setOpaque(true);
            background.add(dim);
            //seller hellokitty
            ImageIcon kittyicon=new ImageIcon("images/hellokitty.png");
            
            JLabel kitty=new JLabel(kittyicon);
            int kw=kittyicon.getIconWidth();// to get actual width of image uploaded now image size will be equal to image uploaded
            int kh=kittyicon.getIconHeight();
            kitty.setBounds(10,230,kw,kh);
            background.add(kitty);
            

            //cabinet
            ImageIcon cabineticon=new ImageIcon("images/cabinet.png");
            JLabel cabinet=new JLabel(cabineticon);
            cabinet.setBounds(180, 170, 440, 360);
cabinet.setLayout(null);
            background.add(cabinet);
            
             // ===== ITEMS (SCALED) =====
            JButton cupcakeBtn = createItemButton("images/cupcake.png", 70, 60);
            cupcakeBtn.setBounds(60, 60, 80, 80);
            cabinet.add(cupcakeBtn);

            JButton toastBtn = createItemButton("images/toast.png", 70, 60);
            toastBtn.setBounds(190, 60, 80, 80);
            cabinet.add(toastBtn);

            JButton pastryBtn = createItemButton("images/pastry.png", 70, 60);
            pastryBtn.setBounds(320, 60, 80, 80);
            cabinet.add(pastryBtn);

            // ===== CLICK ACTIONS =====
            cupcakeBtn.addActionListener(ev ->
                    JOptionPane.showMessageDialog(frame, "Cupcake added to cart"));

            toastBtn.addActionListener(ev ->
                    JOptionPane.showMessageDialog(frame, "French Toast added to cart"));

            pastryBtn.addActionListener(ev ->
                    JOptionPane.showMessageDialog(frame, "Pastry added to cart"));

            background.revalidate();
            background.repaint();
     } });
     background.add(startbutton);
        frame.setContentPane(background);
        frame.setLocationRelativeTo(null); // center screen (optional but nice)
        frame.setVisible(true);
    }

    // ===== HELPER FOR IMAGE BUTTON =====
    static JButton createItemButton(String path, int w, int h) {
        ImageIcon raw = new ImageIcon(path);
        Image img = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(img));
        btn.setBorder(null);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        return btn;
    }
}