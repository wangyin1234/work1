package com.wison.purchase.packages.module.main.utils;

import com.wison.base.core.domain.vo.form.FormSelectedVO;
import com.wison.purchase.packages.module.main.domain.CompanyPurPackageT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormUtil {
    public static List<FormSelectedVO> getDisciplineSelect(List<CompanyPurPackageT> list) {
        List<FormSelectedVO> selectList = new ArrayList<>();
        list.forEach(companyPurPackageT -> {
            if (selectList.stream().noneMatch(s -> s.getRecordKey().equals(companyPurPackageT.getDisciplineId()))) {
                selectList.add(new FormSelectedVO(companyPurPackageT.getDisciplineId(), companyPurPackageT.getDisciplineName()));
            }
        });
        return selectList;
    }
}
