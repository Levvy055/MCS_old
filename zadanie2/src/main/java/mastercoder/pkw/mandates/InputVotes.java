package mastercoder.pkw.mandates;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InputVotes implements IInputVotes {

    private final List<Vote> v;
    private final String filesPath;

    public InputVotes(String votesFilesPath) throws InputParametersException {
        this.v = new ArrayList<>();
        this.filesPath = votesFilesPath;
        initialize();
    }

    private void initialize() throws InputParametersException {
        File dir = new File(filesPath);
        File[] f = dir.listFiles();
        for (File file : f) {
            if (FileNameValidation.validate(file.getName())) {
                try {
                    List<String> l = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
                    initializeValidVotesFromFileData(l);
                } catch (IOException e) {
                    throw new InputParametersException("Reading from path: "+ filesPath +" failed");
                }
            }
        }
    }

    private void initializeValidVotesFromFileData(List<String> l) {
        for (String line : l) {
            String[] f = line.split(";");
            Vote vote = new Vote(f[0], f[1], Integer.parseInt(f[2]));

            boolean updated = false;

            for (Vote validVote : v) {
                if (validVote.partShortName.equals(vote.partShortName)) {
                    validVote.validVotes = vote.validVotes;
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                v.add(vote);
            }
        }
    }

    @Override
    public List<Vote> getAllValidVotes() {
        return v;
    }

    @Override
    public List<Vote> getAllValidVotesForPart(String partShortName) {
        ArrayList<Vote> validVotesForPart = new ArrayList<>();
        for (Vote vote : v) {
            if (vote.partShortName.equals(partShortName)) {
                validVotesForPart.add(vote);
            }
        }
        return validVotesForPart;
    }
}
