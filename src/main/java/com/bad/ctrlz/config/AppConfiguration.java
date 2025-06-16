package com.bad.ctrlz.config;

import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.component.page.Viewport;

@PWA(
    name = "CtrlZ System", 
    shortName = "CtrlZ", 
    description = "Sistema de gestión CtrlZ",
    iconPath = "icons/ctrlz-logo-144x144.png",
    backgroundColor = "#ffffff", 
    themeColor = "#2c3e50", 
    display = "standalone", 
    startPath = "/"
)
@Meta(name = "theme-color", content = "#2c3e50")
@Viewport("width=device-width, initial-scale=1, user-scalable=yes")
public class AppConfiguration implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        // Agregar favicon manualmente
        settings.addFavIcon("icon", "./icons/ctrlz-logo-144x144.png", "144x144");
        settings.addLink("shortcut icon", "./icons/ctrlz-logo-144x144.png");
    }
}