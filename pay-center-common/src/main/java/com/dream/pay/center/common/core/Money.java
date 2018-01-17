package com.dream.pay.center.common.core;

import com.dream.pay.enums.CurrencyCode;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * 收单系统统一money类,所有money操作统一使用此类,不可以直接操作 <p>默认构造返回0分,CNY币种</p>
 * Created by mengzhenbin on 16/6/24.
 */
@Data
public class Money implements Serializable {

    private static final long serialVersionUID = 4128095832905511318L;

    public static final String DEFAULT_CURRENCY_CODE = "CNY";

    // 大于
    public static final int BIGGER = 1;

    // 等于
    public static final int EQUAL = 0;

    // 小于
    public static final int SMALLER = -1;

    /**
     * 币种
     */
    private Currency currency;

    /**
     * 分
     */
    private long cent;

    public Money() {
        this(Currency.getInstance(DEFAULT_CURRENCY_CODE), 0);
    }

    public Money(long cent) {
        this(Currency.getInstance(DEFAULT_CURRENCY_CODE), cent);
    }

    public Money(Currency currency, long cent) {
        this.currency = currency;
        this.cent = cent;
    }

    /**
     * 获取币种数字
     *
     * @return 币种数字
     */
    public int getCurrencyNum() {
        return Integer.parseInt(CurrencyCode.getEnumByAlpha(this.getCurrency().getCurrencyCode()).getNum());
    }

    /**
     * 获取币种编码
     *
     * @return 币种编码
     */
    public String getCurrentCode() {
        return this.getCurrency().getCurrencyCode();
    }


    /**
     * 断言本货币对象与另一货币对象是否具有相同的币种。
     * 如果本货币对象与另一货币对象具有相同的币种，则方法返回。
     * 否则抛出运行时异常<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 另一货币对象
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    protected void assertSameCurrencyAs(Money other) {

        if (null == other) {
            return;
        }

        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    /**
     * 货币累加。
     * 如果两货币币种相同，则本货币对象的金额等于两货币对象金额之和，并返回本货币对象的引用。
     * 如果两货币对象币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为加数的货币对象。
     * @return 累加后的本货币对象。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public Money addTo(Money other) {

        if (null == other) {
            return this;
        }

        assertSameCurrencyAs(other);

        this.cent += other.cent;

        return this;
    }

    /**
     * 货币累加，但是不修改原有对象，即不会影响原有数据，而是新建了一个新的money对象
     * 目前仅在退款场景使用
     * 如果两货币币种相同，则本货币对象的金额等于两货币对象金额之和，。
     * 如果两货币对象币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为加数的货币对象。
     * @return 累加后的本货币 新对象。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public Money addToWithNewObj(Money other) {

        Money resultMoney = new Money();

        if (null == other) {
            resultMoney.cent = this.cent;
            return resultMoney;
        }

        assertSameCurrencyAs(other);

        resultMoney.cent = this.cent + other.cent;

        return resultMoney;
    }


    /**
     * 货币相减,注意不修改原有对象，即不会影响原有数据，而是新建了一个新的money对象
     * <p>
     * 目前仅在退款场景使用
     *
     * @param other 作为减数的对象
     * @return 减后的本货币对象
     */
    public Money subToWithNewObj(Money other) {

        Money resultMoney = new Money();

        if (null == other) {
            resultMoney.cent = this.cent;
            return resultMoney;
        }

        assertSameCurrencyAs(other);

        resultMoney.cent = this.cent - other.cent;

        return resultMoney;
    }


    public static void main(String[] args) {
        System.out.println(new Money(2).equals(new Money(Currency.getInstance("USD"), 2)));
    }


    public boolean greaterOrEqualThan(Money other) {
        if (null == other) {
            return true;
        }
        assertSameCurrencyAs(other);
        return this.cent >= other.cent;
    }

    public boolean equal(Money other) {
        if (null == other) {
            return true;
        }
        assertSameCurrencyAs(other);
        return this.cent == other.cent;
    }

    public boolean greaterThan(Money other) {
        if (null == other) {
            return true;
        }
        assertSameCurrencyAs(other);
        return this.cent > other.cent;
    }

    public boolean lessThan(Money other) {
        if (null == other) {
            return false;
        }
        assertSameCurrencyAs(other);
        return this.cent < other.cent;
    }

    public BigDecimal getYuan() {

        String currencyCode = this.getCurrency().getCurrencyCode();

        /** 获取币种枚举 */
        CurrencyCode currencyEnum = CurrencyCode.getEnumByNum(currencyCode);
        if (currencyEnum == null) {
            currencyEnum = CurrencyCode.getEnumByAlpha(currencyCode);
        }
        if (currencyEnum == null) {
            throw new IllegalArgumentException("不支持的币种, currencyCode=" + currencyCode);
        }
        currencyCode = currencyEnum.getAlpha();
        Currency currency = Currency.getInstance(currencyCode);
        int digits = currency.getDefaultFractionDigits();

        BigDecimal amountDecimal = new BigDecimal(this.getCent());

        return amountDecimal.movePointLeft(digits);

    }


}
