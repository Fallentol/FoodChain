package classes;

/**
 * Класс содержит результаты поиска локатора<br/>
 * Класс- который будет хранить результат работы локатора, <br/>
 * рекомендации куда бежать, откуда бежать и от кого бежать<br/>
 *
 * public Sprite sprite  обнаруженный живой спрайт
 */
public class LocationResult {
    public LocationResult(Sprite s) {
        sprite = s;
    }

    public Sprite sprite; // обнаруже
    boolean UP;
    boolean DOWN;
    boolean LEFT;
    boolean RIGHT;
}
