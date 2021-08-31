package com.test.diff.services.exceptions;


import com.test.diff.services.enums.StatusCode;
import lombok.Data;

/**
 * @author wl
 */
@Data
public class FileException extends RuntimeException{

    private StatusCode statusCode;

    public FileException(){}

    public FileException(StatusCode statusCode){
        super(statusCode.getDesc());
        this.statusCode = statusCode;
    }

    public FileException(String msg){
        super(msg);
    }
}
