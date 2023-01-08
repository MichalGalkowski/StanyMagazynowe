package pl.ara.stanymagazynowe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_item, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        Product currentProduct = products.get(position);
        holder.name.setText(currentProduct.getName());
        holder.amount.setText("Ilość: " + currentProduct.getAmount());
        holder.lowAmount.setText("Mała ilość: " + currentProduct.getLowAmount());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void filterList(List<Product> filteredList) {
        products = filteredList;
        notifyDataSetChanged();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProducts(int position) {
        return products.get(position);
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        TextView name, amount, lowAmount;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productItemName);
            amount = itemView.findViewById(R.id.productItemAmount);
            lowAmount = itemView.findViewById(R.id.productItemLowAmount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener!=null && position!=RecyclerView.NO_POSITION) {
                    listener.onItemClick(products.get(position));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
