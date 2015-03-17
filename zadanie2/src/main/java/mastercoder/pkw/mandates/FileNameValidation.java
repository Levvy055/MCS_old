package mastercoder.pkw.mandates;

public class FileNameValidation {

    private static final String p = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static boolean validate(String n) {
        n = n.substring(0,n.length() - 4);
        return n.matches(p);
    }
}