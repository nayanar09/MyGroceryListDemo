package com.nayana.example.mygrocerylist.UserInterface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nayana.example.mygrocerylist.Activities.DetailsActivity;
import com.nayana.example.mygrocerylist.Data.DataBaseHandler;
import com.nayana.example.mygrocerylist.Model.Grocery;
import com.nayana.example.mygrocerylist.R;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog; //A subclass of Dialog that can display one, two or three buttons.
    private LayoutInflater inflater; //Instantiates a layout XML file into its corresponding View  objects.

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        //public abstract class LayoutInflater  extends Object
        //Instantiates a layout XML file into its corresponding View  objects

        //public android.view.View inflate(@LayoutRes int resource,
        //                                 android.view.ViewGroup root)
        //Inflate a new view hierarchy from the specified xml resource. Throws InflateException if there is an error.
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.list_row, parent ,false);

        return new ViewHolder( view , context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Grocery grocery = groceryItems.get(position);

        viewHolder.groceryItemName.setText(grocery.getName());
        viewHolder.quantity.setText(grocery.getQuantity());
        viewHolder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryItems.size(); //returns list size which is same as groceryItems count
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public int id; //id of each item in DB

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            //list_row.xml
            groceryItemName = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);
            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Todo: go to next screen Details Activity

                    int positon = getAdapterPosition();

                    Grocery grocery = groceryItems.get(positon);

                    Intent intent = new Intent(context , DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    intent.putExtra("id", grocery.getId());

                    //cannot start activity directly as we are not in activity so using context
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.editButton :
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    editItem(grocery);
                    break;

                case R.id.deleteButton :
                    /*int*/ position = getAdapterPosition();
                    /*Grocery*/ grocery = groceryItems.get(position);
                    deleteItem( grocery.getId());
                    break;

            };
        }

        public void deleteItem(final int id) {

            //create an AlertDialog

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);

            final View view = inflater.inflate( R.layout.confirmation_alertdialog , null);
            //view will have everything confirmation_alertdialog will have

            Button noButton = (Button) view.findViewById( R.id.noButtonAD);
            Button yesButton = (Button) view.findViewById(R.id.yesButtonAD);

            alertDialogBuilder.setView(view); //setting alertdialogview
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete item
                    DataBaseHandler db = new DataBaseHandler(context);
                    //delete item
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        public void editItem(final Grocery grocery){

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItemID);
            final EditText quantity = (EditText) view.findViewById(R.id.groceryQtyID);

            final TextView title = (TextView) view.findViewById(R.id.titleID);
            title.setText("Edit Grocery");

            Button editsaveButton = (Button) view.findViewById(R.id.saveButtonID);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            editsaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataBaseHandler db = new DataBaseHandler(context);

                    //Update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty()
                            && !quantity.getText().toString().isEmpty()) {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else {
                        Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }

    }
}