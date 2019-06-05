package com.manymobi.esdsl.parser.run.process;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 梁建军
 * 创建日期： 2018/11/5
 * 创建时间： 14:45
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractRunProcess implements RunProcess {

    protected List<RunProcess> child;

    protected AbstractRunProcess(List<RunProcess> child) {
        this.child = child;
    }

    protected Object getVariable/*变量*/(Map<String, Object> parameter, String key) {

        if (key == null) {
            return null;
        }

        String[] split = key.split("\\.");
        Object temp = parameter;
        for (String s : split) {
            if (temp instanceof Map) {
                temp = ((Map) temp).get(s);
            } else {
                //首字母大写
                String titleCase = s.substring(0, 1).toUpperCase() + s.substring(1);
                try {
                    Method method = temp.getClass().getMethod("get" + titleCase);
                    temp = method.invoke(temp);
                } catch (Exception e) {
                    throw new IllegalParameterException("获取参数失败,参数 " + JSON.toJSONString(parameter) + " key " + key, e);
                }
            }
        }

        return temp;
    }

    public abstract static class Build<T extends AbstractRunProcess> {

        protected List<RunProcess> child = new LinkedList<>();

        public void addRunProcess(RunProcess runProcess) {
            child.add(runProcess);
        }

        public abstract T build();
    }

}
