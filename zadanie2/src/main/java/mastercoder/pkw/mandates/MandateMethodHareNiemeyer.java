package mastercoder.pkw.mandates;

import java.util.ArrayList;
import java.util.List;

public class MandateMethodHareNiemeyer implements IMandateMethod {

    @Override
    public List<Mandate> calculateMandates(List<Vote> votes, int mandatesCount) {
        List<Vote> v = votes;
        List<Mandate> partMandates = init(v);
        List<Double> tab = new ArrayList<>(v.size());
        int totalVotes = 0;
        for (Vote vote : v) {
            totalVotes += vote.validVotes;
        }
        int mandCounter = 0;
        for(int i=0; i<v.size();i++) {
            double d = v.get(i).validVotes * mandatesCount / totalVotes;
            tab.add(d);
            partMandates.get(i).mandates = (int)Math.floor(tab.get(i));
            tab.set(i, tab.get(i)-partMandates.get(i).mandates);
            mandCounter += partMandates.get(i).mandates;
        }
        double max;
        int maxInd = 0;
        while(mandCounter < mandatesCount) {
            max = -1;
            for(int i=0; i<v.size(); i++) {
                if(max<tab.get(i)) {
                    max = tab.get(i);
                    maxInd = i;
                }
            }
            partMandates.get(maxInd).mandates++;
            mandCounter++;
            tab.set(maxInd, 0d);
        }
        return partMandates;
    }

    private List<Mandate> init(List<Vote> votes) {
        List<Mandate> partMandates = new ArrayList<>(votes.size());
        for (Vote vote : votes) {
            Mandate mandate = new Mandate(vote.partShortName, vote.partName, 0);
            partMandates.add(mandate);
        }
        return partMandates;
    }
}
