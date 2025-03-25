package com.cre.springbootinit.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id;
}
