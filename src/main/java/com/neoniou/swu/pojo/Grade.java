package com.neoniou.swu.pojo;

/**
 * 成绩
 *
 * @author Neo.Zzj
 */
public class Grade {

    /**
     * 课程名称
     */
    private String kcmc;
    /**
     * 成绩
     */
    private String cj;
    /**
     * 学分
     */
    private String xf;
    /**
     * 绩点
     */
    private String jd;

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }
}
