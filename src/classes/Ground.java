package classes;


import java.awt.*;

/**
 * Conditions:
 * 0=normal
 * 1=eaten
 * 2=fired
 */
public class Ground extends Sprite {

    public Ground(int x, int y) {
        super(x, y);
        color = new Color(26, 157, 4);
        type = Type.GROUND;
        condition = 0;
    }

    private int grossRiseTurns = 0;

    @Override
    void calculateBehavior() {

        boolean needToCheck = false;
        if (grossRiseTurns >= 0) {
            grossRiseTurns--;
            needToCheck = true;
        }
        if (needToCheck && grossRiseTurns == -1) {
            color = new Color(26, 157, 4);
            condition = 0;
        }

    }

    /**
     * 0=траву съели
     * 1=трава горит
     *
     * @param i - тип события
     */
    @Override
    void outsideImpactTrigger(int i) {
        if (i == 0) {
            grassEaten();
        }
    }

    @Override
    void render(Graphics g) {
        super.draw(g);
    }

    @Override
    Sprite reproduction() {
        return null;
    }

    private void grassEaten() {
        grossRiseTurns = Configurator.grassGrowthRate;
        condition = 1;
        color = new Color(59, 66, 35);
    }


}
