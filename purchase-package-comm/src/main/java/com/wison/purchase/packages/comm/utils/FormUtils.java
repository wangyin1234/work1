package com.wison.purchase.packages.comm.utils;

import com.wison.base.core.domain.vo.form.FormSelectedVO;
import com.wison.purchase.packages.comm.enums.CategoryEnum;
import com.wison.purchase.packages.comm.enums.DoneStatusEnum;
import com.wison.purchase.packages.comm.enums.ProcessStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormUtils {
    public static List<FormSelectedVO> getCategorySelect() {
        return Arrays.stream(CategoryEnum.values()).map(category ->
                new FormSelectedVO(category.getCode(), category.getValue())
        ).collect(Collectors.toList());
    }

    public static List<FormSelectedVO> getProcessStatusSelect() {
        return Arrays.stream(ProcessStatusEnum.values()).map(category ->
                new FormSelectedVO(category.getStatus(), category.getDesc())
        ).collect(Collectors.toList());
    }


    public static List<FormSelectedVO> getDoneStatusSelect() {
        return Arrays.stream(DoneStatusEnum.values()).map(category ->
                new FormSelectedVO(category.getCode(), category.getValue())
        ).collect(Collectors.toList());
    }
}
