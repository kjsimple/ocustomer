package org.opencustomer.framework.webapp.util.html.renderer;

import java.util.Locale;
import java.util.Properties;

import org.apache.struts.util.MessageResources;

public final class RendererContext {

    private Properties       settings;
    private MessageResources resources;
    private Locale           locale;
    
    public RendererContext(MessageResources resources, Properties styles, Locale locale) {
        this.settings   = styles;
        this.resources = resources;
        this.locale    = locale;
    }

    public Properties getSettings() {
        return settings;
    }

    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public MessageResources getResources() {
        return resources;
    }

    public void setResources(MessageResources resources) {
        this.resources = resources;
    }
}
