package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("/styles/style.css")
public class GridLayout extends Div {

    public GridLayout() {
        addClassName("grid-layout");
    }
}
