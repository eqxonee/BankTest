package com.example.bankconsumer.service;

import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;

public class FixedBackOff implements BackOff {
    public static final long DEFAULT_INTERVAL = 5000;

    /**
     * Constant value indicating an unlimited number of attempts.
     */
    public static final long UNLIMITED_ATTEMPTS = Long.MAX_VALUE;

    private long interval = DEFAULT_INTERVAL;

    private long maxAttempts = UNLIMITED_ATTEMPTS;


    /**
     * Create an instance with an interval of {@value #DEFAULT_INTERVAL}
     * ms and an unlimited number of attempts.
     */
    public FixedBackOff() {
    }

    /**
     * Create an instance.
     * @param interval the interval between two attempts
     * @param maxAttempts the maximum number of attempts
     */
    public FixedBackOff(long interval, long maxAttempts) {
        this.interval = interval;
        this.maxAttempts = maxAttempts;
    }


    /**
     * Set the interval between two attempts in milliseconds.
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * Return the interval between two attempts in milliseconds.
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     * Set the maximum number of attempts in milliseconds.
     */
    public void setMaxAttempts(long maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    /**
     * Return the maximum number of attempts in milliseconds.
     */
    public long getMaxAttempts() {
        return this.maxAttempts;
    }

    @Override
    public BackOffExecution start() {
        return new FixedBackOffExecution();
    }


    private class FixedBackOffExecution implements BackOffExecution {

        private long currentAttempts = 0;

        @Override
        public long nextBackOff() {
            this.currentAttempts++;
            if (this.currentAttempts <= getMaxAttempts()) {
                return getInterval();
            }
            else {
                return STOP;
            }
        }

        @Override
        public String toString() {
            String attemptValue = (FixedBackOff.this.maxAttempts == Long.MAX_VALUE ?
                    "unlimited" : String.valueOf(FixedBackOff.this.maxAttempts));
            return "FixedBackOff{interval=" + FixedBackOff.this.interval +
                    ", currentAttempts=" + this.currentAttempts +
                    ", maxAttempts=" + attemptValue +
                    '}';
        }
    }
}
