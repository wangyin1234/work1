package com.wison.purchase.packages.comm.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.wison.purchase.packages.comm.utils.MapstructUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 * @author Lion Li
 * @since 2021-05-13
 */
@SuppressWarnings("unchecked")
public interface BaseMapperPlus<T, V> extends BaseMapper<T> {

    Log log = LogFactory.getLog(BaseMapperPlus.class);

    /**
     * 获取当前类的泛型类型 V 的 Class 对象
     * <p>
     * 该方法使用反射机制从当前类（继承自 BaseMapperPlus 类）的泛型参数中获取第一个泛型类型 V 的 Class 对象
     *
     * @return 当前类的泛型类型 V 的 Class 对象
     */
    default Class<V> currentVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 1);
    }

    /**
     * 获取当前类的泛型类型 T 的 Class 对象
     * <p>
     * 该方法使用反射机制从当前类（继承自 BaseMapperPlus 类）的泛型参数中获取第一个泛型类型 T 的 Class 对象
     *
     * @return 当前类的泛型类型 T 的 Class 对象
     */
    default Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 0);
    }

    /**
     * 使用默认的查询条件查询并返回结果列表
     *
     * @return 返回查询结果的列表
     */
    default List<T> selectList() {
        return this.selectList(new QueryWrapper<>());
    }

    /**
     * 批量插入实体对象集合
     *
     * @param entityList 实体对象集合
     * @return 插入操作是否成功的布尔值
     */
    default boolean insertBatch(Collection<T> entityList) {
        Db.saveBatch(entityList);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 批量根据ID更新实体对象集合
     *
     * @param entityList 实体对象集合
     * @return 更新操作是否成功的布尔值
     */
    default boolean updateBatchById(Collection<T> entityList) {
        Db.updateBatchById(entityList);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 批量插入或更新实体对象集合
     *
     * @param entityList 实体对象集合
     * @return 插入或更新操作是否成功的布尔值
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList) {
        Db.saveOrUpdateBatch(entityList);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 批量插入实体对象集合并指定批处理大小
     *
     * @param entityList 实体对象集合
     * @param batchSize  批处理大小
     * @return 插入操作是否成功的布尔值
     */
    default boolean insertBatch(Collection<T> entityList, int batchSize) {
        Db.saveBatch(entityList, batchSize);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 批量根据ID更新实体对象集合并指定批处理大小
     *
     * @param entityList 实体对象集合
     * @param batchSize  批处理大小
     * @return 更新操作是否成功的布尔值
     */
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        Db.updateBatchById(entityList, batchSize);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 批量插入或更新实体对象集合并指定批处理大小
     *
     * @param entityList 实体对象集合
     * @param batchSize  批处理大小
     * @return 插入或更新操作是否成功的布尔值
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList, int batchSize) {
        Db.saveOrUpdateBatch(entityList, batchSize);
        // 临时解决 新版本 mp 插入状态判断错误问题
        return true;
    }

    /**
     * 根据ID查询单个VO对象
     *
     * @param id 主键ID
     * @return 查询到的单个VO对象
     */
    default V selectVoById(Serializable id) {
        return selectVoById(id, this.currentVoClass());
    }

    /**
     * 根据ID查询单个VO对象并将其转换为指定的VO类
     *
     * @param id      主键ID
     * @param voClass 要转换的VO类的Class对象
     * @param <C>     VO类的类型
     * @return 查询到的单个VO对象，经过转换为指定的VO类后返回
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    /**
     * 根据ID集合批量查询VO对象列表
     *
     * @param idList 主键ID集合
     * @return 查询到的VO对象列表
     */
    default List<V> selectVoBatchIds(Collection<? extends Serializable> idList) {
        return selectVoBatchIds(idList, this.currentVoClass());
    }

    /**
     * 根据ID集合批量查询实体对象列表，并将其转换为指定的VO对象列表
     *
     * @param idList  主键ID集合
     * @param voClass 要转换的VO类的Class对象
     * @param <C>     VO类的类型
     * @return 查询到的VO对象列表，经过转换为指定的VO类后返回
     */
    default <C> List<C> selectVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        List<T> list = this.selectBatchIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    /**
     * 根据查询条件Map查询VO对象列表
     *
     * @param map 查询条件Map
     * @return 查询到的VO对象列表
     */
    default List<V> selectVoByMap(Map<String, Object> map) {
        return selectVoByMap(map, this.currentVoClass());
    }

    /**
     * 根据查询条件Map查询实体对象列表，并将其转换为指定的VO对象列表
     *
     * @param map     查询条件Map
     * @param voClass 要转换的VO类的Class对象
     * @param <C>     VO类的类型
     * @return 查询到的VO对象列表，经过转换为指定的VO类后返回
     */
    default <C> List<C> selectVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = this.selectByMap(map);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    /**
     * 根据条件查询单个VO对象
     *
     * @param wrapper 查询条件Wrapper
     * @return 查询到的单个VO对象
     */
    default V selectVoOne(Wrapper<T> wrapper) {
        return selectVoOne(wrapper, this.currentVoClass());
    }

    /**
     * 根据条件查询单个VO对象，并指定返回的VO对象的类型
     *
     * @param wrapper 查询条件Wrapper
     * @param voClass 返回的VO对象的Class对象
     * @param <C>     返回的VO对象的类型
     * @return 查询到的单个VO对象，经过类型转换为指定的VO类后返回
     */
    default <C> C selectVoOne(Wrapper<T> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    /**
     * 查询所有VO对象列表
     *
     * @return 查询到的VO对象列表
     */
    default List<V> selectVoList() {
        return selectVoList(new QueryWrapper<>(), this.currentVoClass());
    }

    /**
     * 根据条件查询VO对象列表
     *
     * @param wrapper 查询条件Wrapper
     * @return 查询到的VO对象列表
     */
    default List<V> selectVoList(Wrapper<T> wrapper) {
        return selectVoList(wrapper, this.currentVoClass());
    }

    /**
     * 根据条件查询实体对象列表，并将其转换为指定的VO对象列表
     *
     * @param wrapper 查询条件Wrapper
     * @param voClass 要转换的VO类的Class对象
     * @param <C>     VO类的类型
     * @return 查询到的VO对象列表，经过转换为指定的VO类后返回
     */
    default <C> List<C> selectVoList(Wrapper<T> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    /**
     * 根据条件分页查询VO对象列表
     *
     * @param page    分页信息
     * @param wrapper 查询条件Wrapper
     * @return 查询到的VO对象分页列表
     */
    default <P extends IPage<V>> P selectVoPage(IPage<T> page, Wrapper<T> wrapper) {
        return selectVoPage(page, wrapper, this.currentVoClass());
    }

    /**
     * 根据条件分页查询实体对象列表，并将其转换为指定的VO对象分页列表
     *
     * @param page    分页信息
     * @param wrapper 查询条件Wrapper
     * @param voClass 要转换的VO类的Class对象
     * @param <C>     VO类的类型
     * @param <P>     VO对象分页列表的类型
     * @return 查询到的VO对象分页列表，经过转换为指定的VO类后返回
     */
    default <C, P extends IPage<C>> P selectVoPage(IPage<T> page, Wrapper<T> wrapper, Class<C> voClass) {
        // 根据条件分页查询实体对象列表
        List<T> list = this.selectPage(page, wrapper).getRecords();
        // 创建一个新的VO对象分页列表，并设置分页信息
        IPage<C> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(list)) {
            return (P) voPage;
        }
        voPage.setRecords(MapstructUtils.convert(list, voClass));
        return (P) voPage;
    }

    /**
     * 根据条件查询符合条件的对象，并将其转换为指定类型的对象列表
     *
     * @param wrapper 查询条件Wrapper
     * @param mapper  转换函数，用于将查询到的对象转换为指定类型的对象
     * @param <C>     要转换的对象的类型
     * @return 查询到的符合条件的对象列表，经过转换为指定类型的对象后返回
     */
    default <C> List<C> selectObjs(Wrapper<T> wrapper, Function<? super Object, C> mapper) {
        return this.selectObjs(wrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

}
