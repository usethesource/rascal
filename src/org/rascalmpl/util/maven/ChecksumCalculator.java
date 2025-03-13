package org.rascalmpl.util.maven;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculator {
    private static final int BUFFER_SIZE = 1024;

    public static String calculateChecksum(Path path, String algorithm) throws IOException {
        try (FileInputStream input = new FileInputStream(path.toFile())) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance(algorithm);
            }
            catch (NoSuchAlgorithmException e) {
                // Should not happen: SHA1 and MD5 should be supported on all systems.
                throw new RuntimeException(e);
            }
  
            byte[] bytes = new byte[BUFFER_SIZE];
            int bytesRead = 0; 
            while (true) {
                bytesRead = input.read(bytes);
                if (bytesRead == -1) {
                    break;
                }
                digest.update(bytes, 0, bytesRead);
            }

            byte[] hashBytes = digest.digest();

            StringBuffer hashBuffer = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                hashBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
             
            return hashBuffer.toString();
        }
    }
}
