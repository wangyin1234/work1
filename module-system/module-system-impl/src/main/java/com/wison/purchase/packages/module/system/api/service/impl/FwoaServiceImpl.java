package com.wison.purchase.packages.module.system.api.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.wison.cology.api.service.CologyProcessDubboService;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import com.wison.purchase.packages.comm.utils.StringUtils;
import com.wison.purchase.packages.module.system.api.domain.OaInfo;
import com.wison.purchase.packages.module.system.api.domain.bo.FlowLogTBo;
import com.wison.purchase.packages.module.system.api.domain.response.FwoaUserIdRes;
import com.wison.purchase.packages.module.system.api.service.FwoaService;
import com.wison.purchase.packages.module.system.domain.FlowLogT;
import com.wison.purchase.packages.module.system.domain.request.*;
import com.wison.purchase.packages.module.system.mapper.FlowLogTMapper;
import com.wison.purchase.packages.module.system.utils.FwoaUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class FwoaServiceImpl implements FwoaService {
    private final ExecutorService executorService;
    private final FlowLogTMapper flowLogTMapper;
    @DubboReference
    CologyProcessDubboService cologyProcessDubboService;

    @Override
    public <T extends OaInfo> String create(T t, Class<T> clazz) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        List<WorkflowRequestTableField> workflowRequestTableFields = new ArrayList<>();
        List<WorkflowDetailTable> workflowDetailTables = new ArrayList<>();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) continue;
            boolean canAccess = field.isAccessible();
            if (!canAccess) field.setAccessible(true);
            if (field.get(t) instanceof String) {
                WorkflowRequestTableField workflowRequestTableField = WorkflowRequestTableField.builder().edit(true).view(true).fieldValue((String) field.get(t)).fieldName(field.getName()).build();
                workflowRequestTableFields.add(workflowRequestTableField);
            }
            if (field.get(t) instanceof List<?>) {
                List<?> list = (List<?>) field.get(t);
                if (!list.isEmpty()) {
                    workflowDetailTables.add(createDetail(list));
                }

            }
            field.setAccessible(canAccess);
        }
        return FwoaUtils.doCreate(t, workflowRequestTableFields, workflowDetailTables);
    }

    private <T> WorkflowDetailTable createDetail(List<T> list) {
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        List<WorkflowRequestTableRecord> workflowRequestTableRecords = IntStream.range(0, list.size()).mapToObj(i -> {
            try {
                T t = list.get(i);
                List<WorkflowRequestTableField> workflowRequestTableFields = new ArrayList<>();
                for (Field field : fields) {
                    if ("serialVersionUID".equals(field.getName())) continue;
                    boolean canAccess = field.isAccessible();
                    if (!canAccess) field.setAccessible(true);
                    WorkflowRequestTableField workflowRequestTableField = WorkflowRequestTableField.builder().edit(true).view(true).fieldValue((String) field.get(t)).fieldName(field.getName()).build();
                    workflowRequestTableFields.add(workflowRequestTableField);
                    field.setAccessible(canAccess);
                }
                return WorkflowRequestTableRecord.builder().recordOrder(String.valueOf(i + 1)).workflowRequestTableFields(new WorkflowRequestTableFields(workflowRequestTableFields)).build();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return WorkflowDetailTable.builder().tableDBName("").tableRequestRecordsInfo(new TableRequestRecordsInfo(workflowRequestTableRecords)).build();
    }

    @Override
    public String getUserId() {
        FwoaUserIdRes fwoaUserIdRes = FwoaUserIdRes.builder().loginName(UserInfoHolder.getUserInfo().getLoginName()).userName("创建人").build();
        return userIdReturn(fwoaUserIdRes).getUserId();
    }

    @Override
    public String getUserId(FwoaUserIdRes fwoaUserIdRes) {
        return userIdReturn(fwoaUserIdRes).getUserId();
    }

    @Override
    public Map<String, FwoaUserIdRes> getUserId(List<FwoaUserIdRes> list) {
        List<CompletableFuture<FwoaUserIdRes>> futureList = list.stream().map(l ->
                CompletableFuture.supplyAsync(() -> userIdReturn(l), executorService)
        ).collect(Collectors.toList());
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        Map<String, FwoaUserIdRes> map = new HashMap<>();
        futureList.forEach(future -> {
            FwoaUserIdRes res = future.join();
            map.put(res.getLoginName(), res);
        });
        return map;
    }

    @Override
    public void delete(FlowLogTBo bo) {
        try {
            cologyProcessDubboService.deleteProcess(UserInfoHolder.getUserInfo().getLoginName(), bo.getRequestId());
            FlowLogT entity = BeanUtil.toBean(bo, FlowLogT.class);
            flowLogTMapper.insert(entity);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private FwoaUserIdRes userIdReturn(FwoaUserIdRes fwoaUserIdRes) {
        String userId = FwoaUtils.getUserId(fwoaUserIdRes.getLoginName());
        fwoaUserIdRes.setError(!StringUtils.startWithNumber(userId));
        fwoaUserIdRes.setUserId(userId);
        return fwoaUserIdRes;
    }
}
