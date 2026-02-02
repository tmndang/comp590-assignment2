
public final class Philosopher implements Runnable {
    private final int id;
    private final Fork left;
    private final Fork right;
    private final Butler butler;

    private final long runMilliseconds;

    public Philosopher(int id, Fork left, Fork right, Butler butler) {
        this(id, left, right, butler, 30_000); 
    }

    public Philosopher(int id, Fork left, Fork right, Butler butler, long runMilliseconds) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.butler = butler;
        this.runMilliseconds = runMilliseconds;
    }

    @Override
    public void run() {
        long stopAt = System.currentTimeMillis() + runMilliseconds;

        while (System.currentTimeMillis() < stopAt && !Thread.currentThread().isInterrupted()) {
            try {
                think();

                System.out.println("requests permition from butler");
                butler.enter();
                System.out.println("got butler permition");

                boolean leftIsBeingHeld = false;
                boolean rightIsBeingHeld = false;

                try {
                    left.pickUpFork(id);
                    leftIsBeingHeld = true;

                    right.pickUpFork(id);
                    rightIsBeingHeld = true;

                    eat();
                } finally {
                    if (rightIsBeingHeld) right.putDownFork(id);
                    if (leftIsBeingHeld) left.putDownFork(id);

                    butler.leave();
                    System.out.println("released butler permition");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("interrupted, stopping");
            }
        }

        System.out.println("done");
    }

    private void think() throws InterruptedException {
        System.out.println("Thinking");
        Thread.sleep(250);
    }

    private void eat() throws InterruptedException {
        System.out.println("Eating");
        Thread.sleep(250);
    }
}
