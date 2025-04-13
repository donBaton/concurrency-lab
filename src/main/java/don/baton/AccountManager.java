package don.baton;

import java.math.BigDecimal;

public interface AccountManager {
    long createAccount(BigDecimal amount);
    BigDecimal getBalance(long accountId);
    void transferMoney(long from, long to, BigDecimal amount);
}
