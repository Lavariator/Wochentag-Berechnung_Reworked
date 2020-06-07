//
//Programmed by Lavariator

package Wochentag;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

/*TODO Maybe create Dropdownmenus*/

public class WochentagBerechnung extends JFrame {
    private JTextField textTag = null;
    private JTextField textMonat = null;
    private JTextField textJahr = null;
    private JLabel ergebnisLabel = null;
    Timer timer;


    public WochentagBerechnung() {
        setTitle("Wochentag-Rechner");

        setLayout(new FlowLayout());

        JLabel labelTag = new JLabel("Tag:");
        JLabel labelMonat = new JLabel("Monat:");
        JLabel labelJahr = new JLabel("Jahr:");

        add(labelTag);
        add(getTag());
        add(labelMonat);
        add(getMonat());
        add(labelJahr);
        add(getJahr());
        add(getButton());
        add(getErgebnisLabel());

        pack();

        setSize(330, 95);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        System.out.println();
        setIconImage(new ImageIcon(getClass().getResource("/resources/Icon.png")).getImage());
        setVisible(true);
    }


    private JLabel getErgebnisLabel() {
        if (ergebnisLabel == null) {
            ergebnisLabel = new JLabel();
            ergebnisLabel.setPreferredSize(new Dimension(245, 22));
            ergebnisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return ergebnisLabel;
    }

    private JTextField getTag() {
        if (textTag == null) {
            textTag = new JTextField();
            textTag.setPreferredSize(new Dimension(20, 22));
            textTag.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    if (textTag.getText().length() >= 2) keyEvent.consume();
                }
            });
        }
        return textTag;
    }

    private JTextField getMonat() {
        if (textMonat == null) {
            textMonat = new JTextField();
            textMonat.setPreferredSize(new Dimension(20, 22));
            textMonat.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    if (textMonat.getText().length() >= 2) keyEvent.consume();
                }
            });
        }
        return textMonat;
    }

    private JTextField getJahr() {
        if (textJahr == null) {
            textJahr = new JTextField();
            textJahr.setPreferredSize(new Dimension(40, 22));
            textJahr.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    if (textJahr.getText().length() >= 4) keyEvent.consume();
                }
            });
        }
        return textJahr;
    }

    private JButton getButton() {
        JButton startButton = new JButton("Berechnen!");
        ActionListener actionListener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getErgebnisLabel().setText(berechneWochentag());
                if(actionEvent.getSource() == timer){
                    getErgebnisLabel().setText("");
                    timer.stop();
                    return;
                }

                timer = new Timer(3500, this);
                timer.start();
            }
        };
        startButton.addActionListener(actionListener);
        return startButton;
    }

    private String berechneWochentag()  {
        String wochentagString = "";

        try {
            int tag = Integer.parseInt(getTag().getText());
            int monat = Integer.parseInt(getMonat().getText());
            String jahr = getJahr().getText();

            if (checkMonthLength(tag, monat, Integer.parseInt(jahr)) || Integer.parseInt(jahr) < 1 || Integer.parseInt(jahr) > 9999)
                throw new NumberFormatException();

            //Berechnung
            int nTag = tag % 7;

            int nMonat = 0;
            switch (monat){
                case 2:
                case 3:
                case 11:
                    nMonat = 3;
                    break;
                case 4:
                case 7:
                    nMonat = 6;
                    break;
                case 9:
                case 12:
                    nMonat = 5;
                    break;
                case 5:
                    nMonat = 1;
                    break;
                case 6:
                    nMonat = 4;
                    break;
                case 8:
                    nMonat = 2;
                    break;
            }

            int jahrVorne = 0;
            int jahrHinten = 0;

            switch (jahr.length()){
                case 1:
                case 2:
                    jahrHinten = Integer.parseInt(jahr);
                    break;
                case 3:
                    jahrVorne = Integer.parseInt(jahr.substring(0,1));
                    jahrHinten = Integer.parseInt(jahr.substring(1,3));
                    break;
                case 4:
                    jahrVorne = Integer.parseInt(jahr.substring(0,2));
                    jahrHinten = Integer.parseInt(jahr.substring(2, 4));
                    break;
            }

            int nJarhundert = (3 - (jahrVorne % 4)) * 2;
            int nJahr = (jahrHinten + jahrHinten / 4) % 7;

            //Schaltjahr-Überprüfung
            int nSchalt = 0;
            if (Integer.parseInt(jahr) % 4 == 0)
                nSchalt = 6;

            //Wochentag-Auswahl
            int w = ((nTag + nMonat + nJahr + nJarhundert + nSchalt) + 1) % 7;

            String wochentag = "";

            switch (w){
                case 0:
                    wochentag ="Sonntag";
                    break;
                case 1:
                    wochentag = "Montag";
                    break;
                case 2:
                    wochentag = "Dienstag";
                    break;
                case 3:
                    wochentag = "Mittwoch";
                    break;
                case 4:
                    wochentag = "Donnerstag";
                    break;
                case 5:
                    wochentag = "Freitag";
                    break;
                case 6:
                    wochentag = "Samstag";
                    break;
            }

            wochentagString = String.format("Wochentag: %s", wochentag);
            getErgebnisLabel().setForeground(Color.black);

        } catch (NumberFormatException e) {
            wochentagString = "Sie haben eine ungültige Zahl eingegeben!";
            getErgebnisLabel().setForeground(Color.red);
        }

        return wochentagString;
    }

    private Boolean checkMonthLength(Integer tag, Integer monat, Integer jahr){
        boolean b = false;
        ArrayList<Integer> einundreissiger = new ArrayList(Collections.singletonList(new int[]{1, 3, 5, 7, 8, 10, 12}));
        ArrayList<Integer> dreissiger = new ArrayList(Collections.singletonList(new int[] {4, 6, 9, 11}));

        if (einundreissiger.contains(monat) && 0 < tag && tag < 32)
            b = true;
        if (dreissiger.contains(monat) && 0 < tag && tag < 31)
            b = true;
        if (monat == 2) {
            if ((jahr % 4 == 0 && tag > 0 && tag < 30) || (tag > 0 && tag < 29)) {
                b = true;
            }
        }
        return b;
    }

    public static void main(String[] args) {
        WochentagBerechnung gui = new WochentagBerechnung();
    }
}