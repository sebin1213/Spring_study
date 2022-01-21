package com.example.shop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String address;
    private String detail_address;

    protected Address() {
    }
    public Address(String address, String detail_address) {
        this.address = address;
        this.detail_address = detail_address;
    }
}
