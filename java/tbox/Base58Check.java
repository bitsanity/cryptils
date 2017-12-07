package tbox;

import java.util.Arrays;

public class Base58Check
{
  public static String encode( byte[] extkey ) throws Exception
  {
    byte[] checksum =
      java.util.Arrays.copyOfRange( SHA256.hash(SHA256.hash(extkey)), 0, 4 );

    // append checksum to input
    byte[] toEncode = new byte[ extkey.length + checksum.length ];

    System.arraycopy( extkey, 0, toEncode, 0, extkey.length );

    System.arraycopy( checksum,
                      0,
                      toEncode,
                      toEncode.length - checksum.length,
                      checksum.length );

    return Base58.encode( toEncode );
  }

  public static byte[] decode( String str ) throws Exception
  {
    byte[] strBytes = Base58.decode( str );

    byte[] data = Arrays.copyOfRange(strBytes, 0, strBytes.length - 4);

    byte[] check = Arrays.copyOfRange( strBytes,
                                       strBytes.length - 4,
                                       strBytes.length );

    byte[] hashed = Arrays.copyOfRange( SHA256.doubleHash(data), 0, 4 );

    if (!Arrays.equals(hashed, check))
      throw new Exception( "checksum failed, check: " +
                           HexString.encode(check) +
                           ", hashed: " + HexString.encode(hashed) );
    return data;
  }

  public static void main( String[] args ) throws Exception
  {
    // data:
    // github.com/nayuki/Bitcoin-Cryptography-Library/blob/master/
    // java/io/nayuki/bitcoin/crypto/Base58CheckTest.java

    String[] srcs = new String[] {
      "",
      "FF",
      "00",
      "0000",
      "00010966776006953D5567439E5E39F86A0D273BEE" };

    String[] rslts = new String[] {
      "3QJmnh",
      "VrZDWwe",
      "1Wh4bh",
      "112edB6q",
      "16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM" };

    for (int ii = 0; ii < srcs.length; ii++)
    {
      byte[] srcbytes = HexString.decode( srcs[ii] );
      String b58 = encode( srcbytes );

      if (!b58.equals(rslts[ii]))
      {
        System.err.println( "Base58Check : " + b58 + " != " + rslts[ii] );
        return;
      }
    }

    for (int ii = 0; ii < rslts.length; ii++)
    {
      byte[] dec = decode( rslts[ii] );
      String chk = HexString.encode( dec );

      if (!chk.equalsIgnoreCase(srcs[ii]))
      {
        System.err.println( "Base58Check : " + srcs[ii] + " != " + chk );
        return;
      }
    }

    System.out.println( "Base58Check: PASS" );
  }
}
