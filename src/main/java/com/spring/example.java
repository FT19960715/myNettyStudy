package com.spring;

import org.apache.ibatis.jdbc.SQL;

public class example {
    public String selectPersonSql(){
        return new SQL().SELECT("P.ID", "A.USERNAME", "A.PASSWORD", "P.FULL_NAME", "D.DEPARTMENT_NAME", "C.COMPANY_NAME")
                .FROM("info_user").INNER_JOIN("info_clinic").WHERE("u.logic_delete=0")
                .AND().WHERE("clinic_id = 1").LIMIT(10).toString();
    }

    public static void main(String[] args) {
        example e = new example();
        System.out.println(e.selectPersonSql());
    }
}
