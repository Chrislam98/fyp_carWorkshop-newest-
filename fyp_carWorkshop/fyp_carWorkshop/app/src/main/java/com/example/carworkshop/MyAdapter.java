package com.example.carworkshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private UsersData usersData;
    private TextView ownerID;

    Context context;
    ArrayList<Owner_service> owner_services;

    public MyAdapter(Context c, ArrayList<Owner_service> os){
        context = c;
        owner_services = os;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.service_info,parent,false));
    }
//25.17
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.serprice.setText(owner_services.get(position).getPrice());
        holder.sername.setText(owner_services.get(position).getServicename());
        holder.sercat.setText(owner_services.get(position).getCategory());
        holder.serdesc.setText(owner_services.get(position).getSer_desc());
        holder.sertime.setText(owner_services.get(position).getTime());
        holder.sownerid.setText(owner_services.get(position).getOwnerId());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=
                        DialogPlus.newDialog(holder.sername.getContext()).setContentHolder
                                (new ViewHolder(R.layout.dialogcontext)).setExpanded(true,1100).create();
                View myview = dialogPlus.getHolderView();
                final TextView sownerid = myview.findViewById(R.id.uownerID);
                final TextView sername = myview.findViewById(R.id.sname);
                final TextView sercat = myview.findViewById(R.id.scat);
                final EditText price = myview.findViewById(R.id.sprice);
                final EditText min = myview.findViewById(R.id.smin);
                final EditText serdesc = myview.findViewById(R.id.sdesc);
                Button submit = myview.findViewById(R.id.usubmit);

                sownerid.setText(owner_services.get(position).getOwnerId());
                sername.setText(owner_services.get(position).getServicename());
                sercat.setText(owner_services.get(position).getCategory());
                price.setText(owner_services.get(position).getPrice());
                min.setText(owner_services.get(position).getTime());
                serdesc.setText(owner_services.get(position).getSer_desc());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("ownerId", sownerid.getText().toString());
                        map.put("servicename", sername.getText().toString());
                        map.put("price", price.getText().toString());
                        map.put("time", min.getText().toString());
                        map.put("ser_desc", serdesc.getText().toString());

                        String ownerId = sownerid.getText().toString();
                        String name = sername.getText().toString();

                        FirebaseDatabase.getInstance().getReference("Owner_service").child(ownerId).child(name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });
      //28.44
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.sername.getContext(),R.style.MyDialogTheme);
                builder.setTitle("delete panel");
                builder.setMessage("Delete....?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DialogPlus dialogPlus=DialogPlus.newDialog(holder.sername.getContext()).setContentHolder(new ViewHolder(R.layout.dialogcontext)).setExpanded(true,1100).create();
                        View myview = dialogPlus.getHolderView();
                        final TextView sownerid = myview.findViewById(R.id.uownerID);
                        final TextView sername = myview.findViewById(R.id.sname);
                        sownerid.setText(owner_services.get(position).getOwnerId());
                        sername.setText(owner_services.get(position).getServicename());
                        String ownerId = sownerid.getText().toString();
                        String name = sername.getText().toString();
                        FirebaseDatabase.getInstance().getReference("Owner_service").child(ownerId).child(name).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return owner_services.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView sername,serprice,sertime,sownerid,sercat,serdesc;
        Button edit,delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            sername = (TextView) itemView.findViewById(R.id.sername);
            sercat = (TextView) itemView.findViewById(R.id.cat);
            serdesc = (TextView) itemView.findViewById(R.id.desc);
            serprice = (TextView) itemView.findViewById(R.id.serprice);
            sertime = (TextView) itemView.findViewById(R.id.sertime);
            sownerid = (TextView) itemView.findViewById(R.id.sownerID);

            edit = (Button)itemView.findViewById(R.id.update);
            delete = (Button)itemView.findViewById(R.id.delete);

        }
    }


}
