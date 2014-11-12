package net.kaoriya.incenc;

import java.util.ArrayList;

public class FrontCompression {

    public final static char ASCII_RS = '\u001e';
    public final static char ASCII_US = '\u001f';

    /**
     * Compress array of strings to a string with simple front compression.
     */
    public static String compress(String[] strs) {
        // Check argument.
        if (strs == null) {
            return null;
        } else if (strs.length == 0) {
            return "";
        }
        // Compress.
        StringBuffer b = new StringBuffer();
        String prev = "";
        for (int i = 0; i < strs.length; ++i) {
            String s = strs[i] == null ? "" : strs[i];
            // TODO: check to including ASCII_?? chars.
            int l = countMatchPrefix(s, prev);
            if (l >= 3) {
                b.append(l)
                 .append(ASCII_US)
                 .append(s.substring(l))
                 .append(ASCII_RS);
            } else {
                b.append(s)
                 .append(ASCII_RS);
            }
            prev = s;
        }
        return b.toString();
    }

    private static int countMatchPrefix(String s, String prev) {
        int l = s.length(), n = prev.length();
        if (n < l) {
            l = n;
        }
        for (int i = 0; i < l; ++i) {
            if (s.charAt(i) != prev.charAt(i)) {
                l = i;
                break;
            }
        }
        return l;
    }

    /**
     * Decompress array of strings from a string by simple front compression.
     */
    public static String[] decompress(String s) {
        ArrayList<String> a = new ArrayList<>();
        int prev_start = 0, prev_end = -1;
        StringBuilder prev = new StringBuilder();
        for (int i = 0, L = s.length(); i < L; i++) {
            int x = s.indexOf(ASCII_RS, i);
            if (x < 0) {
                // FIXME: raise exception.
                return null;
            }
            int y = s.indexOf(ASCII_US, i);
            if (y > x || y < 0) {
                prev.delete(0, prev.length());
                prev.append(s, i, x);
            } else {
                int n = Integer.parseInt(s.substring(i, y), 10);
                if (n < 0) {
                    // TODO: raise invalid reuse length error.
                    return null;
                } else if (n > prev.length()) {
                    // TODO: raise too long reuse length error.
                    return null;
                }
                prev.delete(n, prev.length());
                prev.append(s, y + 1, x);
            }
            a.add(prev.toString());
            i = x;
        }
        return a.toArray(new String[0]);
    }

}
