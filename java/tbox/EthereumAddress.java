package tbox;

// See:
// https://ethereum.stackexchange.com/questions/3542/
//   how-are-ethereum-addresses-generated/3619#3619

public class EthereumAddress
{
  private ECKeyPair keypair_;

  public EthereumAddress( byte[] pvkey ) throws Exception
  {
    keypair_ = new ECKeyPair( pvkey );

    // ensure we're configured to use uncompressed keys
    byte[] pubkey = keypair_.uncompressedPublicKey();
    if (pubkey.length != 65 || pubkey[0] != (byte)0x04)
      throw new Exception( "Invalid public key format: " +
                           HexString.encode(pubkey) );
  }

  public String toString()
  {
    try {
      byte[] pubkey64 =
        ByteOps.dropFirstByte( keypair_.uncompressedPublicKey() );

      byte[] hashed = Keccak256.hash( pubkey64 );
      byte[] resultbytes = java.util.Arrays.copyOfRange( hashed, 12, 32 );
      return "0x" + HexString.encode( resultbytes );
    } catch( Exception e ) {}

    return "";
  }

  // test vectors from:
  // https://github.com/ethereum/tests/blob/develop/BasicTests/keyaddrtest.json
  public static void main( String[] args ) throws Exception
  {
    String[] privatekeys = {
      "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
      "c87f65ff3f271bf5dc8643484f66b200109caffe4bf98c4cb393dc35740b28c0"
    };

    String[] addresses = {
      "0xcd2a3d9f938e13cd947ec05abc7fe734df8dd826",
      "0x13978aee95f38490e9769c39b2773ed763d9cd5f"
    };

    for (int ii = 0; ii < privatekeys.length; ii++)
    {
      EthereumAddress ea =
        new EthereumAddress( HexString.decode(privatekeys[ii]) );

      if ( !ea.toString().equalsIgnoreCase(addresses[ii]) )
        throw new Exception( "Expected: " + addresses[ii] +
                             " got: " + ea.toString() );
    }

    System.out.println( "EthereumAddress: PASS" );
  }
}
