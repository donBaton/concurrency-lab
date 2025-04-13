package don.baton.task5;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final long id;
    private BigDecimal balance;
    private ReentrantLock lock = new ReentrantLock();

    public Account(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance);
    }
}
