import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class DiningPhilosophers {
    private static final int N = 5;

    public static void main(String[] args) {
        Butler butler = new Butler(new Semaphore(N - 1, true));

        Fork[] forkNum = new Fork[N];
        for (int i = 0; i < N; i++) {
            forkNum[i] = new Fork(i); 
        }

        List<Thread> philosopherThreads = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Fork left = forkNum[i];
            Fork right = forkNum[(i + 1) % N];

            Philosopher p = new Philosopher(i, left, right, butler);
            Thread t = new Thread(p, "Philosopher-" + i);
            philosopherThreads.add(t);
        }

        for (Thread t : philosopherThreads) {
            t.start();
        }
    }
}
