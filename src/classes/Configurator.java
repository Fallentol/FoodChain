package classes;


public class Configurator {
    public static final int fieldX = 400;
    public static final int fieldY = 240;
    public static final int pixels = 4; // количество пикселей в одной клекте
    public static int WIDTH = fieldX * pixels; //ширина
    public static int HEIGHT = fieldY * pixels; //ширина

    public static int sheepVision = 10;
    public static int gameTemp = 100; // задержка отрисовки кадров
    public static int grassGrowthRate = 800; // через сколько шагов трава отрастает
    public static int shipSatiety = 200; // сытость овцы
    public static int shipAppetit = 2; // сколько травы овца сьедает за одну травяную клетку
    public static int directRunTurns = 3; // количество ходов, после которых стоит сменить направление (чем больше, тем реже направление меняется)
    public static int shipHollyLifeDaysRate = 30; // порог после которого овца может размножаться


}

