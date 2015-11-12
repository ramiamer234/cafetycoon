package org.vaadin.samples.cafetycoon.ui.dashboard;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import org.vaadin.samples.cafetycoon.domain.CafeStatus;
import org.vaadin.samples.cafetycoon.ui.dashboard.model.OverviewModel;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyConverter;
import org.vaadin.samples.cafetycoon.ui.utils.MoneyUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CafeTableView extends VerticalLayout {

    private static final String COL_CAFE = "cafe";
    private static final String COL_24INCOME = "24income";
    private static final String COL_STATUS = "status";

    private OverviewModel model;
    private Label balance;
    private Label income;
    private Grid cafeGrid;
    private IndexedContainer container;
    private final PropertyChangeListener balanceUpdatedListener = this::balanceUpdated;
    private final PropertyChangeListener incomeUpdatedListener = this::incomeUpdated;
    private final PropertyChangeListener cafeStatusUpdated = this::cafeStatusUpdated;

    public CafeTableView(OverviewModel model) {
        setSizeFull();
        setSpacing(true);
        setMargin(true);

        HorizontalLayout financialLayout = new HorizontalLayout();
        financialLayout.setWidth("100%");
        addComponent(financialLayout);

        balance = new Label();
        balance.setCaption("Balance");
        financialLayout.addComponent(balance);
        income = new Label();
        income.setCaption("24h income");
        financialLayout.addComponent(income);

        container = createContainer();
        cafeGrid = new Grid(container);
        cafeGrid.setSizeFull();
        cafeGrid.getColumn(COL_24INCOME).setConverter(new MoneyConverter());
        addComponent(cafeGrid);
        setExpandRatio(cafeGrid, 1.0f);

        this.model = model;
    }

    @Override
    public void attach() {
        super.attach();
        model.addPropertyChangeListener(OverviewModel.PROP_TOTAL_BALANCE, balanceUpdatedListener);
        model.addPropertyChangeListener(OverviewModel.PROP_TOTAL_INCOME_24H, incomeUpdatedListener);
        model.addPropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusUpdated);
        // Update the UI with latest model state
        balanceUpdated(null);
        incomeUpdated(null);
        cafeStatusUpdated(null);
    }

    @Override
    public void detach() {
        model.removePropertyChangeListener(OverviewModel.PROP_TOTAL_BALANCE, balanceUpdatedListener);
        model.removePropertyChangeListener(OverviewModel.PROP_TOTAL_INCOME_24H, incomeUpdatedListener);
        model.removePropertyChangeListener(OverviewModel.PROP_CURRENT_STATUS_AND_INCOME, cafeStatusUpdated);
        super.detach();
    }

    private IndexedContainer createContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(COL_CAFE, String.class, "");
        container.addContainerProperty(COL_24INCOME, BigDecimal.class, BigDecimal.ZERO);
        container.addContainerProperty(COL_STATUS, CafeStatus.class, null);
        return container;
    }

    private void balanceUpdated(PropertyChangeEvent event) {
        balance.setValue(MoneyUtils.formatMoney(model.getTotalBalance()));
    }

    private void incomeUpdated(PropertyChangeEvent event) {
        income.setValue(MoneyUtils.formatMoney(model.getTotalIncome24h()));
    }

    private void cafeStatusUpdated(PropertyChangeEvent event) {
        if (event instanceof IndexedPropertyChangeEvent) {
            // Only a single cafe has been updated
            if (event.getNewValue() == null) {
                OverviewModel.CafeStatusAndIncomeDTO dto = (OverviewModel.CafeStatusAndIncomeDTO) event.getOldValue();
                container.removeItem(dto.getCafe());
            } else {
                OverviewModel.CafeStatusAndIncomeDTO dto = (OverviewModel.CafeStatusAndIncomeDTO) event.getNewValue();
                Item item;
                if (event.getOldValue() == null) {
                    item = container.addItem(dto.getCafe());
                } else {
                    item = container.getItem(dto.getCafe());
                }
                updateContainerItem(item, dto);
            }
        } else {
            // All cafes have been updated
            container.removeAllItems();
            for (OverviewModel.CafeStatusAndIncomeDTO dto : model.getCurrentStatusAndIncome()) {
                Item item = container.addItem(dto.getCafe());
                updateContainerItem(item, dto);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateContainerItem(Item item, OverviewModel.CafeStatusAndIncomeDTO dto) {
        item.getItemProperty(COL_CAFE).setValue(dto.getCafe().getName());
        item.getItemProperty(COL_24INCOME).setValue(dto.getIncome24h());
        item.getItemProperty(COL_STATUS).setValue(dto.getStatus());
    }
}