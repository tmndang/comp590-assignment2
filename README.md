# Design Rationale for Dining Philosophers Problem Solution

**Tracy Dang, Bennett Rakower, Megha Thumma**

## Features and Structure Used

**Note:** the `while` loop in the `Philosopher` class is given a duration of 30 seconds to make sure that the program produces enough output in order to be able to make conclusions, while ensuring that the loop does not go on forever.

### Philosophers

* The `Philosopher` class is used to instantiate each philosopher around the dining table
* This class implements the `Runnable` interface to allow each philosopher to be able to run as a thread
* Each philosopher is assigned an `id` so that they can be easily identifiable in the output
* Each philosopher has a `left` and `right` fork created through the `Fork` class, since each philosopher needs both forks to eat
* Each philosopher also has access to a shared `Butler`, which controls access to the dining table
  * **The use of the Butler (implemented using a Semaphore) ensures that only 4 out of 5 philosophers can attempt to eat at once.**
  * A philosopher must `acquire` permission from the Butler before picking up forks
  * After eating, the philosopher `releases` the permit, allowing another waiting philosopher to enter

It is important that only 4 out of 5 philosophers are allowed to eat at a time because this guarantees that at least one fork is always available.  
If all 5 philosophers were allowed to pick up forks simultaneously, each could grab their left fork and become stuck waiting for their right fork, causing a **deadlock**.  
By limiting table access to 4 philosophers, one fork is always free, ensuring progress and avoiding deadlock.

### Forks

* Each fork is an instance of the `Fork` class
* Each fork is given a unique identifier through the `id` field
  * This allows the output to specify which fork is being picked up or put down
* The `Fork` class contains a `ReentrantLock`
  * This enforces **mutual exclusion** and prevents **race conditions** when philosophers attempt to pick up or put down forks

#### Methods

The `Fork` class contains two main methods:

* `pickUpFork()` — represents picking up a fork
* `putDownFork()` — represents putting down a fork

The `pickUpFork()` method locks the fork, while the `putDownFork()` method unlocks it.  
This ensures that once a philosopher picks up a fork, no other philosopher can use it until it is put down after eating.  
**This guarantees mutual exclusion and prevents multiple philosophers from holding the same fork at the same time.**

### Spaghetti

* The "spaghetti" is represented by the `eat()` method in the `Philosopher` class
* Eating is simulated by printing a message and pausing the thread briefly using `Thread.sleep()`
* This delay represents a more realistic eating phase rather than an instantaneous action

### Thinking

* The `think()` method represents the philosopher’s thinking phase
* Similar to `eat()`, it prints the philosopher’s current state and pauses briefly
* This allows more natural interleavings between philosopher threads

### Life Cycle

Each philosopher follows this life cycle:

1. Think  
2. Request permission from the Butler  
3. Pick up the left fork  
4. Pick up the right fork  
5. Eat  
6. Put down the right fork  
7. Put down the left fork  
8. Release Butler permission

Forks and Butler permits are released inside a `finally` block to ensure that shared resources are always returned, even if the thread is interrupted.  
This helps prevent deadlock and starvation caused by unreleased resources.

### Preventing Deadlock

Deadlock is prevented primarily through the use of the Butler Semaphore:

* Only 4 philosophers may attempt to eat at the same time
* At least one fork is always available
* Circular waiting between all philosophers cannot occur

Additionally, forks are always picked up and put down in a controlled and consistent manner, further reducing the chance of deadlock.

### Preventing Starvation

Starvation is minimized through:

* The fair `Semaphore` used by the Butler
* Fair `ReentrantLock` usage on forks
* Prompt release of forks and table permits after eating

Once a philosopher finishes eating, they immediately release both forks and their Butler permit, allowing waiting philosophers to proceed.

### Are Deadlocks and Starvation Still Possible?

#### Deadlocks

Deadlocks are extremely unlikely due to the Butler limiting access to the dining table.  
Since not all philosophers can compete for forks at once, at least one philosopher is always able to eat and make progress.

#### Starvation

Starvation is also unlikely due to the use of fair semaphores and locks.  
However, complete starvation prevention cannot be guaranteed because thread scheduling is ultimately controlled by the JVM and operating system.  
In rare cases, a philosopher may wait longer than others, but the overall design significantly reduces this risk.