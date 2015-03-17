package mastercoder.pkw.mandates;

public final class MandateMethodFactory {

    public static IMandateMethod get(MethodEnum method) {
        switch (method) {
            case D_HONDTA:
                return new MandateMethodDHondta();
            case SAINT_LAGUE:
                return new MandateMethodSainteLague();
            case HARE_NIEMEYER:
                return new MandateMethodHareNiemeyer();
            default:
                return new MandateMethodDHondta();
        }
    }
}
