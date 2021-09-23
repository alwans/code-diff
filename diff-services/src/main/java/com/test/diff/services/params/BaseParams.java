package com.test.diff.services.params;


import lombok.Data;

import java.io.Serializable;

@Data
public class BaseParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page = 1;

    private Integer size = 10;

}
