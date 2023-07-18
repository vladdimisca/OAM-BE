package com.oam.model.converter;

import com.oam.util.EncryptionUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ApartmentCodeConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String code) {
        return EncryptionUtil.encryptAES(code);
    }

    @Override
    public String convertToEntityAttribute(String encryptedCode) {
        return EncryptionUtil.decryptAES(encryptedCode);
    }
}
