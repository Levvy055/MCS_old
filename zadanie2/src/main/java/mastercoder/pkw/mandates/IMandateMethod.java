package mastercoder.pkw.mandates;

import java.util.List;

public interface IMandateMethod {

    List<Mandate> calculateMandates(List<Vote> votes, int mandates);
}
