package com.cre.oj.controller;


import com.cre.oj.common.BaseResponse;
import com.cre.oj.model.entity.SignInRecord;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.user.IdRequest;
import com.cre.oj.service.SignInService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/sign-in")
public class SignInController {

    @Resource
    private SignInService signInService;

    @Resource
    private UserService userService;

    @PostMapping("/post")
    public BaseResponse signIn(@RequestBody IdRequest idRequest) {
        boolean success = signInService.signIn(idRequest.getUserId());
        return BaseResponse.success(success);
    }

    @GetMapping("/get")
    public BaseResponse<List<SignInRecord>> getSignedDates(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return BaseResponse.success(signInService.getSignedDates(loginUser.getId()));
    }
}
