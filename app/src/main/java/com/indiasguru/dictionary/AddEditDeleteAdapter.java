package com.indiasguru.dictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 3/12/2018.
 */

public class AddEditDeleteAdapter extends RecyclerView.Adapter<AddEditDeleteAdapter.ViewHolder> {
    public AddEditDeleteAdapter(List<PremiumWord> premiumWordList, Context context) {
        this.wordList = premiumWordList;
        this.context = context;
        this.url = context.getResources().getString(R.string.url)+"/deleteSelectedWord";
    }

    private List<PremiumWord> wordList;
    private Context context;
    private AppDatabase appDatabase;
    public static UserObject userObject;
    private AlertDialog.Builder builder;
    private String url;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_add_edit_delete, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PremiumWord premiumWord = wordList.get(position);
        holder.word.setText(premiumWord.getWord());
        holder.meaning.setText(premiumWord.getMeaning());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView word, meaning;
        private AppCompatButton edit, delete;
        public ViewHolder(View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word);
            meaning = itemView.findViewById(R.id.meaning);
            edit = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteButton);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id;
            switch (v.getId()){
                case R.id.editButton:
                    id = wordList.get(getAdapterPosition()).getId();
                    GetUserFromLocalDatabaseInAdapter getUserFromLocalDatabaseInAdapter = new GetUserFromLocalDatabaseInAdapter(v.getContext(), id);
                    getUserFromLocalDatabaseInAdapter.execute();
                    break;
                case R.id.deleteButton:
                    final String id1 = wordList.get(getAdapterPosition()).getId();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Delete Word")
                            .setMessage("Are you sure to delete this entry")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    url = url+"?id="+id1;
                                    deleteWord(url);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
            }

        }

        private void deleteWord(final String url) {
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if(jsonResponse.getString("status").equals("success")){
                                    DeleteFromLocalDatabase.getInstance(context, null, wordList.get(getAdapterPosition())).execute("deletePremiumWord");
                                    wordList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(0, wordList.size());
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "Some internal server error occured", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Slow network or Check your internet connection", Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }
    }
}


class GetUserFromLocalDatabaseInAdapter extends AsyncTask<String, Void, UserObject> {
    private  AppDatabase appDatabase;
    private  UserObject user;
    private Context context;
    private String id;
    public GetUserFromLocalDatabaseInAdapter(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override
    protected UserObject doInBackground(String... voids) {
        appDatabase = AppDatabase.getAppDatabase(context);
        user = appDatabase.wordsDao().getLoggedInUser();
        return user;
    }

    @Override
    protected void onPostExecute(UserObject userObject) {
        AddEditDeleteAdapter.userObject = userObject;
        context.startActivity(new Intent(context,AddOrEdit.class).putExtra("url","/updateSelectedWord").putExtra("id",id)
                .putExtra("username", userObject.getUsername()).putExtra("password",userObject.getPassword()).putExtra("isAdmin", userObject.isAdmin()));
        ((Activity)context).finish();
    }
}
