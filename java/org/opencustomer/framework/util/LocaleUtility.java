/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.log4j.Logger;

public final class LocaleUtility {
    
    private final static Logger log = Logger.getLogger(LocaleUtility.class);
    
    private static LocaleUtility instance;
    
    private List<Locale> availableLocales = new ArrayList<Locale>();
    
    private LocaleUtility() {        
    }
    
    public static LocaleUtility getInstance() {
        if(instance == null)
            instance = new LocaleUtility();
        
        return instance;
    }
    
    public static Locale parseLocale(String str) {
        String[] parts = str.split("_", 3);
        
        String language = "";
        String country = "";
        String variant = "";
        
        if(parts.length >= 1)
            language = parts[0];
        if(parts.length >= 2)
            country = parts[1];
        if(parts.length >= 3)
            variant = parts[2];
        
        return new Locale(language, country, variant);
    }

    public void load(String name) {
        final String start  = name+"_";
        final String ending = ".properties";
        
        if(log.isDebugEnabled())
            log.debug("file default locale: "+name+ending);
        
        File defaultLocale = new File(LocaleUtility.class.getClassLoader().getResource(name+ending).getFile());
        
        File[] localeFiles = defaultLocale.getParentFile().listFiles(new FileFilter() {
            public boolean accept(File file) {
                if(file.getName().startsWith(start) && file.getName().endsWith(ending)) {
                    return true;
                } else {
                    return false;
                }
            };
        });
        
        for(File localeFile : localeFiles) {
            String localeName = localeFile.getName();
            localeName = localeName.replaceFirst(start, "");
            localeName = localeName.replaceFirst(ending, "");

            Locale locale = LocaleUtility.parseLocale(localeName);
            if(locale != null)
                availableLocales.add(locale);
        }
    }     
    
    public List<Locale> getLocales() {
        return availableLocales;
    }
    
    public static class LocaleBean implements Comparable {
        private String localizedName;
        private Locale locale;
        
        public LocaleBean() {            
        }

        public LocaleBean(Locale locale, String localizedName) {     
            this.locale = locale;
            this.localizedName = localizedName;
        }

        public final Locale getLocale()
        {
            return locale;
        }

        public final void setLocale(Locale locale)
        {
            this.locale = locale;
        }

        public final String getLocalizedName()
        {
            return localizedName;
        }

        public final void setLocalizedName(String localizedName)
        {
            this.localizedName = localizedName;
        }

        public int compareTo(Object obj)
        {
            int compare = -1;

            if (obj == null)
                compare = -1;
            else if (this == obj)
                compare = 0;
            else if (!(obj instanceof LocaleBean))
                compare = -1;
            else
            {
                LocaleBean castObj = (LocaleBean) obj;

                CompareToBuilder builder = new CompareToBuilder();

                builder.append(this.getLocalizedName(), castObj.getLocalizedName());

                compare = builder.toComparison();
            }

            return compare;
        }
    }
}
