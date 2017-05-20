package classes;

import java.awt.*;

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
        name = String.valueOf((int) (Math.random() * 5));
    }

    int hollyLifeDays = 0;

    private int vision = Configurator.sheepVision;

    @Override
    void calculateBehavior() {
        if (condition == 1) {
            feeding();
        }
        if (satiety <= 0) { // если еда закончилась - переходит в режим умерла
            condition = 0;
        }

        if (satiety > 0.8 * Configurator.shipSatiety) { // если довольно упитанная - срабатывает счетчик счастливых дней
            hollyLifeDays++;
        } else {
            hollyLifeDays = 0;
        }

        if (hollyLifeDays > Configurator.shipHollyLifeDaysRate) {
            color = new Color(168, 193, 190);
        }
        lifeTurn++; // счетчик прожитих дней

    }

    /**
     * 0=траву съели
     * 1=трава горит
     *
     * @param i - тип события
     */
    @Override
    void outsideImpactTrigger(int i) {
        condition = 0;
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

        if (((int) (Math.random() * Configurator.shipRepairChanse) == 0)) {

            int newX = X + dim;
            int newY = Y + dim;

            if (X <= Configurator.pixels * 2) {
                newX = X + dim;
            }
            if (Y < Configurator.pixels * 2) {
                newY = Y + dim;
            }
            if (X >= Configurator.WIDTH - Configurator.pixels * 2) {
                newX = X - dim;
            }
            if (Y >= Configurator.HEIGHT - Configurator.pixels * 2) {
                newY = Y - dim;
            }
            Ship s = new Ship(newX, newY);
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
        Field.Cellular cell = Field.getCellular(X / dim, Y / dim); // получаем клетку поля для проверки наличия в ней травы
        if (cell.background.condition == 0) { // если трава была и все впорядке
            cell.background.outsideImpactTrigger(0);
            if (satiety < Configurator.shipSatiety) satiety += Configurator.shipAppetit; // повысить сытость
        } else {
            satiety--; // если овца прошла по пустой клетке - её сытость уменьшилась
        }
        receiveRestriction(); //сбрасываем старые рекомендации и получаем новые по поводу стенок поля
        normalMove();


    }

    private void receiveRestriction() {
        super.autoCorrectionDirection();
        for (LocationResult lr : Field.getLocationResults(X / Configurator.pixels, Y / Configurator.pixels, vision)) {
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
            if (((int) (Math.random() * Configurator.shipDirectRunTurns) == 0)) { //  один раз в N количесво ходов нужно сменить направление
                direction = (int) (Math.random() * 4);
                //direction = 2; // для отладки движение только вверх
                lastTurnDirection = direction;
            }
            if (direction == 0 && UP >= 0) {
                if (Y == 0) continue;
                Field.makeTurn(X / dim, Y / dim, X / dim, (Y / dim) - 1);
                Y -= dim;
                recalculate = false;
            }
            if (direction == 1 && RIGHT >= 0) {
                if (X > Configurator.WIDTH - dim * 2) {
                    System.out.println("crityc X=" + X + " Configurator.WIDTH=" + (Configurator.WIDTH - dim * 2));
                }
                if (X >= Configurator.WIDTH - dim) continue;

                Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, Y / dim);
                X += dim;
                recalculate = false;
            }
            if (direction == 2 && DOWN >= 0) {
                if (Y > Configurator.HEIGHT - dim * 2) {
                    System.out.println("crityc Y=" + Y);
                }
                if (Y >= Configurator.HEIGHT - dim) continue;
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
