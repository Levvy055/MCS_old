package mastercoder.pkw.mandates;

import java.util.ArrayList;
import java.util.List;

public class MandateMethodDHondta implements IMandateMethod {

    @Override
    public List<Mandate> calculateMandates(List<Vote> votes, int mandatesCount) {
        List<Vote> v = votes;
        List<Mandate> partMandates = initializeMandates(v);
        List<Double> tab = new ArrayList<>();
        for (Vote vote : v) {
            tab.add((double) vote.validVotes);
        }
        int maxInd = 0;
        for (int i = mandatesCount; i > 0; i--) {
            double max = -1;
            for (int j = 0; j < tab.size(); j++) {
                if (max < tab.get(j)) {
                    max = tab.get(j);
                    maxInd = j;
                }
            }

            partMandates.get(maxInd).mandates++;
            tab.set(maxInd, calc(v.get(maxInd).validVotes, partMandates.get(maxInd).mandates));
        }
        return partMandates;
    }

    private List<Mandate> initializeMandates(List<Vote> v) {
        List<Mandate> partMandates = new ArrayList<>(v.size());
        for (Vote vote : v) {
            Mandate mandate = new Mandate(vote.partShortName, vote.partName, 0);
            partMandates.add(mandate);
        }
        return partMandates;
    }

    private double calc(int v, int mandates) {
        return (double)v * 1.0 / (2*mandates + 1.0);
    }
}
