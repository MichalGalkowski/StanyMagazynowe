package pl.ara.stanymagazynowe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.AddProductHolder> {

    private List<Product> products = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public AddProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_production_item, parent, false);

        return new AddProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductHolder holder, int position) {

        Product currentProduct = products.get(position);
        holder.name.setText(currentProduct.getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void filterList(List<Product> filteredList) {
        products = filteredList;
        notifyDataSetChanged();
    }

    public Product getProducts(int position) {
        return products.get(position);
    }

    class AddProductHolder extends RecyclerView.ViewHolder {

        TextView name;

        public AddProductHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.addProductionProductName);
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
