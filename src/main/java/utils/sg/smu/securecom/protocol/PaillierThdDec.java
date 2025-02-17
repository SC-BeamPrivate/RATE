package utils.sg.smu.securecom.protocol;

import utils.sg.smu.securecom.keys.KeyGen;
import utils.sg.smu.securecom.keys.PaillierPrivateKey;
import utils.sg.smu.securecom.keys.PaillierThdPrivateKey;
import edu.alibaba.mpc4j.common.jnagmp.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author bwzhao
 * Pailliery cryptosystem with threshold decryption
 */
public class PaillierThdDec {

	/**
	 * The first parameter of threshold private key
	 * sk1+sk2=0 mod lambda and sk1+sk2=1 mod n
	 */
	protected BigInteger sk;

	/**
	 * The public key parameter n of Paillier cryptosystem
	 */
	protected BigInteger n;

	/**
	 * nsquare=n*n
	 */
	protected BigInteger nsquare;

	private BigInteger random;
	static BigIntegerUtils BigIntegerUtils = new BigIntegerUtils();

	/**
	 * Constructor, initialize threshold key
	 *
	 * @param key
	 */
	public PaillierThdDec(PaillierThdPrivateKey key) {
		this.sk = key.getSk();
		this.n = key.getN();
		this.nsquare = key.getNsquare();
	}

	/**
	 * Party decrypt ciphertext
	 *
	 * @param c
	 * @return c^sk mod n^2=r^{sk*N}*(1+mn*sk) mod n^2
	 */
	public BigInteger partyDecrypt(BigInteger c) {
		return BigIntegerUtils.modPow(c, sk, nsquare);
//		return c.modPow(sk, nsquare);
	}

	/**
	 * Integrate party decrypted ciphertexts
	 *
	 * @param c1 the first party ciphertext
	 * @param c2 the second party ciphertext
	 * @return m=(c1*c2 mod n^2-1)/n
	 */
	public BigInteger finalDecrypt(BigInteger c1, BigInteger c2) {
		return BigIntegerUtils.mulMod(c1, c2, nsquare).subtract(BigInteger.ONE).divide(n);
//        return c1.multiply(c2).mod(nsquare).subtract(BigInteger.ONE).divide(n);
	}

	/**
	 * Integrate party decrypted ciphertexts
	 *
	 * @param c party ciphertexts arrary
	 * @return m=(c[0]*c[1] mod n^2-1)/n
	 */
	public BigInteger finalDecrypt(BigInteger[] c) {
		return BigIntegerUtils.mulMod(c[0], c[1], nsquare).subtract(BigInteger.ONE).divide(n);
//        return c[0].multiply(c[1]).mod(nsquare).subtract(BigInteger.ONE).divide(n);
	}

	public void setRandom(BigInteger random) {
		this.random = random;
	}

	public BigInteger getRandom() {
		return this.random;
	}

	public static void test() {
		Random rd = new Random();
		PaillierPrivateKey key = KeyGen.PaillierKey(512, 122333356, rd);
		BigInteger m1 = new BigInteger("134252345");
		Paillier pai = new Paillier(key);

		BigInteger em1 = pai.encrypt(m1);
		System.out.println(pai.decrypt(em1));

		PaillierThdPrivateKey[] ThdKey = KeyGen.genThdKey(key.getLambda(), key.getN(), key.getNsquare(), rd);
		PaillierThdDec ptd1 = new PaillierThdDec(ThdKey[0]);
		PaillierThdDec ptd2 = new PaillierThdDec(ThdKey[1]);

		BigInteger pm1 = ptd1.partyDecrypt(em1);
		BigInteger pm2 = ptd2.partyDecrypt(em1);

		System.out.println(ptd2.finalDecrypt(pm1, pm2));
	}

	public static void main(String[] args) {

		test();
	}
}
