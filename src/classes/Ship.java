package classes;

import java.awt.*;
import java.util.ArrayList;

/**
 * Сущность овца
 * 0=убита
 * 1=питается
 * 2=убегает
 */
public class Ship extends Sprite {
    public Ship(int x, int y) {
        super(x, y);
        condition = 1;
        color = new Color(168, 193, 190);
        type = Type.SHIP;
        satiety = Configurator.shipSatiety;
    }

    int hollyLifeDays = 0;

    private int vision = Configurator.sheepVision;

    @Override
    void calculateBehavior() {
        if (condition == 1) {
            feeding();
        }
        if (satiety <= 0) {
            condition = 0;
        }

        if (satiety > 0.95 * Configurator.shipSatiety) {
            hollyLifeDays++;
        } else {
            hollyLifeDays = 0;
        }

        if (hollyLifeDays > Configurator.shipHollyLifeDaysRate) {
            color = new Color(168, 193, 190);
        }
        lifeTurn++;

    }

    /**
     * 0=траву съели
     * 1=трава горит
     *
     * @param i - тип события
     */
    @Override
    void outsideImpactTrigger(int i) {

    }

    @Override
    void render(Graphics g) {
        super.draw(g);
    }

    /**
     * метод размножения овец
     *
     * @return
     */
    public Sprite reproduction() {
        if (hollyLifeDays < Configurator.shipHollyLifeDaysRate)
            return null; // если счастливых дней недостаточно не размножаться

        if (((int) (Math.random() * 300) == 0)) {
            int newX = X + dim;
            int newY = Y + dim;
            if (X < 40) newX = X + dim;
            if (Y < 40) newX = Y + dim;
            if (X > Configurator.WIDTH - 40) newX = X - dim;
            if (Y > Configurator.HEIGHT - 40) newY = Y - dim;
            Ship s = new Ship(X + dim, Y + dim);
            s.color = new Color(223, 196, 65);
            hollyLifeDays = 0;
            return s;
        }
        return null;
    }

    /**
     * Метод описывает поведение овцы при питании
     * 0-вверх
     * 1-вправо
     * 2-вниз
     * 3-влево
     */
    private void feeding() {
        Field.Cellular cell = Field.getCellular(X / dim, Y / dim);
        if (cell.background.condition == 0) { // если трава была и все впорядке
            cell.background.outsideImpactTrigger(0);
            if (satiety < Configurator.shipSatiety) satiety += Configurator.shipAppetit; // повысить сытость
        } else {
            satiety--;
        }
        receiveRestriction(); //сбрасываем старые рекомендации и получаем новые по поводу стенок поля
        normalMove();


    }

    private void receiveRestriction() {
        super.autoCorrectionDirection();
        for (LocationResult lr : Field.getLocationResults(X, Y, vision)) {
            //System.out.println("РЕКОМЕНДАЦИИ ПОСТУПИЛИ rl=" + lr.UP + " " + lr.DOWN + " " + lr.RIGHT + " " + lr.LEFT);
            if (lr.sprite.type == Type.SHIP) { // если встретил овцу
                if (lr.DOWN) DOWN = -1;
                if (lr.UP) UP = -1;
                if (lr.LEFT) LEFT = -1;
                if (lr.RIGHT) RIGHT = -1;
            }
        }
    }

    private void normalMove() {
        int calcIteration = 10;
        boolean recalculate = true;
        while (calcIteration > 0 && recalculate) {
            calcIteration--;
            int direction = lastTurnDirection;
            if (((int) (Math.random() * Configurator.directRunTurns) == 0)) { //  один раз в N количесво ходов нужно сменить направление
                direction = (int) (Math.random() * 4);
                //direction = 0; // для отладки движение только вверх
                lastTurnDirection = direction;
            }
            if (direction == 0 && UP >= 0) {
                if (Y == 0) continue;
                Field.makeTurn(X / dim, Y / dim, X / dim, (Y / dim) - 1);
                Y -= dim;
                recalculate = false;
            }
            if (direction == 1 && RIGHT >= 0) {
                if (X > Configurator.WIDTH - dim * 2) continue;
                Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, Y / dim);
                X += dim;
                recalculate = false;
            }
            if (direction == 2 && DOWN >= 0) {
                if (Y > Configurator.HEIGHT - dim * 5) continue;
                Field.makeTurn(X / dim, Y / dim, X / dim, (Y / dim) + 1);
                Y += dim;
                recalculate = false;
            }
            if (direction == 3 && LEFT >= 0) {
                if (X == 0) continue;
                Field.makeTurn(X / dim, Y / dim, (X / dim) - 1, Y / dim);
                X -= dim;
                recalculate = false;
            }
        }
    }


}
