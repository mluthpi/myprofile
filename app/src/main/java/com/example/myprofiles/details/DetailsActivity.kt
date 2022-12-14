package com.example.myprofiles.details

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myprofiles.databinding.ActivityDetailsBinding
import com.example.myprofiles.details.followers.FollowersActivity
import com.example.myprofiles.details.following.FollowingActivity
import com.example.myprofiles.model.UserDetailsResponse
import com.example.myprofiles.model.UserRepositoryResponseItem

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "USERNAME"

    }

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var reposAdapter : DetailReposAdapter

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        username = intent.getStringExtra(USERNAME) ?: ""

        detailsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailsViewModel::class.java)
        detailsViewModel.getDetailsUser(username)
        detailsViewModel.getUserRepos(username)
        observeViewModel()


        binding.tvFollower.setOnClickListener{
            val intent = Intent(this, FollowersActivity::class.java)
            intent.putExtra(FollowersActivity.FOLLOWERS, username)
            startActivity(intent)
        }

        binding.tvFollowing.setOnClickListener {
            val intent = Intent(this, FollowingActivity::class.java)
            intent.putExtra(FollowingActivity.FOLLOWING, username)
            startActivity(intent)
        }
    }


    private fun observeViewModel() {
        detailsViewModel.detailsUser.observe(this, { userDetails ->
            showUserDetails(userDetails)
        })

        detailsViewModel.isLoading.observe(this, {isLoading ->
            showLoading(isLoading)
        })

        detailsViewModel.detailRepos.observe(this, {userDetailRepos ->
            showRepos(userDetailRepos)
        })
    }

    private fun showUserDetails(userDetailResponse: UserDetailsResponse) {
        Glide.with(this)
            .load(userDetailResponse.avatarUrl)
            .into(binding.imgDetail)
        binding.tvDetailName.text = userDetailResponse.name
        binding.tvFollowerValue.text = userDetailResponse.followers.toString()
        binding.tvFollowingValue.text = userDetailResponse.following.toString()
//        binding.tvCompany.text = userDetailResponse.company ?: ""
//        binding.tvLocation.text = userDetailResponse.location ?: ""
//        binding.tvRepoValue.text = userDetailResponse.publicRepos.toString()

        Log.e(TAG, "Observer viewmodel: $userDetailResponse")


        supportActionBar?.hide()

    }



    private fun showRepos(userReposItem: List<UserRepositoryResponseItem>) {
        reposAdapter = DetailReposAdapter()
        reposAdapter.addRepos(userReposItem)
        binding.rvRepo.apply { layoutManager = LinearLayoutManager(this@DetailsActivity,
            RecyclerView.VERTICAL, false)
            adapter = reposAdapter
        }
    }




    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}