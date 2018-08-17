/*
 * Copyright (C) 2018 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.bond;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableValidator;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.currency.AdjustablePayment;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.DaysAdjustment;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.product.LegalEntityId;
import com.opengamma.strata.product.PositionInfo;
import com.opengamma.strata.product.SecurityId;
import com.opengamma.strata.product.SecurityInfo;
import com.opengamma.strata.product.TradeInfo;

/**
 * A security representing a bill.
 * <p>
 * A bill is a financial instrument that represents a unique fixed payment.
 * 
 * <h4>Price and yield</h4>
 * Strata uses <i>decimal</i> yields and prices for bills in the trade model, pricers and market data.
 * For example, a price of 99.32% is represented in Strata by 0.9932 and a yield of 1.32% is represented by 0.0132.
 */
@BeanDefinition
public final class BillSecurity
    implements LegalEntitySecurity, ImmutableBean, Serializable {

  /**
   * The standard security information.
   * <p>
   * This includes the security identifier.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final SecurityInfo info;
  /**
   * The adjustable notional payment of the bill notional, the amount must be positive.
   */
  @PropertyDefinition(validate = "notNull")
  private final AdjustablePayment notional;
  /**
   * The day count convention applicable.
   * <p>
   * The conversion from dates to a numerical value is made based on this day count.
   */
  @PropertyDefinition(validate = "notNull")
  private final DayCount dayCount;
  /**
   * Yield convention.
   * <p>
   * The convention defines how to convert from yield to price and inversely.
   */
  @PropertyDefinition(validate = "notNull")
  private final BillYieldConvention yieldConvention;
  /**
   * The legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bill.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final LegalEntityId legalEntityId;
  /**
   * The number of days between valuation date and settlement date.
   * <p>
   * It is usually one business day for US and UK bills and two days for Euroland government bills.
   */
  @PropertyDefinition(validate = "notNull")
  private final DaysAdjustment settlementDateOffset;

  //-------------------------------------------------------------------------
  @ImmutableValidator
  private void validate() {
    ArgChecker.isTrue(settlementDateOffset.getDays() >= 0, "The settlement date offset must be non-negative");
    ArgChecker.isTrue(notional.getAmount() > 0, "Notional must be strictly positive");
  }

  //-------------------------------------------------------------------------
  @Override
  public Currency getCurrency() {
    return notional.getCurrency();
  }

  @Override
  public ImmutableSet<SecurityId> getUnderlyingIds() {
    return ImmutableSet.of();
  }

  @Override
  public BillSecurity withInfo(SecurityInfo info) {
    return toBuilder().info(info).build();
  }

  //-------------------------------------------------------------------------
  @Override
  public Bill createProduct(ReferenceData refData) {
    return new Bill(getSecurityId(), notional, dayCount, yieldConvention, legalEntityId, settlementDateOffset);
  }

  @Override
  public BillTrade createTrade(TradeInfo info, double quantity, double tradePrice, ReferenceData refData) {
    return new BillTrade(info, createProduct(refData), quantity, tradePrice);
  }

  @Override
  public BillPosition createPosition(PositionInfo positionInfo, double quantity, ReferenceData refData) {
    return BillPosition.ofNet(positionInfo, createProduct(refData), quantity);
  }

  @Override
  public BillPosition createPosition(
      PositionInfo positionInfo,
      double longQuantity,
      double shortQuantity,
      ReferenceData refData) {

    return BillPosition.ofLongShort(positionInfo, createProduct(refData), longQuantity, shortQuantity);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code BillSecurity}.
   * @return the meta-bean, not null
   */
  public static BillSecurity.Meta meta() {
    return BillSecurity.Meta.INSTANCE;
  }

  static {
    MetaBean.register(BillSecurity.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static BillSecurity.Builder builder() {
    return new BillSecurity.Builder();
  }

  private BillSecurity(
      SecurityInfo info,
      AdjustablePayment notional,
      DayCount dayCount,
      BillYieldConvention yieldConvention,
      LegalEntityId legalEntityId,
      DaysAdjustment settlementDateOffset) {
    JodaBeanUtils.notNull(info, "info");
    JodaBeanUtils.notNull(notional, "notional");
    JodaBeanUtils.notNull(dayCount, "dayCount");
    JodaBeanUtils.notNull(yieldConvention, "yieldConvention");
    JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
    JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
    this.info = info;
    this.notional = notional;
    this.dayCount = dayCount;
    this.yieldConvention = yieldConvention;
    this.legalEntityId = legalEntityId;
    this.settlementDateOffset = settlementDateOffset;
    validate();
  }

  @Override
  public BillSecurity.Meta metaBean() {
    return BillSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the standard security information.
   * <p>
   * This includes the security identifier.
   * @return the value of the property, not null
   */
  @Override
  public SecurityInfo getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the adjustable notional payment of the bill notional, the amount must be positive.
   * @return the value of the property, not null
   */
  public AdjustablePayment getNotional() {
    return notional;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count convention applicable.
   * <p>
   * The conversion from dates to a numerical value is made based on this day count.
   * @return the value of the property, not null
   */
  public DayCount getDayCount() {
    return dayCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets yield convention.
   * <p>
   * The convention defines how to convert from yield to price and inversely.
   * @return the value of the property, not null
   */
  public BillYieldConvention getYieldConvention() {
    return yieldConvention;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the legal entity identifier.
   * <p>
   * This identifier is used for the legal entity that issues the bill.
   * @return the value of the property, not null
   */
  @Override
  public LegalEntityId getLegalEntityId() {
    return legalEntityId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of days between valuation date and settlement date.
   * <p>
   * It is usually one business day for US and UK bills and two days for Euroland government bills.
   * @return the value of the property, not null
   */
  public DaysAdjustment getSettlementDateOffset() {
    return settlementDateOffset;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BillSecurity other = (BillSecurity) obj;
      return JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(notional, other.notional) &&
          JodaBeanUtils.equal(dayCount, other.dayCount) &&
          JodaBeanUtils.equal(yieldConvention, other.yieldConvention) &&
          JodaBeanUtils.equal(legalEntityId, other.legalEntityId) &&
          JodaBeanUtils.equal(settlementDateOffset, other.settlementDateOffset);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(notional);
    hash = hash * 31 + JodaBeanUtils.hashCode(dayCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(yieldConvention);
    hash = hash * 31 + JodaBeanUtils.hashCode(legalEntityId);
    hash = hash * 31 + JodaBeanUtils.hashCode(settlementDateOffset);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("BillSecurity{");
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("notional").append('=').append(notional).append(',').append(' ');
    buf.append("dayCount").append('=').append(dayCount).append(',').append(' ');
    buf.append("yieldConvention").append('=').append(yieldConvention).append(',').append(' ');
    buf.append("legalEntityId").append('=').append(legalEntityId).append(',').append(' ');
    buf.append("settlementDateOffset").append('=').append(JodaBeanUtils.toString(settlementDateOffset));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BillSecurity}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<SecurityInfo> info = DirectMetaProperty.ofImmutable(
        this, "info", BillSecurity.class, SecurityInfo.class);
    /**
     * The meta-property for the {@code notional} property.
     */
    private final MetaProperty<AdjustablePayment> notional = DirectMetaProperty.ofImmutable(
        this, "notional", BillSecurity.class, AdjustablePayment.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> dayCount = DirectMetaProperty.ofImmutable(
        this, "dayCount", BillSecurity.class, DayCount.class);
    /**
     * The meta-property for the {@code yieldConvention} property.
     */
    private final MetaProperty<BillYieldConvention> yieldConvention = DirectMetaProperty.ofImmutable(
        this, "yieldConvention", BillSecurity.class, BillYieldConvention.class);
    /**
     * The meta-property for the {@code legalEntityId} property.
     */
    private final MetaProperty<LegalEntityId> legalEntityId = DirectMetaProperty.ofImmutable(
        this, "legalEntityId", BillSecurity.class, LegalEntityId.class);
    /**
     * The meta-property for the {@code settlementDateOffset} property.
     */
    private final MetaProperty<DaysAdjustment> settlementDateOffset = DirectMetaProperty.ofImmutable(
        this, "settlementDateOffset", BillSecurity.class, DaysAdjustment.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "notional",
        "dayCount",
        "yieldConvention",
        "legalEntityId",
        "settlementDateOffset");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case 1585636160:  // notional
          return notional;
        case 1905311443:  // dayCount
          return dayCount;
        case -1895216418:  // yieldConvention
          return yieldConvention;
        case 866287159:  // legalEntityId
          return legalEntityId;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BillSecurity.Builder builder() {
      return new BillSecurity.Builder();
    }

    @Override
    public Class<? extends BillSecurity> beanType() {
      return BillSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SecurityInfo> info() {
      return info;
    }

    /**
     * The meta-property for the {@code notional} property.
     * @return the meta-property, not null
     */
    public MetaProperty<AdjustablePayment> notional() {
      return notional;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DayCount> dayCount() {
      return dayCount;
    }

    /**
     * The meta-property for the {@code yieldConvention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<BillYieldConvention> yieldConvention() {
      return yieldConvention;
    }

    /**
     * The meta-property for the {@code legalEntityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LegalEntityId> legalEntityId() {
      return legalEntityId;
    }

    /**
     * The meta-property for the {@code settlementDateOffset} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DaysAdjustment> settlementDateOffset() {
      return settlementDateOffset;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return ((BillSecurity) bean).getInfo();
        case 1585636160:  // notional
          return ((BillSecurity) bean).getNotional();
        case 1905311443:  // dayCount
          return ((BillSecurity) bean).getDayCount();
        case -1895216418:  // yieldConvention
          return ((BillSecurity) bean).getYieldConvention();
        case 866287159:  // legalEntityId
          return ((BillSecurity) bean).getLegalEntityId();
        case 135924714:  // settlementDateOffset
          return ((BillSecurity) bean).getSettlementDateOffset();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code BillSecurity}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<BillSecurity> {

    private SecurityInfo info;
    private AdjustablePayment notional;
    private DayCount dayCount;
    private BillYieldConvention yieldConvention;
    private LegalEntityId legalEntityId;
    private DaysAdjustment settlementDateOffset;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(BillSecurity beanToCopy) {
      this.info = beanToCopy.getInfo();
      this.notional = beanToCopy.getNotional();
      this.dayCount = beanToCopy.getDayCount();
      this.yieldConvention = beanToCopy.getYieldConvention();
      this.legalEntityId = beanToCopy.getLegalEntityId();
      this.settlementDateOffset = beanToCopy.getSettlementDateOffset();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case 1585636160:  // notional
          return notional;
        case 1905311443:  // dayCount
          return dayCount;
        case -1895216418:  // yieldConvention
          return yieldConvention;
        case 866287159:  // legalEntityId
          return legalEntityId;
        case 135924714:  // settlementDateOffset
          return settlementDateOffset;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          this.info = (SecurityInfo) newValue;
          break;
        case 1585636160:  // notional
          this.notional = (AdjustablePayment) newValue;
          break;
        case 1905311443:  // dayCount
          this.dayCount = (DayCount) newValue;
          break;
        case -1895216418:  // yieldConvention
          this.yieldConvention = (BillYieldConvention) newValue;
          break;
        case 866287159:  // legalEntityId
          this.legalEntityId = (LegalEntityId) newValue;
          break;
        case 135924714:  // settlementDateOffset
          this.settlementDateOffset = (DaysAdjustment) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public BillSecurity build() {
      return new BillSecurity(
          info,
          notional,
          dayCount,
          yieldConvention,
          legalEntityId,
          settlementDateOffset);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the standard security information.
     * <p>
     * This includes the security identifier.
     * @param info  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder info(SecurityInfo info) {
      JodaBeanUtils.notNull(info, "info");
      this.info = info;
      return this;
    }

    /**
     * Sets the adjustable notional payment of the bill notional, the amount must be positive.
     * @param notional  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder notional(AdjustablePayment notional) {
      JodaBeanUtils.notNull(notional, "notional");
      this.notional = notional;
      return this;
    }

    /**
     * Sets the day count convention applicable.
     * <p>
     * The conversion from dates to a numerical value is made based on this day count.
     * @param dayCount  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder dayCount(DayCount dayCount) {
      JodaBeanUtils.notNull(dayCount, "dayCount");
      this.dayCount = dayCount;
      return this;
    }

    /**
     * Sets yield convention.
     * <p>
     * The convention defines how to convert from yield to price and inversely.
     * @param yieldConvention  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder yieldConvention(BillYieldConvention yieldConvention) {
      JodaBeanUtils.notNull(yieldConvention, "yieldConvention");
      this.yieldConvention = yieldConvention;
      return this;
    }

    /**
     * Sets the legal entity identifier.
     * <p>
     * This identifier is used for the legal entity that issues the bill.
     * @param legalEntityId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder legalEntityId(LegalEntityId legalEntityId) {
      JodaBeanUtils.notNull(legalEntityId, "legalEntityId");
      this.legalEntityId = legalEntityId;
      return this;
    }

    /**
     * Sets the number of days between valuation date and settlement date.
     * <p>
     * It is usually one business day for US and UK bills and two days for Euroland government bills.
     * @param settlementDateOffset  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder settlementDateOffset(DaysAdjustment settlementDateOffset) {
      JodaBeanUtils.notNull(settlementDateOffset, "settlementDateOffset");
      this.settlementDateOffset = settlementDateOffset;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(224);
      buf.append("BillSecurity.Builder{");
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("notional").append('=').append(JodaBeanUtils.toString(notional)).append(',').append(' ');
      buf.append("dayCount").append('=').append(JodaBeanUtils.toString(dayCount)).append(',').append(' ');
      buf.append("yieldConvention").append('=').append(JodaBeanUtils.toString(yieldConvention)).append(',').append(' ');
      buf.append("legalEntityId").append('=').append(JodaBeanUtils.toString(legalEntityId)).append(',').append(' ');
      buf.append("settlementDateOffset").append('=').append(JodaBeanUtils.toString(settlementDateOffset));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}