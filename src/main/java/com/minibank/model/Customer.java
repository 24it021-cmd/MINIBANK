package com.minibank.model;

import com.minibank.model.annotation.MaxLength;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong CUSTOMER_COUNTER = new AtomicLong(100);

    private final String customerId;
    @MaxLength(80)
    private final String name;
    private final String email;
    private final String mobile;
    private final Address address;

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = "CUST" + CUSTOMER_COUNTER.incrementAndGet();
        this.name = Objects.requireNonNull(name, "name");
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
    }

    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }
    }
}