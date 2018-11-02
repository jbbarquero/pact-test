package com.malsolo.pact.test.boot.producer;

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
