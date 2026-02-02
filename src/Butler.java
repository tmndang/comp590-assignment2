import java.util.concurrent.Semaphore;

public class Butler {
    private final Semaphore seats;

    public Butler(Semaphore seats) {
        this.seats = seats;
    }

    public void enter() throws InterruptedException {
        seats.acquire();
    }

    public void leave() {
        seats.release();
    }
}
