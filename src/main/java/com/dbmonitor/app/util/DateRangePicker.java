package com.dbmonitor.app.util;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.Text;

public class DateRangePicker extends CustomField<LocalDateRange> {
    private final DatePicker start = new DatePicker();
    private final DatePicker end = new DatePicker();

    public DateRangePicker(String label) {
        super();
        setLabel(label);
        configurePickers();
        add(start, new Text(" – "), end);
    }

    private void configurePickers() {
        start.setPlaceholder("Start date");
        start.getElement().executeJs("this.focusElement.setAttribute('title', 'Start date')");
        end.setPlaceholder("End date");
        end.getElement().executeJs("this.focusElement.setAttribute('title', 'End date')");

        // Ensure valid range
        start.addValueChangeListener(e -> {
            if (e.getValue() != null && end.getValue() != null && e.getValue().isAfter(end.getValue())) {
                end.setValue(e.getValue());
            }
        });
        end.addValueChangeListener(e -> {
            if (e.getValue() != null && start.getValue() != null && e.getValue().isBefore(start.getValue())) {
                start.setValue(e.getValue());
            }
        });
    }

    @Override
    protected LocalDateRange generateModelValue() {
        return new LocalDateRange(start.getValue(), end.getValue());
    }

    @Override
    protected void setPresentationValue(LocalDateRange dateRange) {
        start.setValue(dateRange != null ? dateRange.getStartDate() : null);
        end.setValue(dateRange != null ? dateRange.getEndDate() : null);
    }
}
