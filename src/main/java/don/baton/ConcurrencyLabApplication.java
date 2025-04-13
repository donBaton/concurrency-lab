package don.baton;

import don.baton.task1.SimpleAccountManager;
import don.baton.task2.SynchronizedMethodAccountManager;
import don.baton.task4.SynchronizedSortedAccountsAccountManager;
import don.baton.task5.ReentrantLockAccountManager;
import don.baton.task6.OptimisticLockAccountManager;

public class ConcurrencyLabApplication {

    public static final int THREADS = 1000;
    public static final int OPERATIONS = 100_000;

    public static void main(String[] args) {
        MultithreadingRunner.test(new SimpleAccountManager(), THREADS, OPERATIONS);
        MultithreadingRunner.test(new SynchronizedMethodAccountManager(), THREADS, OPERATIONS);
        // Should be a deadlock
        // MultithreadingRunner.test(new SynchronizedAccountsAccountManager(), THREADS, OPERATIONS);
        MultithreadingRunner.test(new SynchronizedSortedAccountsAccountManager(), THREADS, OPERATIONS);
        MultithreadingRunner.test(new ReentrantLockAccountManager(), THREADS, OPERATIONS);
        MultithreadingRunner.test(new OptimisticLockAccountManager(), THREADS, OPERATIONS);
    }
}
