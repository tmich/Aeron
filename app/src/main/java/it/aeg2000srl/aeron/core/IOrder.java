package it.aeg2000srl.aeron.core;

import java.util.Date;
import java.util.List;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public interface IOrder {
    enum OrderType {
        NORMAL,
        ICEWER
    }

    void add(IProduct product, int quantity, String notes, String discount);

    void add(IOrderItem orderItem);

    void remove(IOrderItem item);

    long getId();

    Date getCreationDate();

    List<IOrderItem> getItems();

    void setNotes(String notes);

    long getCustomerId();

    void setId(long newId);

    void setSentDate(Date date);

    String getNotes();

    void setCreationDate(Date creationDate);

    Date getSentDate();

    void setCustomerId(long customerId);

    OrderType getType();
}
