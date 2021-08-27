package com.test.codediff.exceptions;


import com.test.codediff.enums.StatusCode;
import lombok.Data;

/**
 * @author wl
 */
@Data
public class GitException extends RuntimeException{

    private StatusCode statusCode;

    public GitException(){};
    public GitException(StatusCode statusCode){
        super(statusCode.getDesc());
    }
    public GitException(String msg){
        super(msg);
    }

}
