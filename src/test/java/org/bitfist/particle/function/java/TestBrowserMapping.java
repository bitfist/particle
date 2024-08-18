package org.bitfist.particle.function.java;

@BrowserMapping(prefix = "test")
public class TestBrowserMapping {

    private int callableCalled = 0;
    private int consumerCalled = 0;
    private int producerCalled = 0;
    private int functionCalled = 0;

    private String consumed = null;

    private Byte byteValue = null;
    private Short shortValue = null;
    private Integer integerValue = null;
    private Long longValue = null;
    private Float floatValue = null;
    private Double doubleValue = null;

    private TestObject testObject = null;

    @BrowserMapping
    public void aCallable() {
        callableCalled++;
    }

    @BrowserMapping
    public String aProducer() {
        producerCalled++;
        return "blah";
    }

    @BrowserMapping
    public void aConsumer(String value) {
        consumed = value;
        consumerCalled++;
    }

    @BrowserMapping
    public String aFunction(String value) {
        functionCalled++;
        return new StringBuilder(value).reverse().toString();
    }

    @BrowserMapping
    public void aByteConsumer(Byte value) {
        byteValue = value;
    }

    @BrowserMapping
    public void aShortConsumer(Short value) {
        shortValue = value;
    }

    @BrowserMapping
    public void anIntegerConsumer(Integer value) {
        integerValue = value;
    }

    @BrowserMapping
    public void aLongConsumer(Long value) {
        longValue = value;
    }

    @BrowserMapping
    public void aFloatConsumer(Float value) {
        floatValue = value;
    }

    @BrowserMapping
    public void aDoubleConsumer(Double value) {
        doubleValue = value;
    }

    @BrowserMapping
    public void anObjectConsumer(TestObject value) {
        testObject = value;
    }

    // Getter methods for the variables
    public int getCallableCalled() {
        return callableCalled;
    }

    public int getConsumerCalled() {
        return consumerCalled;
    }

    public int getProducerCalled() {
        return producerCalled;
    }

    public int getFunctionCalled() {
        return functionCalled;
    }

    public String getConsumed() {
        return consumed;
    }

    public Byte getByteValue() {
        return byteValue;
    }

    public Short getShortValue() {
        return shortValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public TestObject getTestObject() {
        return testObject;
    }
}
