package dev.jonas.library.security.encryption;

import dev.jonas.library.exceptions.security.DecryptionFailedException;
import dev.jonas.library.exceptions.security.EncryptionFailedException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@Converter
@Slf4j
public class StringEncryptionConverter implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String SECRET = "MySuperSecretKey";

    private final SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), AES);

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Hex.encodeHexString(cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Encryption error", e);
            throw new EncryptionFailedException("Encryption failed");
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Hex.decodeHex(dbData.toCharArray());
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption error", e);
            throw new DecryptionFailedException("Decryption failed");
        }
    }
}