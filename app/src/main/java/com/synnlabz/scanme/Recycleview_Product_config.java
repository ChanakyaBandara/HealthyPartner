package com.synnlabz.scanme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Recycleview_Product_config {
    private Context mContext;
    private ProductAddaptor mProductAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Product> products, List<String> keys){
        mContext = context;
        mProductAddaptor = new ProductAddaptor(products,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mProductAddaptor);
    }

    class InsectItemView extends RecyclerView.ViewHolder{


        private TextView ProductName;
        private TextView ProductDate;
        private ImageView Product_Img;


        private String key;

        public InsectItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.item_products,parent,false));

            ProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            ProductDate = (TextView) itemView.findViewById(R.id.txtdate);
            Product_Img = (ImageView) itemView.findViewById(R.id.productImage);

        }

        public void bind(Product  product,String key){
            ProductName.setText(product.getName());
            ProductDate.setText(product.getVeg());
            //Glide.clear(Product_Img);
            Glide.with(mContext).load(product.getImgURL()).into(Product_Img);
            this.key = key;
        }



    }

    class ProductAddaptor extends RecyclerView.Adapter<InsectItemView>{
        private List<Product> mProduct;
        private List<String> mKey;

        public ProductAddaptor(List<Product> mProduct, List<String> mKey) {
            this.mProduct = mProduct;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public InsectItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new InsectItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull InsectItemView holder, int position) {
            holder.bind(mProduct.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mProduct.size();
        }


    }

}
