package com.malsolo.pact.test.boot.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produced {
    private String text;
    private Date date;
    private int count;
}
