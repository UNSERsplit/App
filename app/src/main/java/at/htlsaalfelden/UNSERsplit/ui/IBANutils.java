package at.htlsaalfelden.UNSERsplit.ui;

import java.math.BigInteger;

public class IBANutils {
    public static boolean isValidIban(String iban) {
        if(iban == null) return false;
        String spaceLessIban = iban.replaceAll(" ", "").strip();
        if(spaceLessIban.length() != 20) return false;

        if(!spaceLessIban.startsWith("AT")) return false;

        System.out.println(spaceLessIban);
        String movedIban = spaceLessIban.substring(4) + spaceLessIban.substring(0, 4);
        System.out.println(movedIban);
        BigInteger asInteger = convertCharsToNumbers(movedIban);
        System.out.println(asInteger);
        BigInteger mod = asInteger.mod(new BigInteger("97"));
        System.out.println(mod);
        return mod.equals(new BigInteger("1"));
    }

    private static BigInteger convertCharsToNumbers(String string) {
        StringBuilder sb = new StringBuilder();

        for(char c : string.toUpperCase().toCharArray()) {
            if(c >= '0' && c <= '9') {
                sb.append(c);
            } else {
                int value = c - 'A' + 10;
                sb.append(value);
            }
        }

        return new BigInteger(sb.toString());
    }
}
