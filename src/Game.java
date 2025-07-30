import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable, KeyListener {

    //private static final long serialVersionUID = 1L;
    public static int WIDTH = 160;
    public static int HEIGHT = 120;
    public static int SCALE = 3;

    public BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

    public static Player player;
    public static Enemy enemy;
    public static Ball ball;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        this.addKeyListener(this);
        this.setFocusable(true); // Permite que o componente receba foco para eventos de teclado
        player = new Player(100, HEIGHT-5);
        enemy = new Enemy(100, 0);
        ball = new Ball(100, HEIGHT/2 - 1);
    }

    public static void main(String[] args){
        Game game = new Game();
        JFrame frame = new JFrame("Pong");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(game).start();
    }

    public void tick(){
        player.tick();
        enemy.tick();
        ball.tick();
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = layer.getGraphics();
        g.setColor(Color.BLACK);

        // Limpa o frame anterior
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Renderiza o player
        player.render(g);
        enemy.render(g);
        ball.render(g);

        // Desenha na tela
        g = bs.getDrawGraphics();
        g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);

        bs.show();
    }

    @Override
    public void run() {
        while(true) {
            tick();
            render();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // keyTyped é usado para caracteres digitáveis, não para teclas especiais
        // Deixamos vazio pois usamos keyPressed e keyReleased para as teclas de direção
    }
}