package classes;

import javax.swing.*;
import java.awt.*;

public class Start {
    public static void main(String[] args) {
        Game game = new Game();
        game.setPreferredSize(new Dimension(Configurator.WIDTH, Configurator.HEIGHT));
        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        game.start();
    }
}
