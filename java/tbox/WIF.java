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
      System.err.println( "WIF: FAIL Expected: " + expected + ", got: " + wif );
  }
}
