package net.kaoriya.incenc;

import org.junit.Test;
import static org.junit.Assert.*;

public class FrontCompressionTest
{

    public void checkCompress(String[] array, String exp) {
        String s = FrontCompression.compress(array);
        assertNotNull(s);
        assertEquals(exp, s);
    }

    @Test
    public void compressEmpty() {
        checkCompress(new String[]{}, "");
    }

    @Test
    public void compressNone() {
        checkCompress(new String[]{ "abc", "def" }, "abc\u001edef\u001e");
    }

    @Test
    public void compressSimple() {
        checkCompress(new String[]{ "foo", "foobar" },
                "foo\u001e3\u001fbar\u001e");
        checkCompress(new String[]{ "ourwork", "ourdestiny" },
                "ourwork\u001e3\u001fdestiny\u001e");
    }

    @Test
    public void compressShort() {
        checkCompress(new String[]{ "foobar", "foo" },
                "foobar\u001e3\u001f\u001e");
    }

    @Test
    public void compressCancel() {
	checkCompress(new String[]{ "mywork", "myoffice" },
                "mywork\u001emyoffice\u001e");
        checkCompress(new String[]{ "do", "dog" }, "do\u001edog\u001e");
        checkCompress(new String[]{ "a", "ab", "abc" },
                "a\u001eab\u001eabc\u001e");
    }

    @Test
    public void compressMany() {
        checkCompress(new String[]{"abc", "abcdef", "abcdefghi"},
                "abc\u001e3\u001fdef\u001e6\u001fghi\u001e");
    }

    public void checkDecompress(String s, String[] exp) {
        String[] array = FrontCompression.decompress(s);
        assertNotNull(array);
        assertArrayEquals(exp, array);
    }

    @Test
    public void decompressEmpty() {
        checkDecompress("", new String[0]);
    }

    @Test
    public void decompressNone() {
        checkDecompress("abc\u001edef\u001e", new String[]{"abc", "def"});
    }

    @Test
    public void decompressSimple() {
        checkDecompress("foo\u001e3\u001fbar\u001e",
                new String[]{"foo", "foobar"});
        checkDecompress("ourwork\u001e3\u001fdestiny\u001e",
                new String[]{"ourwork", "ourdestiny"});
    }

    @Test
    public void decompressShort() {
        checkDecompress("foobar\u001e3\u001f\u001e",
                new String[]{"foobar", "foo"});
    }

    @Test
    public void decompressCancel() {
        checkDecompress("mywork\u001emyoffice\u001e",
                new String[]{"mywork", "myoffice"});
        checkDecompress("do\u001edog\u001e", new String[]{"do", "dog"});
        checkDecompress("a\u001eab\u001eabc\u001e",
                new String[]{"a", "ab", "abc"});
    }

    @Test
    public void decompressMany() {
        checkDecompress("abc\u001e3\u001fdef\u001e6\u001fghi\u001e",
                new String[]{"abc", "abcdef", "abcdefghi"});
    }

}
