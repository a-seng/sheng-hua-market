package com.tian.asenghuamarket.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class Result <T>implements Serializable {
    @Serial
    private static final long serivalVersionUID=1L;
    private int resultCode;
    private String message;
    private T data;

    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
}
