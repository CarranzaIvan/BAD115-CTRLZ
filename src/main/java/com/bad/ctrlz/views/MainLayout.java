package com.bad.ctrlz.views;

import com.bad.ctrlz.views.components.Navbar;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("")
public class MainLayout extends AppLayout {

    public MainLayout() {
        Navbar navbar = new Navbar();
        addToNavbar(navbar);
    }
}
