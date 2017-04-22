package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.RequestManager;
import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.Dictionary;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;
import com.crazyhitty.chdev.ks.firebasechat.models.Meaning;
import com.crazyhitty.chdev.ks.firebasechat.models.Tuc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.BreakIterator;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GroupChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<GroupChat> mChats;
    private Activity mActivity;
    private RequestManager mRequestManager;

    public GroupChatRecyclerAdapter(List<GroupChat> chats, Activity activity, RequestManager requestManager) {
        mChats = chats;
        mActivity = activity;
        mRequestManager= requestManager;
    }

    public void add(GroupChat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    public void remove(int position) {
        mChats.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChats.size());
    }


    public void clear() {
        int size = this.mChats.size();
        this.mChats.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).sender,
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    public GroupChat getChat(int position) {
        return mChats.get(position);
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        GroupChat chat = mChats.get(position);

        //String alphabet = chat.sender.substring(0, 1);

        if(chat.imageurl.equals("")) {
            myChatViewHolder.txtChatMessage.setText(chat.message);
            myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            myChatViewHolder.imgchat.setVisibility(View.GONE);
        } else {
            //Glide.with(mActivity).load(Uri.parse(chat.imageurl)).into(myChatViewHolder.imgchat);
            mRequestManager.load(Uri.parse(chat.imageurl)).into(myChatViewHolder.imgchat);
            myChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            myChatViewHolder.imgchat.setVisibility(View.VISIBLE);
        }
        myChatViewHolder.txtUserAlphabet.setText("Me");
        myChatViewHolder.txtSentAt.setText("Sent at " + convertTime(chat.timestamp));
        linkifyMessage(myChatViewHolder.txtChatMessage);

    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        GroupChat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 2);

        if(chat.imageurl.equals("")) {
            otherChatViewHolder.txtChatMessage.setText(chat.message);
            otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            otherChatViewHolder.imgchat.setVisibility(View.GONE);
        } else {
            otherChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            mRequestManager.load(Uri.parse(chat.imageurl)).into(otherChatViewHolder.imgchat);
            otherChatViewHolder.imgchat.setVisibility(View.VISIBLE);
        }

        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
        otherChatViewHolder.txtSentAt.setText("Sent at " + convertTime(chat.timestamp));
        linkifyMessage(otherChatViewHolder.txtChatMessage);
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return format.format(date);
    }

    private void linkifyMessage(TextView txtChat) {
        String definition = txtChat.getText().toString().trim();
        txtChat.setMovementMethod(LinkMovementMethod.getInstance());
        txtChat.setText(definition, TextView.BufferType.SPANNABLE);
        txtChat.setTextColor(Color.WHITE);
        Spannable spans = (Spannable) txtChat.getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(definition);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = definition.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);
                //searchWordInDict(mWord);
                makeJsonObjReq(mWord);
                //Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT).show();
            }

            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.WHITE);
            }
        };
    }


    public void makeJsonObjReq(final String word) {
        final String url = "https://glosbe.com/gapi/translate?from=eng&dest=eng&format=json&phrase="+word+"&pretty=true";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());;
                        displayAlert(word, formatDefinitions(response.toString()), mActivity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        FirebaseChatMainApp.getInstance().addToRequestQueue(getRequest,"sometag");
    }


    private String formatDefinitions(String json) {
        String meanings = "Definitions : \n \n";
        Gson gson = new Gson();
        List<Meaning> mean;
        Dictionary dict = gson.fromJson(json, Dictionary.class);
        List<Tuc> tuc = dict.getTuc();
        if(tuc!=null && tuc.size()!=0) {
            if (tuc.get(0).getMeanings() != null) {
                mean = tuc.get(0).getMeanings();
            } else if (tuc.get(1).getMeanings() != null) {
                mean = tuc.get(1).getMeanings();
            } else if (tuc.get(2).getMeanings() != null) {
                mean = tuc.get(2).getMeanings();
            } else {
                return "The word does not have a meaning. Please try with other words";
            }
            if (mean != null) {
                int num = 1;
                for (Meaning m : mean) {
                    meanings += num + ". " + m.getText() + "\n";
                    num += 1;
                }
            }
        } else {
            return "The word does not have a meaning. Please try with other words";
        }
        return meanings;
    }

    private void displayAlert(String title, String message, Activity context){
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).sender,
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet,txtSentAt;
        private ImageView imgchat;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtSentAt = (TextView) itemView.findViewById(R.id.txtSentAt);
            imgchat = (ImageView) itemView.findViewById(R.id.imgchat);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet, txtSentAt;
        private ImageView imgchat;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtSentAt = (TextView) itemView.findViewById(R.id.txtSentAt);
            imgchat = (ImageView) itemView.findViewById(R.id.imgchat);
        }
    }
}
