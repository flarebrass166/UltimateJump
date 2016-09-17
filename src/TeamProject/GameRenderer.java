package TeamProject;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GameRenderer extends JPanel{

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Game.game.repaint(g);
    }
}