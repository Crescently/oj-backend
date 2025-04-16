package com.cre.oj.model.request.user;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserUpdateAvatarRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @URL
    private String avatarUrl;

    private Long userId;
}
