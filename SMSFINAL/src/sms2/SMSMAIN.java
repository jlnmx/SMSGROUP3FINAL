package sms2;

import java.awt.EventQueue;

public class SMSMAIN {

    public static void main(String[] args) {
        // TODO code application logic here
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Homepage m = new Homepage();
                m.setVisible(true);
            }
        });
    }

}
