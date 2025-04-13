package don.baton.task5;

import don.baton.AccountManager;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ReentrantLockAccountManager implements AccountManager {

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

        account1.lock();
        try {
            account2.lock();
            try {
                accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
                accountTo.setBalance(accountTo.getBalance().add(amount));
            } finally {
                account2.unlock();
            }
        } finally {
            account1.unlock();
        }
    }
}
