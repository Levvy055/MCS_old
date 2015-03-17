package mastercoder.pkw.mandates;

import java.io.File;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        try {
            validateInputParameters(args);
            InputVotes votes = new InputVotes(args[2]);
            IMandateMethod m = MandateMethodFactory.get(MethodEnum.byOrdinal(Integer.parseInt(args[0])));
            List<Mandate> mandates = m.calculateMandates(votes.getAllValidVotes(), Integer.parseInt(args[1]));
            showOnConsole(mandates);
        } catch (InputParametersException e) {
            System.out.println("Wrong input parameters. No party will receive mandates.");
        }
    }

    private static void validateInputParameters(String[] args) throws InputParametersException{
        if (args.length != 3) {
            throw new InputParametersException("Parameters count error.");
        }
        if (!isNumeric(args[0]) || !isNumeric(args[1])) {
            throw new InputParametersException("Parameter should be a number.");
        }
        if (isNullOrWhiteSpace(args[2]) || !directoryExists(args[2])) {
            throw new InputParametersException("Wrong path name.");
        }
    }

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNullOrWhiteSpace (String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean directoryExists(String directory) {
        File file = new File(directory);
        return file.exists();
    }

    private static void showOnConsole(List<Mandate> mandates) {
        for (Mandate mandate : mandates) {
            System.out.println(String.format("%s;%s;%d", mandate.partShortName, mandate.partName, mandate.mandates));
        }
    }
}
