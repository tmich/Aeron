package it.aeg2000srl.aeron.core;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public interface IOrderSender {
    void send(IOrder order) throws IOException, JSONException;
}
