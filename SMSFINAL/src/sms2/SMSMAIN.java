package sms2;

import java.awt.EventQueue;

public class SMSMAIN {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenu m = new MainMenu();
                m.setVisible(true);
            }
        });
    }
    
}
