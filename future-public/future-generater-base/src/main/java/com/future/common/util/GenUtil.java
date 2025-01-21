package com.future.common.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.common.util.JsonUtil;
import com.future.common.util.visiual.PlatformKeyConsts;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class GenUtil {
    /**
     * 字段说明
     */
    private String fieldName;
    /**
     * 运算符
     */
    private String operator;
    /**
     * 逻辑拼接符号
     */
    private String logic;
    /**
     * 组件标识
     */
    private String futureKey;
    /**
     * 字段key
     */
    private String field;
    /**
     * 自定义的值
     */
    private String fieldValue;
    /**
     * 自定义的值2
     */
    private String fieldValue2;

    private List<String> selectIgnore;

    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 日期格式
     */
    private String format;
    /**
     * 数字精度
     */
    private String precision;


    /**
     * @param wrapper wrapper对象
     * @param fieldDb 数据库字段名实际包括前缀
     * @return
     */
    public QueryWrapper<?> solveValue(QueryWrapper<?> wrapper, String fieldDb) {
        MyType myType = myControl(futureKey);
        if ("||".equals(logic)) {
            wrapper.or();
        }
        if (fieldValue == null) {
            fieldValue = "";
        }
        try {
            ArrayList splitKey = new ArrayList<String>() {{
                add(PlatformKeyConsts.DATE);
                add(PlatformKeyConsts.TIME);
                add(PlatformKeyConsts.NUM_INPUT);
                add(PlatformKeyConsts.CREATETIME);
                add(PlatformKeyConsts.MODIFYTIME);
            }};
            if (splitKey.contains(futureKey) && "between".equals(operator)) {
                List<String> data = JsonUtil.getJsonToList(fieldValue, String.class);
                fieldValue = data.get(0);
                fieldValue2 = data.get(1);
            }


            selectIgnore = new ArrayList<String>() {{
                add(PlatformKeyConsts.COMSELECT);
                add(PlatformKeyConsts.ADDRESS);
                add(PlatformKeyConsts.CASCADER);
                add(PlatformKeyConsts.CHECKBOX);
                add(PlatformKeyConsts.DEPSELECT);
            }};

            myType.judge(wrapper, fieldDb);
            return wrapper;
        } catch (Exception e) {
            return wrapper;
        }

    }

    /**
     * 判断控件的所属类型
     *
     * @param futureKey 控件标识
     * @return 控件类型
     */
    public MyType myControl(String futureKey) {
        MyType myType = null;
        switch (futureKey) {
            /** 基础 */
            case PlatformKeyConsts.COM_INPUT:
            case PlatformKeyConsts.TEXTAREA:
            case PlatformKeyConsts.BILLRULE:
            case PlatformKeyConsts.POPUPTABLESELECT:
            case PlatformKeyConsts.RELATIONFORM:
            case PlatformKeyConsts.RELATIONFORM_ATTR:
            case PlatformKeyConsts.POPUPSELECT:
            case PlatformKeyConsts.POPUPSELECT_ATTR:
                myType = new BasicControl();
                break;
            // 数字类型
            case PlatformKeyConsts.CALCULATE:
            case PlatformKeyConsts.NUM_INPUT:
                myType = new NumControl();
                break;
            // 日期类型
            case PlatformKeyConsts.DATE:
            case PlatformKeyConsts.CREATETIME:
            case PlatformKeyConsts.MODIFYTIME:
                myType = new DateControl();
                break;
            // 时间类型
            case PlatformKeyConsts.TIME:
                myType = new TimeControl();
                break;
            // 下拉类型
            default:
                myType = new SelectControl();
        }
        return myType;
    }

    public void getNullWrapper(QueryWrapper<?> wrapper, String fieldDb) {
        if ("||".equals(logic)) {
            wrapper.or(t -> {
                t.isNull(fieldDb);
                t.or().eq(fieldDb, "");
                t.or().eq(fieldDb, "[]");
            });
        } else {
            wrapper.and(t -> {
                t.isNull(fieldDb);
                t.or().eq(fieldDb, "");
                t.or().eq(fieldDb, "[]");
            });
        }
    }

    private void getNotNullWrapper(QueryWrapper<?> wrapper, String fieldDb) {
        if ("||".equals(logic)) {
            wrapper.or(t -> {
                t.isNotNull(fieldDb);
                t.ne(fieldDb, "");
                t.ne(fieldDb, "[]");
            });
        } else {
            wrapper.and(t -> {
                t.isNotNull(fieldDb);
                t.ne(fieldDb, "");
                t.ne(fieldDb, "[]");
            });
        }
    }

    /**
     * 基础类型
     */
    class BasicControl extends MyType {

        @Override
        void judge(QueryWrapper<?> wrapper, String fieldDb) {
            switch (operator) {
                case "null":
                    getNullWrapper(wrapper, fieldDb);
                    break;
                case "notNull":
                    getNotNullWrapper(wrapper, fieldDb);
                    break;
                case "==":
                    wrapper.eq(fieldDb, fieldValue);
                    break;
                case "<>":
                    wrapper.ne(fieldDb, fieldValue);
                    break;
                case "like":
                    wrapper.like(fieldDb, fieldValue);
                    break;
                case "notLike":
                    wrapper.notLike(fieldDb, fieldValue);
                    break;

            }
        }
    }

    class NumControl extends MyType {


        @Override
        void judge(QueryWrapper<?> wrapper, String fieldDb) {
            BigDecimal num1 = new BigDecimal(fieldValue);
            BigDecimal num2 = null;
            if (fieldValue2 != null) {
                num2 = new BigDecimal(fieldValue2);
            }
            // 精度处理
            String fieldPrecisionValue;
            String fieldPrecisionValue2;
            if (StringUtils.isNotBlank(precision)) {
                String zeroNum = "0." + StringUtils.repeat("0", Integer.parseInt(precision));
                DecimalFormat numFormat = new DecimalFormat(zeroNum);
                fieldPrecisionValue = numFormat.format(new BigDecimal(fieldValue));
                num1 = new BigDecimal(fieldPrecisionValue);
                if (fieldValue2 != null) {
                    fieldPrecisionValue2 = numFormat.format(new BigDecimal(fieldValue2));
                    num2 = new BigDecimal(fieldPrecisionValue2);
                }
            }

            switch (operator) {
                case "null":
                    getNullWrapper(wrapper, fieldDb);
                    break;
                case "notNull":
                    getNotNullWrapper(wrapper, fieldDb);
                    break;
                case "==":
                    wrapper.eq(fieldDb, num1);
                    break;
                case "<>":
                    wrapper.ne(fieldDb, num1);
                    break;
                case ">":
                    wrapper.gt(fieldDb, num1);

                    break;
                case "<":
                    wrapper.lt(fieldDb, num1);
                    break;
                case ">=":
                    wrapper.ge(fieldDb, num1);
                    break;
                case "<=":
                    wrapper.le(fieldDb, num1);
                    break;
                case "between":
                    wrapper.between(fieldDb, num1, num2);
                    break;
            }
        }
    }

    class DateControl extends MyType {
        @Override
        void judge(QueryWrapper<?> wrapper, String fieldDb) {

            Long time = null;
            Long time2 = null;
            Date date = new Date();
            Date date2 = new Date();
            if (StringUtils.isNoneBlank(fieldValue)) {
                time = Long.valueOf(fieldValue);
                date = new Date(time);
            }
            if (StringUtils.isNoneBlank(fieldValue2)) {
                time2 = Long.valueOf(fieldValue2);
                // 日期类型的要加上当天的23:59:59
                if (PlatformKeyConsts.DATE.equals(futureKey)) {
                    date2 = new Date(time2 + 60 * 60 * 24 * 1000 - 1000);
                } else {
                    date2 = new Date(time2);
                }

            }

            switch (operator) {
                case "null":
                    getNullWrapper(wrapper, fieldDb);
                    break;
                case "notNull":
                    getNotNullWrapper(wrapper, fieldDb);
                    break;
                case "==":
                    if (PlatformKeyConsts.DATE.equals(futureKey)) {
                        wrapper.between(fieldDb, date, new Date(time + 60 * 60 * 24 * 1000));
                    } else {
                        wrapper.eq(fieldDb, date);
                    }

                    break;
                case "<>":
                    wrapper.ne(fieldDb, date);
                    break;
                case ">":
                    wrapper.gt(fieldDb, date);
                    break;
                case "<":
                    wrapper.lt(fieldDb, date);
                    break;
                case ">=":
                    wrapper.ge(fieldDb, date);
                    break;
                case "<=":
                    wrapper.le(fieldDb, date);
                    break;
                case "between":
                    wrapper.between(fieldDb, date, date2);
                    break;
            }
        }


    }

    class TimeControl extends MyType {
        @Override
        void judge(QueryWrapper<?> wrapper, String fieldDb) {
            switch (operator) {
                case "null":
                    getNullWrapper(wrapper, fieldDb);

                    break;
                case "notNull":
                    getNotNullWrapper(wrapper, fieldDb);
                    break;
                case "==":
                    wrapper.eq(fieldDb, fieldValue);
                    break;
                case "<>":
                    wrapper.ne(fieldDb, fieldValue);
                    break;
                case ">":
                    wrapper.gt(fieldDb, fieldValue);
                    break;
                case "<":
                    wrapper.lt(fieldDb, fieldValue);
                    break;
                case ">=":
                    wrapper.ge(fieldDb, fieldValue);
                    break;
                case "<=":
                    wrapper.le(fieldDb, fieldValue);
                    break;
                case "between":
                    wrapper.between(fieldDb, fieldValue, fieldValue2);
                    break;
            }
        }
    }
    private ArrayList<String> solveListValue(String fieldValue) {
        ArrayList<String> result = new ArrayList<>();
        try {
            List<List> list = JsonUtil.getJsonToList(fieldValue, List.class);
            for (List listSub : list) {
                result.add(JSONArray.toJSONString(listSub));
                // 组织选择需要取最后每个数组最后一个
                String value = (String)listSub.get(listSub.size() - 1);
                result.add(value);
            }

        }catch (Exception e){
            List<String> list = JsonUtil.getJsonToList(fieldValue, String.class);
            result.add(JSONArray.toJSONString(list));
            String value = list.get(list.size() - 1);
            result.add(value);
        }
        return result;
    }
    /**
     * 下拉控件类型
     */
    class SelectControl extends MyType {

        @Override
        void judge(QueryWrapper<?> wrapper, String fieldDb) {
            List<String> list = solveListValue(fieldValue);
            if (StringUtils.isNoneBlank(fieldValue) && fieldValue.charAt(0) == '[' && !selectIgnore.contains(futureKey)) {
                list = JSONUtil.toList(fieldValue, String.class);
            }
            if (selectIgnore.contains(futureKey) && StringUtils.isBlank(fieldValue)) {
                fieldValue = "[]";
            }

            switch (operator) {
                case "null":
                    getNullWrapper(wrapper, fieldDb);

                    break;
                case "notNull":
                    getNotNullWrapper(wrapper, fieldDb);
                    break;
                case "==":
                    wrapper.eq(fieldDb, fieldValue);
                    break;
                case "<>":
                    wrapper.ne(fieldDb, fieldValue);
                    break;
                case "like":
                    wrapper.like(fieldDb, fieldValue);
                    break;
                case "notLike":
                    wrapper.notLike(fieldDb, fieldValue);
                    break;
                case "in":
                    if (list.size() > 0) {
                        List<String> finalList = list;
                        if ("||".equals(logic)) {
                            wrapper.or(t -> {
                                if (finalList.size() > 0) {
                                    for (int i = 0; i < finalList.size(); i++) {
                                        String value = finalList.get(i);
                                        if (i == 0) {
                                            t.like(fieldDb, value);
                                        } else {
                                            t.or().like(fieldDb, value);
                                        }
                                    }
                                }
                            });
                        } else {
                            wrapper.and(t -> {
                                if (finalList.size() > 0) {
                                    for (int i = 0; i < finalList.size(); i++) {
                                        String value = finalList.get(i);
                                        if (i == 0) {
                                            t.like(fieldDb, value);
                                        } else {
                                            t.or().like(fieldDb, value);
                                        }
                                    }
                                }
                            });
                        }
                        if(PlatformKeyConsts.CASCADER.equals(futureKey) || PlatformKeyConsts.COMSELECT.equals(futureKey) || PlatformKeyConsts.ADDRESS.equals(futureKey)){
                            getNotNullWrapper(wrapper,fieldDb);
                        }

                    }
                    break;
                case "notIn":
                    if (list.size() > 0) {
                        List<String> finalList1 = list;
                        if ("||".equals(logic)) {
                            wrapper.or(t -> {
                                if (finalList1.size() > 0) {
                                    for (int i = 0; i < finalList1.size(); i++) {
                                        String value = finalList1.get(i);
                                        if (i == 0) {
                                            t.notLike(fieldDb, value);
                                        } else {
                                            t.notLike(fieldDb, value);
                                        }

                                    }
                                }
                            });
                        } else {
                            wrapper.and(t -> {
                                if (finalList1.size() > 0) {
                                    for (int i = 0; i < finalList1.size(); i++) {
                                        String value = finalList1.get(i);
                                        if (i == 0) {
                                            t.notLike(fieldDb, value);
                                        } else {
                                            t.notLike(fieldDb, value);
                                        }

                                    }
                                }
                            });
                        }
                        if(PlatformKeyConsts.CASCADER.equals(futureKey) || PlatformKeyConsts.COMSELECT.equals(futureKey) || PlatformKeyConsts.ADDRESS.equals(futureKey)){
                            getNotNullWrapper(wrapper,fieldDb);
                        }

                    }
                    break;
            }
        }
    }

    abstract class MyType {
        abstract void judge(QueryWrapper<?> wrapper, String fieldDb);
    }
}
