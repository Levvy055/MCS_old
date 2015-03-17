package mastercoder.pkw.mandates;

import java.util.List;

public interface IInputVotes {

    List<Vote> getAllValidVotes();
    List<Vote> getAllValidVotesForPart(String partShortName);
}
