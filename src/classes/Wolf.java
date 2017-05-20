package classes;

import java.awt.*;

/**
 * Класс - волк, охотится на овцу, траву не ест, умирает от голода
 * <p>
 * <p>
 * Сущность волк
 * 0=убит
 * 1=бродит
 * 2=преследует
 */
public class Wolf extends Sprite {

    public Wolf(int x, int y) {
        super(x, y);
        condition = 1;
        color = new Color(11, 68, 99);
        type = Type.WOLF;
        satiety = Configurator.wolfSatiety;
        name = String.valueOf((int) (Math.random() * 15));
    }

    private int vision = Configurator.wolfVision;

    /**
     * Овца - или другая живность - жертва, которую преследует волк
     */
    public Sprite sacrifice;

    @Override
    void calculateBehavior() {
        //System.out.println("Сытость вока " + satiety);
        if (condition == 1) {
            foundSacrifice();
        }
        if (condition == 2) {
            pursueSacrifice();
        }
        if (satiety < 0) {
            outsideImpactTrigger(0);
        }
        satiety--;
        lifeTurn++;
    }

    private void foundSacrifice() {
        receiveRestriction();
        int calcIteration = 3;
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

                if (X >= Configurator.WIDTH - dim) continue;

                Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, Y / dim);
                X += dim;
                recalculate = false;
            }
            if (direction == 2 && DOWN >= 0) {
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

    private void pursueSacrifice() {
        receiveRestriction();
        //System.out.println("ГОНКА..... UP=" + UP + " DOWN=" + DOWN + " R=" + RIGHT + " L=" + LEFT);
        if (sacrifice == null) outsideImpactTrigger(1);

        if (UP > 0 && RIGHT > 0) {
            if (X + dim > Configurator.WIDTH || Y - dim < 0) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, (Y / dim) - 1);
            Y -= dim;
            X += dim;
        } else if (UP > 0 && LEFT > 0) {
            if (X - dim < 0 || Y - dim < 0) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) - 1, (Y / dim) - 1);
            Y -= dim;
            X -= dim;
        } else if (DOWN > 0 && RIGHT > 0) {
            if (X + dim > Configurator.WIDTH || Y + dim > Configurator.HEIGHT) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, (Y / dim) + 1);
            Y += dim;
            X += dim;
        } else if (DOWN > 0 && LEFT > 0) {
            if (X - dim < 0 || Y + dim > Configurator.HEIGHT) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) - 1, (Y / dim) + 1);
            Y += dim;
            X -= dim;
        } else if (UP > 0) {
            if (Y - dim < 0) return;
            Field.makeTurn(X / dim, Y / dim, X / dim, (Y / dim) - 1);
            Y -= dim;
        } else if (RIGHT > 0) {
            if (X + dim > Configurator.WIDTH) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) + 1, Y / dim);
            X += dim;
        } else if (DOWN > 0) {
            if (Y + dim > Configurator.HEIGHT) return;
            Field.makeTurn(X / dim, Y / dim, X / dim, (Y / dim) + 1);
            Y += dim;
        } else if (LEFT > 0) {
            if (X - dim < 0) return;
            Field.makeTurn(X / dim, Y / dim, (X / dim) - 1, Y / dim);
            X -= dim;
        }

        if (sacrifice != null && sacrifice.X == X && sacrifice.Y == Y) {
            sacrifice.outsideImpactTrigger(0); // сообщить овце, что она убита
            sacrifice = null;
            outsideImpactTrigger(1);
            if (satiety < Configurator.wolfSatiety) {
                satiety += Configurator.wolfAppetit;
            }
        }

    }

    private void receiveRestriction() {
        super.autoCorrectionDirection();
        boolean sacrificeLost = true;
        for (LocationResult lr : Field.getLocationResults(X / Configurator.pixels, Y / Configurator.pixels, vision)) {
            //System.out.println("Wolf rec type = " + lr.sprite.type);
            if (sacrifice == null) {
                //System.out.println("Потерял цель");
                outsideImpactTrigger(1);
            }


            //System.out.println("cond=" + condition + " sacr=" + sacrifice);

            if (condition == 2) { // если в состоянии гонки, то другие проверки делать не нужно
                //System.out.println("Зашел в текущую обработку гонки");
                if (lr.sprite == sacrifice) {
                    //System.out.println("Цель подтвердилась");
                    if (lr.DOWN) DOWN = 1;
                    if (lr.UP) UP = 1;
                    if (lr.LEFT) LEFT = 1;
                    if (lr.RIGHT) RIGHT = 1;
                    sacrificeLost = false;
                    break;
                }

                continue;

            }


            if (lr.sprite.type == Type.SHIP) { // если встретил овцу
                //System.out.println("переключился в режим гонки");
                if (lr.DOWN) DOWN = 1;
                if (lr.UP) UP = 1;
                if (lr.LEFT) LEFT = 1;
                if (lr.RIGHT) RIGHT = 1;
                outsideImpactTrigger(2);
                sacrifice = lr.sprite;
                sacrificeLost = false;
                color = new Color(185, 29, 17);
                //System.out.println("sacr " + sacrifice);
                break;
            }

            if (lr.sprite.type == Type.WOLF) { // если встретил волка
                if (lr.DOWN) DOWN = -1;
                if (lr.UP) UP = -1;
                if (lr.LEFT) LEFT = -1;
                if (lr.RIGHT) RIGHT = -1;
            }

        }

        if (sacrificeLost) {
            //System.out.println("Цель потеряна переход в обычный режим");
            outsideImpactTrigger(1);
            sacrifice = null;
            color = new Color(11, 68, 99);
        }
    }

    @Override
    void outsideImpactTrigger(int event_cod) {
        condition = event_cod;
    }

    @Override
    void render(Graphics g) {
        super.draw(g);
    }

    @Override
    Sprite reproduction() {
        if (satiety < Configurator.wolfSatiety) return null;
        if (((int) (Math.random() * Configurator.wolfRepairChanse) == 0)) {
            int newX = X + dim;
            int newY = Y + dim;
            if (X <= Configurator.pixels*2) newX = X + dim;
            if (Y < Configurator.pixels*2) newX = Y + dim;
            if (X >= Configurator.WIDTH - Configurator.pixels*2) newX = X - dim;
            if (Y >= Configurator.HEIGHT - Configurator.pixels*2) newY = Y - dim;
            Wolf s = new Wolf(newX, newY);
            s.color = new Color(69, 77, 134);
            return s;
        }
        return null;
    }
}
