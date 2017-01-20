package com.google.gwt.sample.components.polymer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.sample.client.*;
import com.google.gwt.sample.components.ErrorHandler;
import com.google.gwt.sample.shared.StockPrice;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.iron.IronIconsetElement;
import com.vaadin.polymer.iron.widget.IronIcons;
import com.vaadin.polymer.iron.widget.IronIconset;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperIconButton;
import com.vaadin.polymer.paper.widget.PaperInput;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by simon.harding on 16.01.2017.
 */
public class PolymerStockDisplayComponent extends Composite {
    private static final int REFRESH_INTERVAL = 5000;

    interface MyUiBinder extends UiBinder<Widget, PolymerStockDisplayComponent> {
    }

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String watchListHeader();
        String watchList();
        String watchListNumericColumn();
        String watchListRemoveColumn();
        String addPanel();
        @ClassName("gwt-Button-remove")
        String gwtButtonRemove();
        String noChange();
        String positiveChange();
        String negativeChange();
        String errorMessage();
        String add();
        String content();
        String dialog();
    }

    @UiField
    Style style;

    @UiField
    Label lastUpdatedLabel;

    @UiField
    Label userMessage;

    @UiField
    Anchor logoutLink;

    @UiField
    Label errorMsgLabel;

    @UiField
    FlexTable stocksTable;

    @UiField
    PaperInput newSymbolTextBox;

    private StockServiceAsync stockService = GWT.create(StockService.class);
    private StockPriceServiceAsync stockPriceService;


    private List<String> stockItems = new ArrayList<>();

    private final LoginInfo loginInfo;


    public PolymerStockDisplayComponent(final LoginInfo login) {

        loginInfo = login;
        initWidget(uiBinder.createAndBindUi(this));
        userMessage.setText(greetingFor(login.getNickname()));
        configureErrorMsg();
        configureStocksTable();
        logoutLink.setHref(loginInfo.getLogoutUrl());

        Timer refreshTimer = new Timer() {
            @Override
            public void run() {
                 refreshWatchList();
            }
        };
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
        loadStocks();
    }

    private String greetingFor(String nickname) {
        return new StringBuilder("Hello ").append(nickname).toString();
    }

    private void configureErrorMsg() {
        errorMsgLabel.setVisible(false);
    }

    private void configureStocksTable() {
        stocksTable.setText(0, 0, "Symbol");
        stocksTable.setText(0, 1, "Price");
        stocksTable.setText(0, 2, "Change");
        stocksTable.setText(0, 3, "Remove");

        // Add styles to elements in the stock list table.
        stocksTable.setCellPadding(6);

        stocksTable.addStyleName(style.watchList());
        stocksTable.getRowFormatter().addStyleName(0, style.watchListHeader());
        setStylesForRow(0);
    }

    private void setStylesForRow(final int row) {
        stocksTable.getCellFormatter().addStyleName(row, 1, style.watchListNumericColumn());
        stocksTable.getCellFormatter().addStyleName(row, 2, style.watchListNumericColumn());
        stocksTable.getCellFormatter().addStyleName(row, 3, style.watchListRemoveColumn());
    }


    @UiHandler("addButton")
    void handleClick(ClickEvent e) {
        doAdd();
    }

//    @UiHandler("newSymbolTextBox")
//    void handleKeyUp(KeyUpEvent e) {
//        if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            doAdd();
//        }
//    }

    private void doAdd() {
        final String symbol = newSymbolTextBox.getValue().toUpperCase().trim();
//        newSymbolTextBox.setFocus(true);

        // Stock code must be between 1 and 10 chars that are numbers,
        // letters, or dots.
        if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert("'" + symbol + "' is not a valid symbol.");
//            newSymbolTextBox.selectAll();
            return;
        }

        if (stockItems.contains(symbol)) {
            return;
        }
        addStock(symbol);
    }

    private void addStock(final String symbol) {
        stockService.addStock(symbol, new AsyncCallback<Void>() {
            public void onFailure(Throwable error) {
                ErrorHandler.handleError(loginInfo, error);
            }

            public void onSuccess(Void ignore) {
                displayStock(symbol);
            }
        });
    }

    private void displayStocks(String[] symbols) {
        for (String symbol : symbols) {
            displayStock(symbol);
        }
    }

    private void displayStock(final String symbol) {
        // Add the stock to the table.
        int row = stocksTable.getRowCount();
        stockItems.add(symbol);
        stocksTable.setText(row, 0, symbol);
        stocksTable.setWidget(row, 2, new Label());
        stocksTable.getCellFormatter().addStyleName(row, 1, style.watchListNumericColumn());
        stocksTable.getCellFormatter().addStyleName(row, 2, style.watchListNumericColumn());
        stocksTable.getCellFormatter().addStyleName(row, 3, style.watchListNumericColumn());

        // Add a button to remove this stock from the table.
        PaperIconButton removeStockButton = new PaperIconButton();

        removeStockButton.setIcon("clear");
        removeStockButton.addStyleDependentName(style.gwtButtonRemove());
        removeStockButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                removeStock(symbol);
            }
        });
        stocksTable.setWidget(row, 3, removeStockButton);

        // Get the stock price.
        refreshWatchList();

    }

    private void removeStock(final String symbol) {
        stockService.removeStock(symbol, new AsyncCallback<Void>() {
            public void onFailure(Throwable error) {
                ErrorHandler.handleError(loginInfo, error);
            }

            public void onSuccess(Void ignore) {
                undisplayStock(symbol);
            }
        });
    }

    private void undisplayStock(String symbol) {
        int removedIndex = stockItems.indexOf(symbol);
        stockItems.remove(removedIndex);
        stocksTable.removeRow(removedIndex + 1);
    }

    private void refreshWatchList() {

        // Initialize the service proxy.
        if (stockPriceService == null) {
            stockPriceService = GWT.create(StockPriceService.class);
        }

        // Set up the callback object.
        AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof DelistedException) {
                    details = "Company '" + ((DelistedException) caught).getSymbol() + "' was delisted";
                }

                errorMsgLabel.setText("Error: " + details);
                errorMsgLabel.setVisible(true);
            }

            public void onSuccess(StockPrice[] result) {
                updateTable(result);
            }
        };

        stockPriceService.getPrices(stockItems.toArray(new String[] {}), callback);
    }

    private void updateTable(StockPrice[] prices) {
        for (int i = 0; i < prices.length; i++) {
            updateTable(prices[i]);
        }
    }

    private void updateTable(StockPrice stockPrice) {
        // Make sure the stock is still in the stock table.
        if (!stockItems.contains(stockPrice.getSymbol())) {
            return;
        }

        int row = stockItems.indexOf(stockPrice.getSymbol()) + 1;

        // Format the data in the Price and Change fields.
        String priceText = NumberFormat.getFormat("#,##0.00").format(stockPrice.getPrice());

        double change = stockPrice.getChange();
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(change);
        String changePercentText = changeFormat.format(stockPrice.getChangePercent());

        // Populate the Price and Change fields with new data.
        stocksTable.setText(row, 1, priceText);
        Label changeWidget = (Label) stocksTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + changePercentText + "%)");

        String styleName = style.noChange();
        if (change > 0) {
            styleName =  style.positiveChange();
        } else if (change < 0) {
            styleName = style.negativeChange();
        }

        changeWidget.setStyleName(styleName);

        // stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
        // + "%)");

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLabel.setText("Last updated: " + dateFormat.format(new Date()));

        // Clear any errors.
        errorMsgLabel.setVisible(false);
    }

    private void loadStocks() {
        stockService.getStocks(new AsyncCallback<String[]>() {
            public void onFailure(Throwable error) {
                ErrorHandler.handleError(loginInfo, error);
            }

            public void onSuccess(String[] symbols) {
                displayStocks(symbols);
            }
        });
    }

}
