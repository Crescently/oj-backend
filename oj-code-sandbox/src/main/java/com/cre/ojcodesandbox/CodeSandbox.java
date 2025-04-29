package com.cre.ojcodesandbox;


import com.cre.ojcodesandbox.model.ExecuteCodeRequest;
import com.cre.ojcodesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
