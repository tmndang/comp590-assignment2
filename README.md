# Design Rationale for Dining Philosophers Problem Solution

**Tracy Dang, Bennett Rakower, Megha Thumma**

## Features and Structure Used

**Note:** the `while` loop in the `Philosopher` class is given a duration of 30 seconds to make sure that the program produces enough output in order to be able to make conclusions, but it still ensures that there are no inifnite loops.

### Philosophers

* The `Philosopher` class is used to instantiate each philosopher at the table
* This class implements the `Runnable` interface to allow each philosopher to be able to run as a thread
* Each philosopher is assigned an `id` so that they can be easily identifiable in the output
* Each philosopher has a `left` and `right` fork created through the `Fork` class, since each philosopher needs both forks to eat
* Each philosopher also has access to a shared `Butler`, which controls access to the dining table
  * **The use of the Butler (implemented using Semaphore) makes sure that only 4 out of 5 philosophers can try to eat at once.**
  * A philosopher must `acquire` permission from the Butler before picking up the forks
  * After eating, the philosopher `releases` the permission, allowing another waiting philosopher to enter

It is important that only 4 out of 5 philosophers are allowed to eat at a time because this makes sure that at least one fork is always available to pick up.  
If all 5 philosophers were allowed to pick up forks at the same time, each could grab their left fork and become stuck waiting for their right fork, causing a **deadlock**.  
By limiting table access to 4 philosophers, one fork is always free, making sure there is progress and avoiding deadlock.

### Forks

* Each fork is an instance of the `Fork` class
* Each fork is given a id through the `id` field
  * This allows the output to specify which fork is being picked up or put down
* The `Fork` class contains a `ReentrantLock`
  * This enforces prevents race conditions when philosophers attempt to pick up or put down forks

#### Methods

The `Fork` class contains two main methods:

* `pickUpFork()` — picking up a fork
* `putDownFork()` — putting down a fork

The `pickUpFork()` method locks the fork and the `putDownFork()` method unlocks it.  
This makes sure that once a philosopher picks up a fork, no other philosopher can use it until it is put down after eating.  
**This prevents multiple philosophers from holding the same fork at the same time.**

### Spaghetti

* The "spaghetti" is represented by the `eat()` method in the `Philosopher` class
* Eating is simulated by printing a message and pausing the thread briefly using `Thread.sleep()`

### Thinking

* The `think()` method represents the philosopher’s thinking phase
* This is like `eat()`, it prints the philosopher’s current state and pauses 

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

Forks and Butler permits are released inside a `finally` block to ensure that shared resources are always returned, even if the thread is interrupted. This helps prevent deadlock and starvation caused by unreleased resources.

### Preventing Deadlock

Deadlock is prevented through the use of the Butler Semaphore:

* Only 4 philosophers can eat at the same time
* At least one fork is always available
* Circular waiting between all philosophers cannot occur

Forks are also always picked up and put down in a controlled manner, which reduces the chance of deadlock.

### Preventing Starvation

Starvation is prevented through:

* The fair `Semaphore` used by the Butler
* Fair `ReentrantLock` usage on forks
* Quick release of forks and table permissions after eating

Once a philosopher finishes eating, they  release both forks and their Butler permission, allowing waiting philosophers to proceed.

#### Deadlocks

We think that deadlocks are very unlikely due to the Butler limiting access to the dining table. Since not all philosophers can try to pick up forks at once, at least one philosopher is always able to eat and make progress.

#### Starvation

We also think that starvation is unlikely due to the use of fair semaphores and locks.However, complete starvation prevention cannot be guaranteed because thread scheduling is ultimately controlled.
