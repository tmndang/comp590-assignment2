import java.util.concurrent.locks.ReentrantLock;

public final class Fork {
    private final int id;
    private final ReentrantLock lock;

    public Fork(int id) {
        this.id = id;
        this.lock = new ReentrantLock(true); 
    }

    public void pickUpFork(int philosopherId) {
        lock.lock();
        System.out.println("Philosopher number " + philosopherId + " picked up fork number " + id);
    }

    public void putDownFork(int philosopherId) {
        System.out.println("Philosopher number " + philosopherId + " put down fork number " + id);
        lock.unlock();
    }

    public int getId() {
        return id;
    }
}
