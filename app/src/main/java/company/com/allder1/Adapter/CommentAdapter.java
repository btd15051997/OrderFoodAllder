package company.com.allder1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.com.allder1.R;
import company.com.allder1.model.Comment;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comment> listComment;
    Context mcontext;

    public CommentAdapter(Context mcontext, ArrayList<Comment> listComment) {
        this.listComment = listComment;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = layoutInflater.inflate(R.layout.itemcomment, viewGroup, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, final int i) {
        if (listComment == null) {

        } else if (listComment != null) {
            Comment comment = listComment.get(i);
            Holder.txtnameusers.setText(comment.getFirstName() + " " + comment.getLastName());
            Holder.txtcomment.setText(comment.getComment());
            if (comment.getLike() == 1) {
                Holder.ic_like.setVisibility(View.VISIBLE);
            } else {
                Holder.ic_like.setVisibility(View.GONE);
            }
            Holder.ratingBar_over.setRating(Float.valueOf(comment.getRating()));
            Holder.txtnumberrating.setText(comment.getRating()+"");
            if(comment.getPicture().isEmpty()){

            }else {
                String base64 = comment.getPicture();
                String base64Image = base64.split(",")[1];
                byte[] imageByteArray = Base64.decode(base64Image, android.util.Base64.DEFAULT);
                Glide.with(mcontext).load(imageByteArray).override(400, 400).centerCrop().into(Holder.profile_image);
            }

        }
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnameusers, txtcomment, txtnumberrating;
        RatingBar ratingBar_over;
        ImageView ic_like;
        private CircleImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnameusers = itemView.findViewById(R.id.txtnameusers);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            ratingBar_over = itemView.findViewById(R.id.ratingBar_over);
            ic_like = itemView.findViewById(R.id.imagelike);
            txtnumberrating = itemView.findViewById(R.id.txtnumberrating);
            profile_image = itemView.findViewById(R.id.iv_avata);
        }
    }

}
