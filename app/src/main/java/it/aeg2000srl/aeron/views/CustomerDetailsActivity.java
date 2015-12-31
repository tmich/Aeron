package it.aeg2000srl.aeron.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Cart;
import it.aeg2000srl.aeron.core.CartItem;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.factories.CartFactory;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.FavoriteProductsArrayAdapter;

public class CustomerDetailsActivity extends AppCompatActivity {
    Customer customer;
    CustomerRepository customerRepository;
    TextView lblCustomerName;
    TextView lblCustomerAddress;
    TextView lblCustomerCity;
    Button btnNewOrder;
    Button btnNewOrderIcewer;
    Button btnPriceList;
//    Button btnWaitingOrders;
//    CheckBox chk;
    ImageView imgCart;
    ListView lstFavorites;
    TextView empty_favorites;
    UseCasesService service;
    Menu menu;
    Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        customerRepository = new CustomerRepository();
        lblCustomerName = (TextView)findViewById(R.id.lblCustomerName);
        lblCustomerAddress = (TextView)findViewById(R.id.lblCustomerAddress);
        lblCustomerCity = (TextView)findViewById(R.id.lblCustomerCity);
        btnNewOrder = (Button)findViewById(R.id.btnNewOrder);
        btnNewOrderIcewer = (Button)findViewById(R.id.btnNewOrderIcewer);
        btnPriceList = (Button)findViewById(R.id.btnPriceList);
//        btnWaitingOrders = (Button)findViewById(R.id.btnWaitingOrders);
        lstFavorites = (ListView)findViewById(R.id.lstFavorites);
        empty_favorites = (TextView)findViewById(R.id.empty_favorites);
        service = new UseCasesService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getIntent() != null) {
            Intent intent = getIntent();
            long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
            customer = customerRepository.findById(customerId);
            update();
        } else {
            finish();
        }

        // Carrello
        cart.setCustomer(customer);
        // Carrello - Fine

        // creazione nuovo ordine
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, OrderActivity.class);
                intent.setAction(getString(R.string.actionNewOrder));
                intent.putExtra(getString(R.string.customerId), customer.getId());

                //carrello
                Bundle bundle = new Bundle();
                bundle.putSerializable("cart", cart);
                intent.putExtras(bundle);
                //intent.putStringArrayListExtra(getString(R.string.cart), selectedCodes);
                startActivity(intent);
            }
        });

        // creazione nuovo ordine Icewer
        btnNewOrderIcewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, OrderIcewerActivity.class);
                intent.setAction(getString(R.string.actionNewOrder));
                intent.putExtra(getString(R.string.customerId), customer.getId());
                startActivity(intent);
            }
        });

        // listino cliente
        btnPriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, PriceListActivity.class);
                intent.putExtra(getString(R.string.customerId), customer.getId());
                startActivity(intent);
            }
        });

        // lista prodotti preferiti

        lstFavorites.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedIndex = i;
                //final CartItem cartItem = ( cart.getItems().get(selectedIndex));
                FavoriteProduct fav = getFavoritesAdapter().getItem(i);
                if (fav.isSelected()) {
                    ProductRepository productRepository = new ProductRepository();
                    IProduct product = productRepository.findById(fav.getProductId());
                    final CartItem _itm = new CartItem(product, 1, "");
                    final CartItem cartItem = (cart.getItems().contains(_itm) ? cart.getItems().get(cart.getItems().indexOf(_itm)) : null);

                    if (cartItem != null) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(CustomerDetailsActivity.this, view);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup_menu_cart_item, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(OrderActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                if (item.getItemId() == R.id.deleteCartItem) {
                                    cart.getItems().remove(cartItem);
                                    getFavoritesAdapter().getItem(selectedIndex).setSelected(false);
                                    getFavoritesAdapter().notifyDataSetChanged();
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                }
                return true;
            }
        });











        lstFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedIndex = i;
                FavoriteProduct fav = getFavoritesAdapter().getItem(i);
//                chk = ((CheckBox) view.findViewById(R.id.checkBox1));
                //imgCart = (ImageView) view.findViewById(R.id.imgCart);

                // Carrello
                ProductRepository productRepository = new ProductRepository();
                IProduct product = productRepository.findById(fav.getProductId());
                final CartItem _itm = new CartItem(product, 1, "");
                final CartItem item = ( cart.getItems().contains(_itm) ? cart.getItems().get(cart.getItems().indexOf(_itm)) : _itm);
                final CartItemDialog dialog = new CartItemDialog(CustomerDetailsActivity.this);
                //dialog.setTitle("Modifica");
                dialog.setProductName(item.getProductName());
                dialog.setQuantity(item.getQuantity());
                dialog.setNotes(item.getNotes());
                //dialog.setDiscount(item.getDiscount());
                // sconto?
                String discount ="";
                UseCasesService useCasesService = new UseCasesService();
                DiscountProduct discountProduct = useCasesService.getDiscountedProductForCustomerId(customer.getId(), item.getProductCode());
                if(discountProduct != null) {
                    discount = discountProduct.getDiscount().getDescription();
                    dialog.setDiscount(discount);
                }
                //fav.setSelected(true);

                dialog.setOnOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //cart.getItems().remove(item);
                        item.setDiscount(dialog.getDiscount());
                        item.setNotes(dialog.getNotes());
                        item.setQuantity(dialog.getQuantity());
                        item.setProductName(dialog.getProductName());
                        //cart.getItems().add(item);
                        int pos = cart.getItems().indexOf(item);
                        if (pos >= 0)
                            cart.getItems().set(pos, item);
                        else
                            cart.getItems().add(item);
                        //chk.setChecked(true);
                        //imgCart.setVisibility(View.VISIBLE);
                        getFavoritesAdapter().getItem(selectedIndex).setSelected(true);
                        getFavoritesAdapter().notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.setOnCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        chk.setChecked(false);
                        //imgCart.setVisibility(View.INVISIBLE);
                        //cart.getItems().remove(item);
                        //getFavoritesAdapter().getItem(selectedIndex).setSelected(false);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                // Carrello - Fine
            }
        });


    }

    protected void update() {
        lblCustomerName.setText(customer.getName());
        lblCustomerAddress.setText(customer.getAddress());
        lblCustomerCity.setText(customer.getCity());

        // lista prodotti preferiti
        lstFavorites.setAdapter(new FavoriteProductsArrayAdapter(this, service.getFavorites(customer)));
        empty_favorites.setVisibility( (lstFavorites.getCount() == 0 ? View.VISIBLE : View.INVISIBLE) );

        // carrello
        FavoriteProductsArrayAdapter adapt = getFavoritesAdapter();
        ProductRepository productRepository = new ProductRepository();

        for (int i =0; i< lstFavorites.getCount(); i++ ) {
            FavoriteProduct fav = adapt.getItem(i);
            IProduct product = productRepository.findById(fav.getProductId());
            CartItem _itm = new CartItem(product, 1, "");
            fav.setSelected(cart.getItems().contains(_itm));
        }

        //ordini in attesa
        updateMenuActions();
    }

    void updateMenuActions() {
        if (this.menu != null) {
            MenuItem menuItem = this.menu.findItem(R.id.action_orders_waiting);
            menuItem.setVisible(service.getWaitingOrders(customer).size() > 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    protected FavoriteProductsArrayAdapter getFavoritesAdapter() {
        return (FavoriteProductsArrayAdapter)lstFavorites.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_details, menu);
        this.menu = menu;

        updateMenuActions();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_orders_history) {
            Intent intent = new Intent(CustomerDetailsActivity.this, OrdersHistoryActivity.class);
            intent.putExtra(getString(R.string.customerId), customer.getId());
            startActivity(intent);
        } else if (id == R.id.action_orders_waiting) {
            Intent intent = new Intent(CustomerDetailsActivity.this, WaitingOrdersActivity.class);
            intent.putExtra(getString(R.string.customerId), customer.getId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
