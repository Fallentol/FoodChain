package classes;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static boolean running;

    public static String NAME = "FOODCHAIN V1.0"; //заголовок окна

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean firePressed = false;
    private int lifeTurnRecord = 0;
    Sprite recordSmen;
    private ArrayList<Sprite> entites = new ArrayList<>();
    String log = "";
    String log2 = "";
    String staticLog = "";


    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        long delta;
        init();
        while (running) {
            delta = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            update(delta);
            try {
                render();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void update(long delta) {
        log = "";
        Field.eraseField(); // очищает поле от живности
        Field.renewGround();// обновлет траву на поле

        ArrayList<Sprite> spriteList = new ArrayList<>();
        for (Sprite s : entites) {
            if (s.condition > 0) { // если сущность жива
                Field.setEntity(s);
                spriteList.add(s);
            }
        }
        entites = spriteList;

        int totalWolfs = 0;
        int totalShips = 0;
        ArrayList<Sprite> newShipList = new ArrayList<>();
        for (Sprite s : entites) {
            s.calculateBehavior();
            Sprite n = s.reproduction();
            if (s.type == Sprite.Type.WOLF) {
                log += "WolfS:" + s.satiety + " ";
                totalWolfs++;
            } else {
                totalShips++;
            }
            if (n != null) newShipList.add(n);
        }
        log2 = " Wolves = " + totalWolfs + " Ships=" + totalShips;
        entites.addAll(newShipList);
    }

    public void render() throws InterruptedException {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }
        Graphics g = bs.getDrawGraphics();


        paintField(g); // рендерится зеленый квадрат
        Field.renderField(g);// ренедерится трава и живность
        paintGrid(g); // рендерится сетка

        //log += entites.get(0).getSprieReport();
        //log2 += entites.get(1).getSprieReport();


        outLOG(g);
        g.dispose();
        bs.show();

        //Thread.sleep(Configurator.gameTemp);

    }

    public void init() {

        Field.prepareField();
        entites.add(new Ship(24, 24));
        entites.add(new Ship(80, 80));
        entites.add(new Ship(120, 120));
        entites.add(new Ship(200, 120));
        entites.add(new Wolf(400, 88));
        entites.add(new Wolf(440, 96));
        //entites.add(new Ship(120, 120));
        /*entites.add(new Ship(300, 100));
        entites.add(new Ship(500, 300));
        entites.add(new Ship(500, 24));
        entites.add(new Ship(200, 80));
        entites.add(new Ship(360, 200));
        entites.add(new Ship(700, 400));
        entites.add(new Ship(700, 40));
        entites.add(new Ship(700, 200));
        entites.add(new Ship(600, 360));

        entites.add(new Ship(800, 100));
        entites.add(new Ship(300, 120));
        entites.add(new Ship(560, 320));
        entites.add(new Ship(500, 24));
        entites.add(new Ship(280, 80));
        entites.add(new Ship(460, 220));
        entites.add(new Ship(720, 400));
        entites.add(new Ship(700, 80));
        entites.add(new Ship(760, 220));
        entites.add(new Ship(640, 380));

        entites.add(new Ship(900, 100));
        entites.add(new Ship(820, 120));
        entites.add(new Ship(860, 420));
        entites.add(new Ship(800, 24));
        entites.add(new Ship(880, 480));
        entites.add(new Ship(760, 220));
        entites.add(new Ship(740, 440));
        entites.add(new Ship(720, 80));
        entites.add(new Ship(860, 220));
        entites.add(new Ship(660, 380));

        entites.add(new Ship(100, 20));
        entites.add(new Ship(100, 60));
        entites.add(new Ship(100, 80));
        entites.add(new Ship(100, 120));
        entites.add(new Ship(120, 160));
        entites.add(new Ship(100, 180));
        entites.add(new Ship(100, 220));
        entites.add(new Ship(80, 260));
        entites.add(new Ship(100, 300));
        entites.add(new Ship(100, 360));

        entites.add(new Ship(40, 20));
        entites.add(new Ship(40, 60));
        entites.add(new Ship(40, 80));
        entites.add(new Ship(40, 120));
        entites.add(new Ship(40, 160));
        entites.add(new Ship(40, 180));
        entites.add(new Ship(40, 220));
        entites.add(new Ship(40, 260));
        entites.add(new Ship(40, 300));
        entites.add(new Ship(40, 360));*/

        addKeyListener(new KeyInputHandler());
    }

    /**
     * Метод для отрисовки фона поля
     *
     * @param g
     */
    private void paintField(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Метод для отрисовки сетки/разметки
     *
     * @param g
     */
    private void paintGrid(Graphics g) {
        g.setColor(Color.GRAY);
        for (int i = Configurator.pixels; i <= Configurator.HEIGHT; i += Configurator.pixels) {
            g.drawLine(0, i, Configurator.WIDTH, i);
        }
        for (int i = Configurator.pixels; i <= Configurator.WIDTH; i += Configurator.pixels) {
            g.drawLine(i, 0, i, Configurator.HEIGHT);
        }
    }

    /**
     * Метод для отрисовки логов
     *
     * @param g
     */
    private void outLOG(Graphics g) {
        int fontSize = 20;
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(Color.YELLOW);
        g.drawString(log, 2, Configurator.HEIGHT - 40);
        g.drawString(log2, 2, Configurator.HEIGHT - 10);
    }


    public class KeyInputHandler extends KeyAdapter {

        public void keyPressed(KeyEvent e) { //клавиша нажата
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }

        }

        public void keyReleased(KeyEvent e) { //клавиша отпущена
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }
    }

}

