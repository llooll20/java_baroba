import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tbox{
    public static void main(String[] args){
        JFrame frame = new JFrame("TextField");
        Dimension d = new Dimension(400, 400);
        frame.setPreferredSize(d); //객체에 해야함
        frame.setLocation(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GridLayout ly = new GridLayout(5, 1, 0, 0);
        frame.setLayout(ly);

        JLabel l = new JLabel("Label");
        JTextField tf = new JTextField();
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setVerticalAlignment(SwingConstants.CENTER);
        JButton b1 = new JButton("SAVE", null);
        JButton b2 = new JButton("Enable", null);
        b1.setHorizontalAlignment(SwingConstants.CENTER);
        b2.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setText("Preset");

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                l.setText(tf.getText());
            }
        ;
        });
        
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                
                tf.setEnabled(false);
            }

        });
        frame.add(l, BorderLayout.NORTH);
        frame.add(b1, BorderLayout.CENTER);
        frame.add(b2, BorderLayout.CENTER);
        frame.add(tf, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);



    }
}