package cn.rivamed.log.core.enums;

public enum RivamedLogDesensitizedEnum {
    PASSWORD("password", "******"),
    PHONE("phone", "***********"),
    IDCARD("idCard", "*****************");

    private String param;
    private String desensitizedValue;

    RivamedLogDesensitizedEnum(String param, String desensitizedValue) {
        this.param = param;
        this.desensitizedValue = desensitizedValue;
    }

    public String getParam() {
        return param;
    }

    public String getDesensitizedValue() {
        return desensitizedValue;
    }


}
