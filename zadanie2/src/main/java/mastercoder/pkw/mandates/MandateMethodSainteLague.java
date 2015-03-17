package mastercoder.pkw.mandates;

import java.util.ArrayList;
import java.util.List;

public class MandateMethodSainteLague implements IMandateMethod {

    @Override
    public List<Mandate> calculateMandates(List<Vote> votes, int mandatesCount) {
        List<Mandate> partMandates = init(votes);
        List<Float> calcTab = new ArrayList<>();
        for (Vote vote : votes) {
            calcTab.add((float)vote.validVotes);
        }
        int maxInd = 0;
        for (int i = mandatesCount; i >= 0; i--) {
            float m = -1;
            for (int j = 0; j < calcTab.size(); j++) {
                if (m < calcTab.get(j)) {
                    m = calcTab.get(j);
                    maxInd = j;
                }
            }
            partMandates.get(maxInd).mandates++;
            calcTab.set(maxInd, calc(votes.get(maxInd).validVotes, partMandates.get(maxInd).mandates));
        }
        return partMandates;
    }

    private List<Mandate> init(List<Vote> v) {
        List<Mandate> partMandates = new ArrayList<>(v.size());
        for(Vote vote : v) {
            Mandate mandate = new Mandate(vote.partShortName, vote.partName, 0);
            partMandates.add(mandate);
        }

        return partMandates;
    }

    private float calc(int v, int mandates) {
        return (float)((v * 1.0) / (mandates + 1.0));
    }
}
