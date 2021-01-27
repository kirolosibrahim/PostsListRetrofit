package com.motatawera.postslistretrofit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.motatawera.postslistretrofit.R;
import com.motatawera.postslistretrofit.adapter.PostsAdapter;

import com.motatawera.postslistretrofit.builders.PostBuilder;
import com.motatawera.postslistretrofit.databinding.ActivityPostsBinding;

import com.motatawera.postslistretrofit.databinding.AddpostalertLayoutBinding;
import com.motatawera.postslistretrofit.interfaces.ApiInterface;
import com.motatawera.postslistretrofit.model.PostsModel;
import com.motatawera.postslistretrofit.network.APIConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity implements PostsAdapter.onPostListener {
    AddpostalertLayoutBinding layoutBinding;
    List<PostsModel> postsModelList;
    PostsAdapter postsAdapter;
    ActivityPostsBinding binding;

    int userId;
    String title, body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_posts);
        postsModelList = new ArrayList<>();
        binding.ProgressPostsLists.setVisibility(View.VISIBLE);


        postsAdapter = new PostsAdapter(this, postsModelList, this);
        binding.rvPostList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPostList.setAdapter(postsAdapter);

        binding.fab.setOnClickListener(v -> {
            AddPost();
        });

        binding.swp.setOnRefreshListener(() -> {
            postsModelList = new ArrayList<>();
            GetPostsList();
        });


        GetPostsList();


    }


    private void GetPostsList() {

        ApiInterface apiInterface = APIConnection.getRetrofit().create(ApiInterface.class);

        Call<List<PostsModel>> call = apiInterface.getPosts();

        call.enqueue(new Callback<List<PostsModel>>() {
            @Override
            public void onResponse(Call<List<PostsModel>> call, Response<List<PostsModel>> response) {

                postsAdapter.setList(response.body());
                postsAdapter.notifyDataSetChanged();
                binding.ProgressPostsLists.setVisibility(View.GONE);
                binding.swp.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<PostsModel>> call, Throwable t) {
                binding.ProgressPostsLists.setVisibility(View.GONE);
                binding.swp.setRefreshing(false);
            }
        });


    }

    @Override
    public void onPathClick(int position, Context context, List<PostsModel> postsList) {

        Intent intent = new Intent(context, PathActivity.class)
                .putExtra("postId", postsList.get(position).getId());
        startActivity(intent);

    }

    @Override
    public void onQueryClick(int position, Context context, List<PostsModel> postsList) {

        Intent intent = new Intent(context, QueryActivity.class)
                .putExtra("postId", postsList.get(position).getId());
        startActivity(intent);

    }

    @Override
    public void onPutClick(int position, Context context, List<PostsModel> postsList) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.addpostalert_layout, null, false);
        alertDialog.setView(layoutBinding.getRoot());
        alertDialog.show();
        int id = postsList.get(position).getId();

        layoutBinding.btnDone.setOnClickListener(v -> {


            EditText a = layoutBinding.AddPostDialogUserid;

            userId = Integer.parseInt(a.getText().toString());

            title = layoutBinding.AddPostDialogTitle.getText().toString();
            body = layoutBinding.AddPostDialogBody.getText().toString();

            PostsModel postsModel = new PostBuilder().setUserId(userId).setTitle(title).setBody(body).setPost();
            ApiInterface apiInterface = APIConnection.getRetrofit().create(ApiInterface.class);


            Call<PostsModel> call = apiInterface.putPost(id, postsModel);

            call.enqueue(new Callback<PostsModel>() {
                @Override
                public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                    Toast.makeText(PostsActivity.this, response.body().getTitle(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostsModel> call, Throwable t) {
                    Toast.makeText(PostsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.dismiss();
        });

    }

    @Override
    public void onDeleteClick(int position, Context context, List<PostsModel> postsList) {
        int id = postsList.get(position).getId();
        ApiInterface apiInterface = APIConnection.getRetrofit().create(ApiInterface.class);


        Call<Void> call = apiInterface.deletePost(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(PostsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });


    }


    private void AddPost() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.addpostalert_layout, null, false);
        alertDialog.setView(layoutBinding.getRoot());
        alertDialog.show();


        layoutBinding.btnDone.setOnClickListener(v -> {


            EditText a = layoutBinding.AddPostDialogUserid;

            userId = Integer.parseInt(a.getText().toString());

            title = layoutBinding.AddPostDialogTitle.getText().toString();
            body = layoutBinding.AddPostDialogBody.getText().toString();

            PostsModel postsModel = new PostBuilder().setUserId(userId).setTitle(title).setBody(body).setPost();
            ApiInterface apiInterface = APIConnection.getRetrofit().create(ApiInterface.class);


            Call<PostsModel> call = apiInterface.setPost(postsModel);

            call.enqueue(new Callback<PostsModel>() {
                @Override
                public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                    Toast.makeText(PostsActivity.this, response.body().getTitle(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostsModel> call, Throwable t) {
                    Toast.makeText(PostsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.dismiss();
        });


    }
}