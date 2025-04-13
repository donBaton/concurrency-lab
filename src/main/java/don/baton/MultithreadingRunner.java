package don.baton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadingRunner {
    public static void test(AccountManager accountManager, int threads, int operations) {
        System.out.println(accountManager.getClass().getSimpleName() + "\n\tStarting " + threads + " threads, " + operations + " operations");
        long account1 = accountManager.createAccount(new BigDecimal(10_000_000));
        long account2 = accountManager.createAccount(new BigDecimal(10_000_000));
        System.out.println("\taccount1 balance before transfers: " + accountManager.getBalance(account1));
        System.out.println("\taccount2 balance before transfers: " + accountManager.getBalance(account2) + "\n");
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        List<Callable<Boolean>> workers = new ArrayList<>(threads);
        for (int i = 0; i < threads / 2; i++) {
            workers.add(() -> {
                for (int j = 0; j < operations; j++) {
                    accountManager.transferMoney(account1, account2, BigDecimal.valueOf(1));
                }
                return true;
            });
            workers.add(() -> {
                for (int j = 0; j < operations; j++) {
                    accountManager.transferMoney(account2, account1, BigDecimal.valueOf(1));
                }
                return true;
            });
        }
        long startTime = System.currentTimeMillis();
        try {
            executor.invokeAll(workers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        System.out.println("\taccount1 balance after transfers: " + accountManager.getBalance(account1));
        System.out.println("\taccount2 balance after transfers: " + accountManager.getBalance(account2));
        System.out.println("\tTime taken: " + (endTime - startTime) + "ms\n");
    }
}
