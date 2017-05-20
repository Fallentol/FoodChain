package classes;


public class Configurator {
    public static final int fieldX = 235;
    public static final int fieldY = 125;
    public static final int pixels = 8; // количество пикселей в одной клекте
    public static int WIDTH = fieldX * pixels; //ширина
    public static int HEIGHT = fieldY * pixels; //ширина

    public static int sheepVision = 8;
    public static int gameTemp = 100; // задержка отрисовки кадров
    public static int grassGrowthRate = 400; // через сколько шагов трава отрастает
    public static int shipSatiety = 200; // сытость овцы
    public static int shipAppetit = 2; // сколько травы овца сьедает за одну травяную клетку
    public static int shipDirectRunTurns = 4; // количество ходов, после которых стоит сменить направление (чем больше, тем реже направление меняется)
    public static int shipHollyLifeDaysRate = 40; // порог после которого овца может размножаться
    public static int shipRepairChanse = 50; // пвероятность после которого овца может размножаться

    public static int wolfSatiety = 300; // сытость волка
    public static int wolfAppetit = 60; // сколько волк сьедает за одну сьеденную овцу
    public static int wolfVision = 8;
    public static int wolfDirectRunTurns = 15; // количество ходов, после которых стоит сменить направление (чем больше, тем реже направление меняется)
    public static int wolfRepairChanse = 900; // пвероятность после которого овца может размножаться


}

