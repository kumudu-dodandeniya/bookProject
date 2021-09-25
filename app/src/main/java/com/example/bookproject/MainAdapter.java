package com.example.bookproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull @NotNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myViewHolder holder, final int position, @NonNull @NotNull MainModel model) {

        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());

      //  Glide.with(holder.img.getContext()).load(model.getBurl()).placeholder(R.drawable.common_google_signin_btn_icon_dark)
              //  .circleCrop().error(R.drawable.common_google_signin_btn_icon_dark_normal)
                //.into(holder.img);
        Picasso.get().load(model.getBurl()).into(holder.img);


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200).create();
                //dialogPlus.show();

                View view = dialogPlus.getHeaderView();

                EditText name = view.findViewById(R.id.txtName);
                EditText description = view.findViewById(R.id.txtDescription);
                EditText price = view.findViewById(R.id.txtPrice);
                EditText burl = view.findViewById(R.id.txtImageUrl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                description.setText(model.getDescription());
                price.setText(model.getPrice());
                burl.setText(model.getBurl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                         map.put("name", name.getText().toString());
                        map.put("description", description.getText().toString());
                        map.put("price", price.getText().toString());
                        map.put("burl", burl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("book").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(),"Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(holder.name.getContext(),"Error While Updating..", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();

                            }
                        });

                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you Sure");
                builder.setMessage("Deleted data can't be Undo..");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("book")
                                .child(getRef(position).getKey()).removeValue();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });




    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name,description,price;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            description = (TextView) itemView.findViewById(R.id.descriptiontext);
            price = (TextView) itemView.findViewById(R.id.pricetext);

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);

        }



    }
}
