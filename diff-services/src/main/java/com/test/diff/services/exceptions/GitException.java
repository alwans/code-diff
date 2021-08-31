package com.test.diff.services.exceptions;



import com.test.diff.services.enums.StatusCode;
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
        this.statusCode = statusCode;
    }
    public GitException(String msg){
        super(msg);
    }

}
