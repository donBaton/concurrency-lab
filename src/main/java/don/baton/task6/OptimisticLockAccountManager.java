package don.baton.task6;

import don.baton.AccountManager;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class OptimisticLockAccountManager implements AccountManager {

    Map<Long, AtomicReference<Account>> accounts = new ConcurrentHashMap<>();
    AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public long createAccount(BigDecimal amount) {
        long accountId = idGenerator.getAndIncrement();
        Account account = new Account(accountId, amount, 0);
        accounts.put(accountId, new AtomicReference<>(account));
        return accountId;
    }

    @Override
    public BigDecimal getBalance(long accountId) {
        return accounts.get(accountId).get().balance();
    }

    @Override
    public void transferMoney(long from, long to, BigDecimal amount) {
        if (from == to) {
            throw new IllegalArgumentException("From and To both are equal");
        }
        AtomicReference<Account> accountFrom = accounts.get(from);
        AtomicReference<Account> accountTo = accounts.get(to);
        if (accountFrom == null || accountTo == null) {
            throw new IllegalArgumentException("Account From, account To or both are equal null");
        }
        if (accountFrom.get().balance().compareTo(amount) < 0) {
            throw new RuntimeException("Not enough balance");
        }
        while (true) {
            AtomicReference<Account> accountFromAtomicReference = accounts.get(from);
            Account accountFromOld = accountFromAtomicReference.get();
            if (accountFromOld.balance().compareTo(amount) < 0) {
                throw new RuntimeException("Not enough balance");
            }
            Account accountFromCopy = new Account(
                    from,
                    accountFromOld.balance().subtract(amount),
                    accountFromOld.version() + 1
            );
            if (accountFromAtomicReference.compareAndSet(accountFromOld, accountFromCopy)) {
                break;
            }
        }
        while (true) {
            AtomicReference<Account> accountToAtomicReference = accounts.get(to);
            Account accountToOld = accountToAtomicReference.get();
            Account accountToCopy = new Account(
                    to,
                    accountToOld.balance().add(amount),
                    accountToOld.version() + 1
            );
            if (accountToAtomicReference.compareAndSet(accountToOld, accountToCopy)) {
                break;
            }
        }
    }
}
