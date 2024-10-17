package CtrlS;

/**
 * Class that Support encryption and decryption.
 */
// Team-Ctrl-S(Currency)
public class EncryptionSupport {

    private static final String key = "randomkey";

    /**
     * Performs encryption for a given input.
     */
    // Team-Ctrl-S(Currency)
    public static String encrypt(String input) {
        byte[] bytes = input.getBytes();
        byte[] keyBytes = key.getBytes();
        byte[] encryptedBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            encryptedBytes[i] = (byte) (bytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return new String(encryptedBytes);
    }

    /**
     * Performs decryption for a given input.
     */
    // Team-Ctrl-S(Currency)
    public static String decrypt(String encrypted) {
        byte[] encryptedBytes = encrypted.getBytes();
        byte[] keyBytes = key.getBytes();
        byte[] decryptedBytes = new byte[encryptedBytes.length];
        for (int i = 0; i < encryptedBytes.length; i++) {
            decryptedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return new String(decryptedBytes);
    }
}