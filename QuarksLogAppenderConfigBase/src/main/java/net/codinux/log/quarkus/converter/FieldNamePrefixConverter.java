package net.codinux.log.quarkus.converter;

import net.codinux.log.quarkus.config.DefaultConfigValues;

import org.eclipse.microprofile.config.spi.Converter;

public class FieldNamePrefixConverter implements Converter<String> {

    @Override
    public String convert(String value) {
        if (value == null || value.isBlank() || DefaultConfigValues.DisableFieldNamePrefix.equalsIgnoreCase(value)) {
            return "";
        }

        return value.trim();
    }

}