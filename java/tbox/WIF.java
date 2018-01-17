package tbox;

import java.util.Arrays;

//
// Wallet Import Formatter
// https://en.bitcoin.it/wiki/Wallet_import_format
//
public class WIF
{
  public static String toWIF( byte[] pvtkey ) throws Exception
  {
    return toWIF( pvtkey, true, false );
  }

  // isMain: true for mainnet address, false for testnet
  // toCompressed: true if the pvt key will correspond to a compressed pubkey
  public static String
    toWIF( byte[] pvtkey, boolean isMain, boolean toCompressed )
  throws Exception
  {
    byte[] extendedkey =
      ByteOps.prepend( (isMain)?(byte)0x80:(byte)0xef, pvtkey );

    if (toCompressed)
      extendedkey = ByteOps.concat( extendedkey, (byte)0x01 );

    byte[] checksum =
      Arrays.copyOfRange( SHA256.doubleHash(extendedkey), 0, 4 );

    byte[] assembled = ByteOps.concat( extendedkey, checksum );

    return Base58.encode( assembled );
  }

  public static byte[] fromWIF( String wif ) throws Exception
  {
    byte[] decoded = Base58.decode( wif );
    if (decoded[0] != (byte)0x80) throw new Exception("Not a WIF pvt key");

    byte[] nocheck = Arrays.copyOfRange( decoded, 0, decoded.length - 4 );

    byte[] checksum =
      Arrays.copyOfRange( decoded, decoded.length - 4, decoded.length );

    if (!ByteOps.compare( Arrays.copyOfRange(SHA256.doubleHash(nocheck), 0, 4),
                          checksum ))
      throw new Exception( "corrupt key" );

    byte[] noheader = ByteOps.dropFirstByte( nocheck );

    // uncompressed - starts with 5
    // compressed - starts with K or L (drop last byte 0x01)
    if (!wif.startsWith("5"))
    {
      if (noheader[noheader.length - 1] != (byte)0x01)
        throw new Exception( "compressed but last byte was not 0x01" );

      noheader = Arrays.copyOfRange( noheader, 0, noheader.length - 1 );
    }

    return noheader;
  }

  // Test --------------------------------------------------------------------
  public static void main( String[] args ) throws Exception
  {
    // well known - do not use for anything other than test
    byte[] pvtkey =
      HexString.decode(
        "0C28FCA386C7A227600B2FE50B7CAE11EC86D3BF1FBE471BE89827E19D72AA1D" );

    String wif = toWIF( pvtkey );
    String expected = "5HueCGU8rMjxEXxiPuD5BDku4MkFqeZyd4dZ1jvhTVqvbTLvyTJ";

    if (wif.equals(expected))
      System.out.println( "WIF: PASS" );
    else
      System.err.println( "WIF: FAIL\nExp: " + expected + ",\ngot: " + wif );

    byte[] pvtkeyfromwif = fromWIF( expected );

    if (ByteOps.compare(pvtkeyfromwif, pvtkey))
      System.out.println( "WIF.fromWIF() PASS" );
    else
      System.out.println( "WIF.fromWIF() FAIL" );
  }
}
