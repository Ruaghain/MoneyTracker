package com.wraith.money.web.repository;

import com.wraith.money.repository.encoding.Encoding;
import org.junit.Assert;

/**
 * This class tests the encoding mechanism for Atlantic Money.
 *
 * NOTE: Can't test this as the encoding changes each time it envoked.
 *
 * User: rowan.massey
 * Date: 24/03/13
 * Time: 19:24
 */
public class EncodingTest {

    //@Test
    public void testEncodePassword() throws Exception {
        Encoding encoding = new Encoding();
        String encodedPassword = encoding.encodePassword("Passw0rd");
        Assert.assertTrue(encoding.validPassword("Password", encodedPassword));
    }
}
