package com.example.carworkshop;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
//public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ImageViewHolder> {
public class ImgWorkshopadapter extends RecyclerView.Adapter<ImgWorkshopadapter.ImageViewHolder> {

    private Context mContext;
    private List<Icon> mIcons;
    private IconAdapter.OnItemClickListener mListener;

    public ImgWorkshopadapter(Context context,List<Icon> icons){
        mContext = context;
        mIcons = icons;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.wsimage,parent,false);
        return new ImgWorkshopadapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Icon iconCurrent = mIcons.get(position);
        holder.textViewName.setText(iconCurrent.getiName());
        Picasso.get().load(iconCurrent.getiUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageViewIcon;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.icon_name);
            imageViewIcon = itemView.findViewById(R.id.icon_img);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select action");
            // MenuItem doWhatever = menu.add(Menu.NONE,1,1,"Do abc");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");

            // doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1 :
                            mListener.onWhatEverClick(position);
                            return true;

                        case 2 :
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(IconAdapter.OnItemClickListener listener){
        mListener = listener;
    }
}
