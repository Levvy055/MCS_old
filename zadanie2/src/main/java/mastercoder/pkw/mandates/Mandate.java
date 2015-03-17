package mastercoder.pkw.mandates;

public final class Mandate {

    public final String partShortName;
    public final String partName;
    public int mandates;

    public Mandate(String name1, String name2, int mandates) {
        this.partName = name1;
        this.partShortName = name2;
        this.mandates = mandates;
    }
}
