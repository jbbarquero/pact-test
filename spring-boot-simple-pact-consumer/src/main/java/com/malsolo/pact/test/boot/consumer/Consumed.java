package com.malsolo.pact.test.boot.consumer;

import lombok.Data;

import java.util.Date;

@Data
public class Consumed {
    private final String text;
    private final Date date;
    private final int count;
    private final boolean valid;
}
