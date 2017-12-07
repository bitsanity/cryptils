package tbox;

import java.math.BigInteger;

public class Base58
{
  public static String encode( byte[] src )
  {
    if (src.length == 0) return "";

    BigInteger srcInt = new BigInteger( 1, src );

    StringBuffer sbuff = new StringBuffer();

    while ( 0 <= srcInt.compareTo(BASE) )
    {
      BigInteger mod = srcInt.mod( BASE );
      sbuff.insert( 0, LUT.charAt(mod.intValue()) );
      srcInt = srcInt.subtract( mod ).divide( BASE );
    }

    sbuff.insert( 0, LUT.charAt(srcInt.intValue()) );

    for ( int ii = 0; ii < src.length; ii++ )
      if ( 0 == src[ii] )
        sbuff.insert( 0, LUT.charAt(0) );
      else break;

    return sbuff.toString();
  }

  public static byte[] decode( String b58 )
  {
    if (b58.length() == 0) return new byte[0];

    byte[] raw = toBigInt( b58 ).toByteArray();

    boolean sign = raw.length > 1 && raw[0] == 0 && raw[1] < 0;

    int zeroes = 0;
    for ( int ii = 0;
              ii < b58.length() && b58.charAt(ii) == LUT.charAt(0); ii++ )
      zeroes++;

    byte[] tmp = new byte[raw.length - (sign ? 1 : 0) + zeroes];

    System.arraycopy( raw, (sign ? 1 : 0), tmp, zeroes, tmp.length - zeroes );

    return tmp;
  }

  private static BigInteger toBigInt( String b58 )
  {
    BigInteger result = BigInteger.valueOf( 0 );

    for ( int ii = b58.length() - 1; 0 <= ii; ii-- )
    {
      int index = LUT.indexOf( b58.charAt(ii) );
      result = result.add(
        BigInteger.valueOf(index).multiply(BASE.pow(b58.length() - 1 - ii)));
    }

    return result;
  }

  public static void main( String[] args ) throws Exception
  {
    // github.com/bitcoin/bitcoin/blob/master/
    // src/test/data/base58_encode_decode.json
    String[] srcs = new String[] {
      "",
      "61",
      "626262",
      "636363",
      "73696d706c792061206c6f6e6720737472696e67",
      "00eb15231dfceb60925886b67d065299925915aeb172c06647",
      "516b6fcd0f",
      "bf4f89001e670274dd",
      "572e4794",
      "ecac89cad93923c02321",
      "10c8511e" };

    String[] rslts = new String[] {
      "",
      "2g",
      "a3gV",
      "aPEr",
      "2cFupjhnEsSn59qHXstmK2ffpLv2",
      "1NS17iag9jJgTHD1VXjvLCEnZuQ3rJDE9L",
      "ABnLTmg",
      "3SEo3LWLoPntC",
      "3EFU7m",
      "EJDM8drfXA6uyA",
      "Rt5zm" };

    for (int ii = 0; ii < srcs.length; ii++)
    {
      byte[] srcbytes = HexString.decode( srcs[ii] );
      String b58 = encode( srcbytes );

      if (!b58.equals(rslts[ii]))
      {
        System.err.println( "Base58e : " + b58 + " != " + rslts[ii] );
        return;
      }
    }

    for (int ii = 0; ii < rslts.length; ii++)
    {
      byte[] dec = decode( rslts[ii] );
      String chk = HexString.encode( dec );

      if (!chk.equals(srcs[ii]))
      {
        System.err.println( "Base58d : " + srcs[ii] + " != " + chk );
        return;
      }
    }

    System.out.println( "Base58: PASS" );
  }

  private static final String LUT =
          "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

  private static final BigInteger BASE = BigInteger.valueOf(58);
}
