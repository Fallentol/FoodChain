package classes;

import java.awt.*;

public abstract class Sprite {

    /**
     * Координат не абсолютные, а координаты положения на поле. в одной клетке <br/>
     * 4 пикселя поэтому абсолютные и координаты поля отличаются в 4 раза
     */
    public int X = 0;
    public int Y = 0;
    public Color color;
    public int condition; // состояние объекта (спит, бежит, идет, съеден и пр)
    public int dim = Configurator.pixels;
    public int satiety;
    public int lastTurnDirection;
    public int lifeTurn = 0;
    public String name;
    /**
     * Позиции для смены координат (рекомендации)
     * 0=доступна
     * 1=рекомендуется
     * -1=запрещена
     */
    public int UP = 0;
    public int DOWN = 0;
    public int LEFT = 0;
    public int RIGHT = 0;


    enum Type {GROUND, SHIP, WOLF, MAN}

    public Type type;

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public Sprite(int x, int y) {
        X = x;
        Y = y;
    }

    public String getRecomendation (int x) {
        String result = "";
        if (x == 0) result = "free";
        if (x == 1) result = "best";
        if (x == -1) result = "avoid";
        return result;
    }

    /**
     * Метод высчитывает поведение спрайта в каждом такте
     */
    abstract void calculateBehavior();

    /**
     * Метод дергают когда в объекте пора менять состояние<br/>
     * параметр цифровой означает тип происшествия, для каждого спрайта они свои
     */
    abstract void outsideImpactTrigger(int event_cod);

    /**
     * Метод отрисовывает спрайт с его осбоенностями
     *
     * @param g
     */
    abstract void render(Graphics g);

    abstract Sprite reproduction();

    void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(X, Y, Configurator.pixels, Configurator.pixels);
    }

    void autoCorrectionDirection() {
        if (type == Type.GROUND) return; // для травы рекомендации не нужны
        UP = DOWN = LEFT = RIGHT = 0;
        // X Y - координаты поля отрисовки, а не из массива
        if (X >= Configurator.WIDTH - Configurator.pixels) RIGHT = -1;
        if (X < Configurator.pixels) LEFT = -1;
        if (Y >= Configurator.HEIGHT - Configurator.pixels) DOWN = -1;
        if (Y < Configurator.pixels) UP = -1;
    }

    public String getSprieReport() {
        String result = "Sprite " + name + "\n";
        result += " Direction UP:" + getRecomendation(UP);
        result += " RIGHT:" + getRecomendation(RIGHT);
        result += " DOWN:" + getRecomendation(DOWN);
        result += " LEFT:" + getRecomendation(LEFT);
        result += " Stat:" + condition;
        result += " X:" + X/Configurator.pixels;
        result += " Y:" + Y/Configurator.pixels;
        result += " Satiety:" + satiety;
        result += " Years:" + lifeTurn;
        return result;
    }


}

