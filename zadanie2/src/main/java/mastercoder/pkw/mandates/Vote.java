package mastercoder.pkw.mandates;

public final class Vote {

    public final String partShortName;
    public final String partName;
    public int validVotes;

    public Vote(String partShortName, String partName, int validVotes) {
        this.partShortName = partShortName.toUpperCase();
        this.partName = partName;
        this.validVotes = validVotes;
    }
}
