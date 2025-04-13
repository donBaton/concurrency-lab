package don.baton.task4;

import don.baton.AccountManager;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SynchronizedSortedAccountsAccountManager implements AccountManager {

    Map<Long, Account> accounts = new ConcurrentHashMap<>();
    AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public long createAccount(BigDecimal amount) {
        long accountId = idGenerator.getAndIncrement();
        Account account = new Account(accountId, amount);
        accounts.put(accountId, account);
        return accountId;
    }

    @Override
    public BigDecimal getBalance(long accountId) {
        return accounts.get(accountId).getBalance();
    }

    @Override
    public void transferMoney(long from, long to, BigDecimal amount) {
        if (from == to) {
            throw new IllegalArgumentException("From and To both are equal");
        }
        Account accountFrom = accounts.get(from);
        Account accountTo = accounts.get(to);
        if (accountFrom == null || accountTo == null) {
            throw new IllegalArgumentException("Account From, account To or both are equal null");
        }
        if (accountFrom.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Not enough balance");
        }
        Account account1 = from < to ? accountFrom : accountTo;
        Account account2 = from > to ? accountFrom : accountTo;

        synchronized (account1) {
            synchronized (account2) {
                if (accountFrom.getBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("Not enough balance");
                }
                accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
                accountTo.setBalance(accountTo.getBalance().add(amount));
            }
        }
    }
}
