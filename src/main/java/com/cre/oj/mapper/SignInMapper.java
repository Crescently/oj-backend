package com.cre.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cre.oj.model.entity.SignInRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignInMapper extends BaseMapper<SignInRecord> {

}
