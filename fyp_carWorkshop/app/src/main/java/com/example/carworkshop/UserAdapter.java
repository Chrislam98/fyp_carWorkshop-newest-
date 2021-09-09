package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewholder> {
    Context context;
    List<UsersData> usersDatas;

    public UserAdapter(Context c1, List<UsersData> ud){
        context = c1;
        usersDatas = ud;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.user_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, final int position) {
        holder.uname.setText(usersDatas.get(position).getUsername());
        holder.email.setText(usersDatas.get(position).getEmail());
        holder.usstatus.setText(usersDatas.get(position).getStatus());
        holder.usid.setText(usersDatas.get(position).getUserId());
        holder.usgender.setText(usersDatas.get(position).getGender());
        holder.usimgurl.setText(usersDatas.get(position).getImageURL());
        holder.usmobile.setText(usersDatas.get(position).getMobile());
        holder.usrole.setText(usersDatas.get(position).getRole());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.uname.getContext()).setContentHolder(new ViewHolder(R.layout.userdialogcontext)).setExpanded(true,1100).create();
                View myview = dialogPlus.getHolderView();
                final TextView name= myview.findViewById(R.id.name);
                final TextView email = myview.findViewById(R.id.email);
                final TextView status = myview.findViewById(R.id.usdstatus);
                final TextView  usdgender = myview.findViewById(R.id.usdgender);
                final TextView  usid = myview.findViewById(R.id.usdid);
                final TextView usimgurl = myview.findViewById(R.id.usdimgurl);
                final TextView usmobile = myview.findViewById(R.id.usdmobile);
                final TextView usrole = myview.findViewById(R.id.usdrole);

                Button block = myview.findViewById(R.id.usdblock);
                Button allow = myview.findViewById(R.id.usdallow);

                name.setText(usersDatas.get(position).getUsername());
                email.setText(usersDatas.get(position).getEmail());
                status.setText(usersDatas.get(position).getStatus());
                usdgender.setText(usersDatas.get(position).getGender());
                usid.setText(usersDatas.get(position).getUserId());
                usimgurl.setText(usersDatas.get(position).getImageURL());
                usmobile.setText(usersDatas.get(position).getMobile());
                usrole.setText(usersDatas.get(position).getRole());

                dialogPlus.show();

                block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bk = "block";
                        Map<String,Object> map = new HashMap<>();
                        map.put("email",email.getText().toString());
                        map.put("gender",usdgender.getText().toString());
                        map.put("imageUrl",usimgurl.getText().toString());
                        map.put("mobile",usmobile.getText().toString());
                        map.put("userId",usid.getText().toString());
                        map.put("role",usrole.getText().toString());
                        map.put("status",bk);
                        map.put("username",name.getText().toString());
                        String userId = usid.getText().toString();

                        FirebaseDatabase.getInstance().getReference("Users").child(userId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                allow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alw = "allow";
                        Map<String,Object> map = new HashMap<>();
                        map.put("email",email.getText().toString());
                        map.put("gender",usdgender.getText().toString());
                        map.put("imageUrl",usimgurl.getText().toString());
                        map.put("mobile",usmobile.getText().toString());
                        map.put("userId",usid.getText().toString());
                        map.put("role",usrole.getText().toString());
                        map.put("status",alw);
                        map.put("username",name.getText().toString());
                        String userId = usid.getText().toString();

                        FirebaseDatabase.getInstance().getReference("Users").child(userId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }

    @Override
    public int getItemCount() {
        return usersDatas.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder
    {
        TextView uname,email,usstatus,usid,usgender,usimgurl,usmobile,usrole;
        Button edit;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.uname);
            email = (TextView) itemView.findViewById(R.id.email);
            usstatus = (TextView) itemView.findViewById(R.id.usstatus);
            usid = (TextView) itemView.findViewById(R.id.usid);
            usgender = (TextView) itemView.findViewById(R.id.usgender);
            usimgurl = (TextView) itemView.findViewById(R.id.usimgurl);
            usmobile = (TextView) itemView.findViewById(R.id.usmobile);
            usrole = (TextView) itemView.findViewById(R.id.usrole);
            edit = (Button)itemView.findViewById(R.id.change);

        }
    }

}
