package classes;

/**
 * Класс содержит результаты поиска локатора
 * Класс- который будет хранить результат работы локатора, <br/>
 * рекомендации куда бежать, откуда бежать и от кого бежать
 */
public class LocationResult {
    public LocationResult(Sprite s) {
        sprite = s;
    }

    public Sprite sprite;
    boolean UP;
    boolean DOWN;
    boolean LEFT;
    boolean RIGHT;
}
