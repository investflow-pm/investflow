package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



@Route(value = "home", layout = MainView.class)
@PageTitle("Главная")
public class HomeView extends VerticalLayout {
    public HomeView() {
        // Configure layout
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        // Create instances of InfoWidget
        InfoWidget widget1 = new InfoWidget("Widget 1", "#f2f2f2", "Field 1", "Value 1");
        InfoWidget widget2 = new InfoWidget("Widget 2", "#e0e0e0", "Field 2", "Value 2");
        InfoWidget widget3 = new InfoWidget("Widget 3", "#d9d9d9", "Field 3", "Value 3");
        InfoWidget widget4 = new InfoWidget("Widget 4", "#cccccc", "Field 4", "Value 4");

        // Configure grid layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.setSizeFull();

        gridLayout.add(widget1);
        gridLayout.add(widget2);
        gridLayout.add(widget3);
        gridLayout.add(widget4);

        // Add grid layout to the view
        add(gridLayout);
    }
}
