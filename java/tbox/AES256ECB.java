package tbox;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES256ECB {
    public AES256ECB( byte[] aeskey ) throws Exception
    {
      sks_ = new SecretKeySpec( aeskey, KEYSPEC );
    }

    public byte[] encrypt( byte[] red ) throws Exception
    {
        Cipher cipher = Cipher.getInstance( CIPHER );
        cipher.init( Cipher.ENCRYPT_MODE, sks_ );
        return cipher.doFinal( red );
    }

    public byte[] decrypt( byte[] blk ) throws Exception
    {
        Cipher cipher = Cipher.getInstance( CIPHER );
        cipher.init( Cipher.DECRYPT_MODE, sks_ );
        return cipher.doFinal( blk );
    }

    private SecretKeySpec sks_;
    private static final String CIPHER = "AES/ECB/NoPadding";
    private static final String KEYSPEC = "AES";
}
