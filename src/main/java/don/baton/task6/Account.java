package don.baton.task6;

import java.math.BigDecimal;

public record Account(long id, BigDecimal balance, long version) {
}
