package com.mambo.template.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mambo.template.R
import com.mambo.template.data.model.FoodItem
import com.mambo.template.data.model.Meal
import com.mambo.template.databinding.ItemFoodBinding
import com.mambo.template.databinding.ItemMealBinding
import com.mambo.template.ui.meal.FoodAdapter
import me.saket.cascade.CascadePopupMenu
import java.text.SimpleDateFormat

class MealAdapter(
    val onMealItemClickListener: OnMealItemClickListener
) : ListAdapter<Meal, MealAdapter.MealViewHolder>(MEAL_ITEM_COMPARATOR) {

    companion object {
        private val MEAL_ITEM_COMPARATOR =
            object : DiffUtil.ItemCallback<Meal>() {
                override fun areItemsTheSame(
                    oldItem: Meal,
                    newItem: Meal
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Meal,
                    newItem: Meal
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding =
            ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class MealViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                ivMealOptions.setOnClickListener {

                    val meal = getItem(adapterPosition)

                    val popup = CascadePopupMenu(ivMealOptions.context, ivMealOptions)
                    popup.inflate(R.menu.menu_item_actions)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_edit -> {
                                onMealItemClickListener.onMealItemEditClicked(meal)
                            }
                            R.id.action_delete -> {
                                onMealItemClickListener.onMealItemDeleteClicked(meal)
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                    popup.show()

                }
            }
        }

        fun bind(meal: Meal) {

            binding.apply {

                val calories = meal.totalCalories().toInt().toString()

                tvMealTitle.text = meal.name
                tvMealDate.text = SimpleDateFormat("MMM dd, yyyy").format(meal.date)
                tvMealCalories.text = "$calories kcal"

                for (foodItem in meal.foods) {

                    val foodBinding =
                        ItemFoodBinding.inflate(LayoutInflater.from(binding.root.context))

                    foodBinding.apply {
                        ivFoodMenu.isVisible = false
                        val serving = if (foodItem.serving_quantity > 0) "serving" else "servings"
                        tvFoodName.text = foodItem.name
                        tvFoodServingCalories.text =
                            "${foodItem.calories_per_serving} kcal for ${foodItem.serving_quantity} $serving"
                        tvFoodServings.text = "Servings : ${foodItem.user_servings}"
                        tvFoodCalories.text = "${foodItem.total_calories} kcal"

                        Glide.with(ivFoodImage).load(foodItem.image).into(ivFoodImage)
                    }

                    val inflater =
                        LayoutInflater.from(binding.root.context).inflate(R.layout.item_food, null)
                    layoutAdd.addView(inflater, binding.layoutAdd.childCount)

//                    val viewGroup: ViewGroup = layoutAdd
//                    viewGroup.addView(foodBinding.root)

                }


            }
        }

    }

    interface OnMealItemClickListener {
        fun onMealItemEditClicked(meal: Meal)
        fun onMealItemDeleteClicked(meal: Meal)
    }

}