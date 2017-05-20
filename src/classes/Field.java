package classes;

import java.awt.*;
import java.util.ArrayList;

public class Field {


    public static Cellular[][] field = new Cellular[Configurator.fieldX][Configurator.fieldY];

    /**
     * Медод для самой начальной инициализации поля. Тут только рассаживается грунт
     */
    public static void prepareField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Cellular(i, j);
            }
        }
    }

    /**
     * Метод для очистки поля от старых сущностей. Выполняется кадждый такт
     */
    public static void eraseField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].entity = null;
            }
        }
    }

    /**
     * @param x - координата в абсотютных координатах
     * @param y - координата в абсотютных координатах
     * @return - объект класса Cellular, который живет в клетке с коорднатами
     */
    public static Cellular getCellular(int x, int y) {
        return field[x][y];
    }

    /**
     * Метод для посадки животного в поле
     *
     * @param sprite
     */
    public static void setEntity(Sprite sprite) {
        try {
            field[sprite.X / Configurator.pixels][sprite.Y / Configurator.pixels].entity = sprite;
        } catch (Exception e) {
            System.out.println("X=" + sprite.X + " Y=" + sprite.Y);

        }
    }

    /**
     * Метод сканирует пространство вокруг живности для принятия решения о дальнеших действиях
     *
     * @param x    - абсолютное положение живности
     * @param y    - абсолютное положение живности
     * @param size - радиус, в пределах которого живность видит
     * @return - список LocationResult, который содержит тип и расположение живности
     */
    public static ArrayList<LocationResult> getLocationResults(int x, int y, int size) {
        ArrayList<LocationResult> results = new ArrayList<LocationResult>();
        //System.out.println("x:" + x + " y:" + y + " s:" + size);

        for (int i = x - size; i <= x + size; i++) {
            for (int j = y - size; j <= y + size; j++) {
                if ((i == x && y == j) || i > Configurator.fieldX - 1 || j > Configurator.fieldY - 1 || i < 0 || j < 0 || field[i][j].entity == null) {
                    continue;
                }
                LocationResult locatorResult = new LocationResult(field[i][j].entity);

                if (i < x) {
                    locatorResult.LEFT = true;
                }
                if (i > x) {
                    locatorResult.RIGHT = true;
                }
                if (j > y) {
                    locatorResult.DOWN = true;
                }
                if (j < y) {
                    locatorResult.UP = true;
                }
                results.add(locatorResult);
            }
        }
        return results;
    }

    /**
     * метод для передвижения сущности на одну клетку
     *
     * @param oldX
     * @param oldY
     * @param newX
     * @param newY
     */
    public static void makeTurn(int oldX, int oldY, int newX, int newY) {
        try {
            field[newX][newY].entity = field[oldX][oldY].entity;
            field[oldX][oldY].entity = null;
        } catch (Exception e) {
            RuntimeException test = new RuntimeException(e + "entity oldY=" + oldY + " newY" + newY);
            throw test;
        }
    }

    /**
     * Метод обновляет поле, заставляя просчитать поведение каждой травяной клетки
     */
    public static void renewGround() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].background.calculateBehavior();
            }
        }
    }


    /**
     * Перезапускает рендер у всех сущностей, лежащих в клетках <br/>
     * Если в клетке нет существа, то ортисовывай траву<br/>
     *
     * @param g - Graphics
     */
    public static void renderField(Graphics g) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].entity == null) {
                    field[i][j].background.render(g);
                } else {
                    field[i][j].entity.render(g);
                }
            }
        }
    }

    public static int getTotalOccupiedCells() {
        int result = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] != null) result++;
            }
        }
        return result;
    }


    /**
     * Вспомагательный класс клетка, дальше будет расширяться
     * Класс поле содержит двухмерный массив с такими вот клетками, в которых могут жить трава и существа
     */
    public static class Cellular {
        public Cellular(int x, int y) {
            background = new Ground(x * Configurator.pixels, y * Configurator.pixels);
        }

        Sprite background; // трава/земля/вода
        Sprite entity; // овцы/волки/человек
    }


}

