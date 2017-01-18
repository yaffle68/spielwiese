package com.google.gwt.sample.client;

import com.google.gwt.sample.shared.StockPrice;
import com.google.gwt.sample.shared.StockPrice;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>StockPriceService</code>.
 */
public interface StockPriceServiceAsync {
	void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}
