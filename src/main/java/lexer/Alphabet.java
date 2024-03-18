package lexer;

public class Alphabet {

    public static boolean r1_9(char match) {
        return match >= '1' && match <= '9';
    }

    public static boolean r0_9(char match) {
        return match == '0' || r1_9(match);
    }


    public static boolean rA_Z(char match) {
        return match >= 'A' && match <= 'Z';
    }

    public static boolean ra_z(char match) {
        return match >= 'a' && match <= 'z';
    }

    public static boolean horizontalWhiteSpace(char match) {
        return switch (match) {
            case '\r', '\t' -> true;
            default -> false;
        };
    }

    public static boolean verticalWhiteSpace(char match) {
        return switch (match) {
            case '\n', '\u000B' -> true;
            default -> false;
        };
    }

    public static boolean structIdEnd(char match) {
        return rA_Z(match) || ra_z(match);
    }

    public static boolean id(char match) {
        return structIdEnd(match) || match == '_' || r0_9(match);
    }

    public static boolean special(char match) {
        return (match >= 33 && match <= 47) || (match >= 58 && match <= 63) || (match >= 91 && match <= 96) || (match >= 123 && match <= 126) || (match >= 128 && match <= 141) || (match >= 161 && match <= 254);
    }

    public static boolean character(char match) {
        return id(match) || special(match) || match == ' ';
    }

    public static boolean comment(char match) {
        return character(match) || horizontalWhiteSpace(match);
    }

    public static boolean string(char match) {
        return comment(match) || verticalWhiteSpace(match);
    }

    public static boolean escape(char match) {
        return switch (match) {
            case 'b', 'f', 'n', 'r', 't', 'v', '\'', '"', '\\' -> true;
            default -> false;
        };
    }


}
