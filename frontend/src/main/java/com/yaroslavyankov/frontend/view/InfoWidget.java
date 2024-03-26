package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InfoWidget extends Div {

    public InfoWidget(String title, String backgroundColor, String fieldName, String fieldValue) {
        addClassName("info-widget");

        setWidth("50%");
        setHeight("50%");
        getStyle().set("border-radius", "8px");
        getStyle().set("background-color", backgroundColor);

        // Create layout for content
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(true);

        // Add title, field name, and value
        contentLayout.add(new H3(title));
        contentLayout.add(new Paragraph(fieldName + ": " + fieldValue));

        // Add content layout to the widget
        add(contentLayout);
    }
}
