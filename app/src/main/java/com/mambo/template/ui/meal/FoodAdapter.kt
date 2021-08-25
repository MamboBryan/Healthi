package com.mambo.template.ui.meal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mambo.template.R
import com.mambo.template.data.model.FoodItem
import com.mambo.template.databinding.ItemFoodBinding
import me.saket.cascade.CascadePopupMenu

class FoodAdapter(
    val onFoodItemClickListener: OnFoodItemClickListener
) : ListAdapter<FoodItem, FoodAdapter.FoodViewHolder>(FOOD_ITEM_COMPARATOR) {

    companion object {
        private val FOOD_ITEM_COMPARATOR =
            object : DiffUtil.ItemCallback<FoodItem>() {
                override fun areItemsTheSame(
                    oldItem: FoodItem,
                    newItem: FoodItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: FoodItem,
                    newItem: FoodItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding =
            ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class FoodViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                ivFoodMenu.setOnClickListener {

                    val foodItem = getItem(adapterPosition)

                    val popup = CascadePopupMenu(ivFoodMenu.context, ivFoodMenu)
                    popup.inflate(R.menu.menu_item_actions)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_edit -> {
                                onFoodItemClickListener.onFoodItemEditClicked(foodItem)
                            }
                            R.id.action_delete -> {
                                onFoodItemClickListener.onFoodItemDeleteClicked(foodItem)
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                    popup.show()

                }
            }
        }

        fun bind(foodItem: FoodItem) {
            binding.apply {
                val serving = if (foodItem.serving_quantity > 0) "serving" else "servings"
                tvFoodName.text = foodItem.name
                tvFoodServingCalories.text =
                    "${foodItem.calories_per_serving} kcal for ${foodItem.serving_quantity} $serving"
                tvFoodServings.text = "Servings : ${foodItem.user_servings}"
                tvFoodCalories.text = "${foodItem.total_calories} kcal"

                Glide.with(ivFoodImage).load(foodItem.image).into(ivFoodImage)
            }
        }

    }

    interface OnFoodItemClickListener {
        fun onFoodItemEditClicked(foodItem: FoodItem)
        fun onFoodItemDeleteClicked(foodItem: FoodItem)
    }

}