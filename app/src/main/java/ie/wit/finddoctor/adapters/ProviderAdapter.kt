package ie.wit.finddoctor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.finddoctor.databinding.CardProviderBinding
import ie.wit.finddoctor.models.ProviderModel
import ie.wit.finddoctor.utils.customTransformation

interface ProviderClickListener {
    fun onProviderClick(provider: ProviderModel)
}

class ProviderAdapter constructor(private var providers: ArrayList<ProviderModel>,
                                  private val listener: ProviderClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<ProviderAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardProviderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val provider = providers[holder.adapterPosition]
        holder.bind(provider,listener)
    }

    fun removeAt(position: Int) {
        providers.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = providers.size

    inner class MainHolder(val binding : CardProviderBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(provider: ProviderModel, listener: ProviderClickListener) {
            binding.root.tag = provider
            binding.provider = provider
            Picasso.get().load(provider.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onProviderClick(provider) }
            binding.executePendingBindings()
        }
    }
}