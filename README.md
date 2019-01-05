# cryptils

Java Utilities for Crypto

## Outside Dependencies

* github.com/bitcoin-core/secp256k1

NOTE: the secp256k1 library does NOT include the recovery and schnorr modules
by default. Include them in the install as follows:

`./configure --enable-experimental --enable-module-recovery --enable-module-schnorr`

## Included Dependencies

* scrypt-1.4.0
* zxing-core-3.2.1
* zxing-javase-3.2.1

## Reused Code

https://github.com/ethereum/ethereumj
* reused only the keccak256 implementation, modified to tbox package structure
