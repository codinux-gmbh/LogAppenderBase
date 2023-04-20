package net.codinux.log.config.quarkus.converter;

import net.codinux.log.config.quarkus.DefaultConfigValues;

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