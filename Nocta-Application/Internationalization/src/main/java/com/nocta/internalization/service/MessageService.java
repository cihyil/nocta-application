package com.nocta.internalization.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class MessageService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Value("${custom.app.locale}")
    private String systemLanguage;

    private Locale locale;

    @PostConstruct
    private void init() {
        locale = new Locale(systemLanguage);
        LocaleContextHolder.setDefaultLocale(locale);
    }

    public String getMessage(String prefix, String key){
        return getMessage(prefix, key, "");
    }

    public String getMessage(String prefix, String key, String value){
        List<String> listOfValues = Collections.singletonList(value);
        return getMessage(prefix, key, listOfValues);
    }

    public String getMessage(String prefix, String key, List<String> args){
        return messageSource.getMessage(concatPrefixAndKey(prefix, key), args.toArray(), locale);
    }

    public String getMessage(String prefix, String key, List<String> args, Locale requestedLocale){
        if (requestedLocale == null){
            return getMessage(prefix, key, args);
        } else {
            return messageSource.getMessage(concatPrefixAndKey(prefix, key), args.toArray(), requestedLocale);
        }
    }

    public String getRequestLocalizedMessage(String prefix, String key){
        return getRequestLocalizedMessage(prefix, key, new ArrayList<>());
    }

    public String getRequestLocalizedMessage(String prefix, String key, List<String> args){
        return getMessage(prefix, key, args, LocaleContextHolder.getLocale());
    }

    private String concatPrefixAndKey(String prefix, String key){
        if(prefix.trim().equalsIgnoreCase("")) {
            return key;
        }
        return prefix.concat(".").concat(key);
    }
}