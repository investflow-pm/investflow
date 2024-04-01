package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "portfolio", layout = MainView.class)
@PageTitle("Портфель")
@RouteAlias(value = "", layout = MainView.class)
public class PortfolioView extends VerticalLayout {

    public PortfolioView() {
    }
}
