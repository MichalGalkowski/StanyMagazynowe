package pl.ara.stanymagazynowe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewProductionAdapter extends RecyclerView.Adapter<ViewProductionAdapter.ProductionProductHolder> {

    private List<ProductionProduct> products = new ArrayList<>();
    private ViewProductionAdapter.onItemClickListener listener;

    @NonNull
    @Override
    public ProductionProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.production_item, parent, false);

        return new ProductionProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionProductHolder holder, int position) {

        ProductionProduct currentProduct = products.get(position);
        holder.name.setText(currentProduct.getName());
        holder.amount.setText("Ilość: " + currentProduct.getAmount());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<ProductionProduct> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public ProductionProduct getProducts(int position) {
        return products.get(position);
    }

    class ProductionProductHolder extends RecyclerView.ViewHolder {

        TextView name, amount;

        public ProductionProductHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productionItemName);
            amount = itemView.findViewById(R.id.productionItemAmount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener!=null && position!=RecyclerView.NO_POSITION) {
                    listener.onItemClick(products.get(position));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(ProductionProduct productionProduct);
    }

    public void setOnItemClickListener(ViewProductionAdapter.onItemClickListener listener) {
        this.listener = listener;
    }
}
