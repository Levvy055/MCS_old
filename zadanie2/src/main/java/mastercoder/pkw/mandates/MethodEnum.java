package mastercoder.pkw.mandates;

public enum MethodEnum {
    D_HONDTA, SAINT_LAGUE, HARE_NIEMEYER, UNKNOWN;

    public static MethodEnum byOrdinal(int ordinal) {
        MethodEnum[] values = values();
        for (MethodEnum methodEnum : values) {
            if (methodEnum.ordinal() == ordinal) {
                return methodEnum;
            }
        }
        return null;
    }
}
