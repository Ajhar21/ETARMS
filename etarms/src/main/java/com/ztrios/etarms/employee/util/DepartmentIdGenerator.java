package com.ztrios.etarms.employee.util;

public class DepartmentIdGenerator {

    private DepartmentIdGenerator() {}

    public static String nextId(long seq) {
        return String.format("dep%03d", seq);
    }
}
