package com.yaroslavyankov.frontend.view.home;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;

import java.util.Map;

public class SectorChartWidget extends InfoWidget {

    public SectorChartWidget(String title, Map<String, Long> sectorMap) {
        super(title);
        addClassName("sector-widget");

        Div chartContainer = new Div();

        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();

        int sumOfValues = sectorMap.values().stream()
                .mapToInt(Long::intValue)
                .sum();
        if (sumOfValues == 0) {
            DataSeriesItem item = new DataSeriesItem("Ничего", 100);
            series.add(item);
        } else {
            for (String key : sectorMap.keySet()) {
                DataSeriesItem item = new DataSeriesItem(key, getSectorPercent(sectorMap.get(key), sumOfValues));
                series.add(item);
            }
        }

        conf.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);



        chartContainer.add(chart);
        add(chartContainer);

//        chart.addPointLegendItemClickListener(event -> {
//            showNotification("Legend item click" + " : " + event.getItemIndex()
//                    + " : " + event.getItem().getName());
//        });
    }

    private Number getSectorPercent(Long sectorCount, int sumOfValues) {
        return (double) (sectorCount * 100 / sumOfValues);
    }
}

